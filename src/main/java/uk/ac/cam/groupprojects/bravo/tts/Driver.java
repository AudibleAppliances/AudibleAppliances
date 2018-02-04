package uk.ac.cam.groupprojects.bravo.tts;

import java.util.List;

public class Driver {
    public static void main(String[] args) {
        try {
            List<String> voices = Synthesiser.getVoices();
            if (voices.size() == 0)
                throw new VoiceMissingException();

            // Test installed voices
            try (Synthesiser synth = new Synthesiser()) {
                for (String voice : voices) {
                    System.out.println(voice);
                    synth.setVoice(voice);
                    synth.speak("Hello, this is a test.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}