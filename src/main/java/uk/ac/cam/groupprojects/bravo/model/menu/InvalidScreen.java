package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

public class InvalidScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        return false;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.INVALID_SCREEN;
    }

    @Override
    public String formatSpeech(BikeStateTracker bikeStateTracker) {
        return "Oh dear, something may have gone wrong. If this message repeats, press and hold the reset button until you hear a beep.";
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
