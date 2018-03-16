package uk.ac.cam.groupprojects.bravo.tts;

public class SpeakCommand implements Command {
    public String text;
    public SpeakCommand(String text) { this.text = text; }
}