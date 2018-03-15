package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Load;
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

        if (isActive) {
            System.out.println("Distance is " + state.getFieldValue(BikeField.DISTANCE, false).getValue());
        }

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public String formatSpeech(BikeStateTracker bikeStateTracker) {
        String result = "";

        if (isActive && !wasActive) {
            result += "Now cycling, you can use the wheel to adjust the difficulty.";
        }

        if (bikeStateTracker.isBoxActiveNow(ScreenBox.LOAD)) {
            Load l = (Load)bikeStateTracker.getFieldValue(BikeField.LOAD, false);
            if (l != null)
                result += l.formatSpeech() + "\n";
            speakDelay = ApplicationConstants.DEFAULT_SPEAK_FREQ / 5;
        } else {
            ConfigData configData = bikeStateTracker.getConfig();
            for (BikeField field : BikeField.values()) {
                if (configData.isSpokenField(field)) {
                    ScreenNumber n = bikeStateTracker.getFieldValue(field, false);
                    if (n != null)
                        result += n.formatSpeech() + "\n";
                }
            }
            speakDelay = ApplicationConstants.DEFAULT_SPEAK_FREQ;
        }

        return result;
    }

    @Override
    public int getSpeakDelay() {
        return speakDelay;
    }

    @Override
    public boolean isSpeakFirst() {
        return false;
    }
}
