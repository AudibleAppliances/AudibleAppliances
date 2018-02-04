package uk.ac.cam.groupprojects.bravo.tts;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

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
    public Synthesiser(String voice) throws FestivalMissingException, VoiceMissingException {
        this();
        setVoice(voice);
    }

    public void setVoice(String voice) throws VoiceMissingException {
        write("(voice_" + voice + ")");
        
        // If the process didn't echo the voice back to us, there was an error
        String output = readLine();
        if (!output.equals(voice)) {
            throw new VoiceMissingException("Missing voice: " + voice + ". Instead got: " + output);
        }
    }

    public void speak(String text) {
        write("(SayText \"" + text + "\")");
        readLine(); // Discard the next line of input (contains "utterance" information)
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

    private void write(String s) {
        out.write(s + "\n");
        out.flush();
    }
    private String read() {
        String s;
        do {
            s = in.next();
        } while (s.startsWith("festival>"));
        return s;
    }
    private String readLine() {
        String s = in.nextLine().trim();
        if (s.startsWith("festival> ")) {
            s = s.substring(10);
        }
        return s;
    }

    public static List<String> getVoices() throws FestivalMissingException {
        try (Synthesiser synth = new Synthesiser()) {
            // Tell the festival process to list the available voices
            synth.write("(voice.list)");
    
            // Read the formatted output (the voice list)
            // Usual output would be eg. "(kal_diphone us1_mbrola cmu_us_awb_arctic_clunits)"
            List<String> voices = new ArrayList<>();
            Boolean seenLastVoice = false;
            while (!seenLastVoice) {
                String s = synth.read();
                if (s.startsWith("(")) { // First item
                    s = s.substring(1);
                } else if (s.endsWith(")")) { // Last item
                    s = s.substring(0, s.length() - 1);
                    seenLastVoice = true;
                }
                voices.add(s);
            }
    
            // Wait for the process to terminate, then return our results
            return voices;
        }
    }

    private static Process spawnFestivalProcess() throws FestivalMissingException {
        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("festival", "--interactive"));
            return pb.start();
        } catch (IOException e) {
            throw new FestivalMissingException(e);
        }
    }
}