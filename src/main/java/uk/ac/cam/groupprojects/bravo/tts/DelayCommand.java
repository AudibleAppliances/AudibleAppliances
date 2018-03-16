package uk.ac.cam.groupprojects.bravo.tts;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

public class DelayCommand implements Command {
    public int millis;
    public DelayCommand() { this(ApplicationConstants.DEFAULT_SPEECH_PAUSE); }
    public DelayCommand(int millis) { this.millis = millis; }
}