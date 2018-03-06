package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Distance;
import uk.ac.cam.groupprojects.bravo.model.numbers.Time;

public class PausedScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {

        return !state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.PAUSED_SCREEN;
    }

    @Override
    public String formatSpeech(BikeStateTracker bikeStateTracker) {
        String result = "The bike is currently paused!\n";

        ConfigData configData = bikeStateTracker.getConfig();
        if (configData.isSpokenField(BikeField.DISTANCE)) {
            Distance d = (Distance)bikeStateTracker.getFieldValue(BikeField.DISTANCE);
            if (d != null)
                result += d.formatSpeech();
        }
        if (configData.isSpokenField(BikeField.TIME)) {
            Time t = (Time)bikeStateTracker.getFieldValue(BikeField.TIME);
            if (t != null)
                result += t.formatSpeech();
        }
        return result;
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
