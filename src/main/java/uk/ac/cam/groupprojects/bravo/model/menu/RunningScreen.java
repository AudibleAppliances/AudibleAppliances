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

public class RunningScreen extends BikeScreen {

    private int speakDelay = ApplicationConstants.RUNNING_SPEAK_FREQ;

    private boolean wasActive = false;
    private boolean isActive = false;

    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        wasActive = isActive;
        isActive = state.isTimeChanging() &&
                           state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
                           state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
                           state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<String> dialogs = new ArrayList<>();

        if (isActive && !wasActive) {
            dialogs.add("Now cycling, you can use the wheel to adjust the difficulty.");
        }

        if (bikeStateTracker.isBoxActiveNow(ScreenBox.LOAD)) {
            ScreenNumber n = bikeStateTracker.getFieldValue(BikeField.LOAD, false);
            if (n != null)
                dialogs.add(n.formatSpeech());
            speakDelay = ApplicationConstants.DEFAULT_SPEAK_FREQ / 5;
        } else {
            ConfigData configData = bikeStateTracker.getConfig();
            for (BikeField field : BikeField.values()) {
                if (configData.isSpokenField(field)) {
                    ScreenNumber n = bikeStateTracker.getFieldValue(field, false);
                    if (n != null)
                        dialogs.add(n.formatSpeech());
                }
            }
            speakDelay = ApplicationConstants.RUNNING_SPEAK_FREQ;
        }

        return dialogs;
    }

    @Override
    public int getSpeakDelay() {
        return speakDelay;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
