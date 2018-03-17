package uk.ac.cam.groupprojects.bravo.tts;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

/**
 * Interfaces with the "festival" program.
 * It has a "pipe" mode, where it just accepts commands and performs them, but that doesn't
 * report error messages and the like (which is desirable, and necessary to get the voice list).
 * This class uses the "interactive" interface, which means we have to skip the
 * "startup banner" (wall of text), and skip past any prompts ("festival> ") that are presented.
 */
public class Synthesiser implements AutoCloseable {
    private final Process festival;
    private final PrintWriter out;
    private final Scanner in;

    // Command queue
    private final LinkedList<Command> commandQueue;
    private final Semaphore availableCommands;

    public Synthesiser() throws FestivalMissingException {
        festival = spawnFestivalProcess();
        out = new PrintWriter(festival.getOutputStream());
        in = new Scanner(festival.getInputStream());

        // Read past the header of the program
        // Read words (next), not lines (nextLine) as the final header line isn't ended with a newline
        String s = "";
        while (!s.startsWith("festival>")) {
            s = in.next();
        }

        commandQueue = new LinkedList<>();
        availableCommands = new Semaphore(0);

        // Speak thread to pull from queue and speak
        Thread speakThread = new Thread(() -> {
            while (true) {
                try {
                    // Take next command from queue
                    Command c = null;
                    while (c == null) {
                        // Wait until there are commands available
                        availableCommands.acquire();
                        synchronized (commandQueue) {
                            if (commandQueue.isEmpty()) {
                                // The only situation in which we can acquire semaphore permission to read but then
                                // encounter an empty queue is if the queue was cleared - in this case, just try again.
                                continue;
                            }
                            c = commandQueue.poll();
                        }
                    }

                    if (c instanceof SpeakCommand) {
                        String toSpeak = ((SpeakCommand)c).text;
                        // Request Festival to synthesise the text
                        write("(SayText \"" + toSpeak + "\")");

                        // Discard the next line of input (contains "utterance" information)
                        // Festival only outputs a line after it's finished speaking, so this also causes us to
                        // block until it's done speaking (desirable behaviour).
                        readLine();
                    }
                    else if (c instanceof DelayCommand) {
                        Thread.sleep(((DelayCommand)c).millis);
                    }
                } catch (InterruptedException e) {
                    // Empty as we want to keep going if this happens
                }
            }
        });

        speakThread.setDaemon(true);
        speakThread.start();
    }
    public Synthesiser(String voice) throws FestivalMissingException,
                                            VoiceMissingException {
        this();
        setVoice(voice);
    }

    public void setVoice(String voice) throws VoiceMissingException {
        synchronized (festival) {
            // Sets the voice used by Festival
            write("(voice_" + voice + ")");
            
            // If the process didn't echo the voice back to us, there was an error
            String output = readLine();
            if (!output.equals(voice)) {
                throw new VoiceMissingException("Missing voice: " + voice + ". Instead got: " + output);
            }
        }
    }

    // Sets the rate of playback of the voice
    // rate is measured in Hertz - valid values are from 2000 to 192000 inclusive (see aplay's documentation)
    public void setRate(int rate) throws RateSetException {
        synchronized (festival) {
            if (rate < 2000 || rate > 192000) {
                throw new RateSetException("Invalid rate: must be between 2000Hz and 192000Hz inclusive.");
            }

            // Get the current command used by festival to play audio
            write("(Parameter.get \"Audio_Command\")");

            // Check it contains a suitable rate parameter
            String pattern = "-r (\\$SR|\\d+)";
            String currentSetting = readLine();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(currentSetting);
            if (!m.find()) {
                throw new RateSetException("Failed to set rate. Got current setting: \"" + currentSetting + "\"");
            }

            // Overwrite the rate parameter with our rate
            String newSetting = m.replaceFirst("-r " + rate);
            // Omit the "" around the new setting as it already contains them (as we read it from the output)
            write("(Parameter.set \"Audio_Command\" " + newSetting + ")");

            // Check festival updated correctly
            String feedback = readLine();
            if (!feedback.equals(newSetting)) {
                throw new RateSetException("Failed to set rate. Got feedback: \"" + feedback + "\"");
            }
        }
    }

    public int getQueueSize() {
        synchronized (commandQueue) {
            return commandQueue.size();
        }
    }

    public void clearQueue() {
        // To reset the command queue, we first acquire exclusive access and clear it, then we
        // reset the semaphore to 0. This is compatible with the enqueueCommands function, but causes
        // a special case in the reader thread (it can be issued a permit by the semaphore then see an empty queue)
        synchronized (commandQueue) {
            System.out.println("Before clearing: " + commandQueue.size());
            commandQueue.clear();
            System.out.println("After clearing: " + commandQueue.size());
            availableCommands.drainPermits();
        }
    }

    public void speak(String... texts) {
        // Build up a list of "speak-delay" pairs
        List<Command> commands = new ArrayList<>(texts.length * 2);
        for (String s : texts) {
            commands.add(new SpeakCommand(s));
            commands.add(new DelayCommand());
        }
        enqueueCommands(commands);
    }
    public void delay(int millis) {
        enqueueCommands(new DelayCommand(millis));
    }
    // Put a list of commands into the queue for processing
    public void enqueueCommands(Command... commands) {
        enqueueCommands(Arrays.asList(commands));
    }
    public void enqueueCommands(List<Command> commands) {
        // Get exclusive access to the queue, add all our commands, signal that there's that many new commands
        synchronized (commandQueue) {
            commandQueue.addAll(commands);
            availableCommands.release(commands.size());
        }
    }
    
    @Override
    public void close() {
        try {
            in.close();
            out.close();
            festival.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Set the interrupted flag
        }
    }

    // Output a command to the festival process
    private void write(String s) {
        synchronized (festival) {
            out.write(s + "\n");
            out.flush();
        }
    }
    // Read a single whitespace-delimited token from the process' output.
    // Ignore tokens containing the festival prompt ("festival>")
    private String read() {
        synchronized (festival) {
            String s;
            do {
                s = in.next();
            } while (s.startsWith("festival>"));
            return s;
        }
    }
    // Reada newline-delimited string from the process' output.
    // Ignore tokens containing the festival prompt ("festival>")
    private String readLine() {
        synchronized (festival) {
            String s = in.nextLine().trim();
            if (s.startsWith("festival> ")) {
                s = s.substring(10);
            }
            return s;
        }
    }

    public static List<String> getVoices() throws FestivalMissingException,
                                                  InvalidVoiceListException {
        try (Synthesiser synth = new Synthesiser()) {
            // Tell the festival process to list the available voices
            synth.write("(voice.list)");

            // Read the formatted output (the voice list)
            // Usual output would be eg. "(kal_diphone us1_mbrola cmu_us_awb_arctic_clunits)"
            List<String> voices = new ArrayList<>();

            String s = synth.read();
            if (!s.startsWith("(")) {
                throw new InvalidVoiceListException("Got invalid token after retrieving voices: \"" + s + "\"");
            }
            // Trim the opening bracket, store it
            s = s.substring(1);
            voices.add(s);

            boolean lastVoice = false;
            while (!lastVoice) {
                s = synth.read();

                if (s.endsWith(")")) { // Last item
                    s = s.substring(0, s.length() - 1);
                    lastVoice = true;
                }
                voices.add(s);
            }

            // Wait for the process to terminate, then return our results
            return voices;
        }
    }

    // Create a process in interactive mode, ready for usage
    private static Process spawnFestivalProcess() throws FestivalMissingException {
        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("festival", "--interactive"));
            return pb.start();
        } catch (IOException e) {
            throw new FestivalMissingException(e);
        }
    }
}