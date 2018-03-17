package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.List;
import java.util.Arrays;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;

public class OffScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        return !state.isTimeChanging() &&
                Arrays.stream(ScreenBox.values()).allMatch(b -> state.getBoxState(b) == LCDState.SOLID_OFF);
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.OFF_SCREEN;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        return Arrays.asList("The screen is currently off, please press any button to continue");
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
