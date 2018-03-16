package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

public class PausedScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean isActive = !state.isTimeChanging() &&
                            state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
                            state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
                            state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.PAUSED_SCREEN;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<String> dialogs = new ArrayList<>();
        dialogs.add("The bike is currently paused.");

        ConfigData configData = bikeStateTracker.getConfig();
        if (configData.isSpokenField(BikeField.DISTANCE)) {
            ScreenNumber n = bikeStateTracker.getFieldValue(BikeField.DISTANCE, false);
            if (n != null)
                dialogs.add(n.formatSpeech());
        }
        if (configData.isSpokenField(BikeField.TIME)) {
            ScreenNumber n = bikeStateTracker.getFieldValue(BikeField.TIME, false);
            if (n != null)
                dialogs.add(n.formatSpeech());
        }
        return dialogs;
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
