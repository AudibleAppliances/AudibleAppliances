package uk.ac.cam.groupprojects.bravo.tts;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

// Interfaces with the "festival" program.
// It has a "pipe" mode, where it just accepts commands and performs them, but that doesn't
// report error messages and the like (which is desirable, and necessary to get the voice list).
// This class uses the "interactive" interface, which means we have to skip the
// "startup banner" (wall of text), and skip past any prompts ("festival> ") that are presented.

public class Synthesiser implements AutoCloseable {
    private final Process festival;
    private final PrintWriter out;
    private final Scanner in;

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
    }
    public Synthesiser(String voice) throws FestivalMissingException,
                                            VoiceMissingException {
        this();
        setVoice(voice);
    }

    public void setVoice(String voice) throws VoiceMissingException {
        // Sets the voice used by Festival
        write("(voice_" + voice + ")");
        
        // If the process didn't echo the voice back to us, there was an error
        String output = readLine();
        if (!output.equals(voice)) {
            throw new VoiceMissingException("Missing voice: " + voice + ". Instead got: " + output);
        }
    }

    // Sets the rate of playback of the voice
    // rate is measured in Hertz - valid values are from 2000 to 192000 inclusive (see aplay's documentation)
    public void setRate(int rate) throws RateSetException {
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

    public void speak(String text) {
        // Request Festival to synthesise the text
        write("(SayText \"" + text + "\")");
        // Discard the next line of input (contains "utterance" information)
        // Festival only output a line after it's finished speaking, so this also causes us to
        // block until it's done speaking (desirable behaviour).
        readLine();
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
        out.write(s + "\n");
        out.flush();
    }
    // Read a single whitespace-delimited token from the process' output.
    // Ignore tokens containing the festival prompt ("festival>")
    private String read() {
        String s;
        do {
            s = in.next();
        } while (s.startsWith("festival>"));
        return s;
    }
    // Reada newline-delimited string from the process' output.
    // Ignore tokens containing the festival prompt ("festival>")
    private String readLine() {
        String s = in.nextLine().trim();
        if (s.startsWith("festival> ")) {
            s = s.substring(10);
        }
        return s;
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