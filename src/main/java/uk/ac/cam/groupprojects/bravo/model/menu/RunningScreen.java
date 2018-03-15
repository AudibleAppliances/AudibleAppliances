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
    private boolean loadTip = false;

    private int speakDelay = ApplicationConstants.DEFAULT_SPEAK_FREQ;

    @Override
    public boolean isActiveScreen(BikeStateTracker state) {

        return state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public String formatSpeech(BikeStateTracker bikeStateTracker) {
        if (!loadTip) {
            loadTip = true;
            return "You can use the wheel to adjust the difficulty";
        }

        String result = "";
        if (bikeStateTracker.isBoxActiveNow(ScreenBox.LOAD)) {
            Load l = (Load)bikeStateTracker.getFieldValue(BikeField.LOAD);
            if (l != null)
                result += l.formatSpeech() + "\n";
            speakDelay = ApplicationConstants.DEFAULT_SPEAK_FREQ / 5;
        } else {
            ConfigData configData = bikeStateTracker.getConfig();
            for (BikeField field : BikeField.values()) {
                if (configData.isSpokenField(field)) {
                    ScreenNumber n = bikeStateTracker.getFieldValue(field);
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
