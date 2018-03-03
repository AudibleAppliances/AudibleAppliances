package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 20/02/2018.
 */
public class RunningScreen extends BikeScreen {

    private boolean loadTip = false;

    @Override
    public boolean getFeatures(BikeStateTracker state) {

        return state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        if ( !loadTip ){
            synthesiser.speak("You can use the wheel to adjust the difficulty");
            loadTip = true;
        }
        ConfigData configData = bikeStateTracker.getConfig();
        for (BikeField field : BikeField.values()) {
            if (configData.isSpokenField(field)) {
                synthesiser.speak( bikeStateTracker.getFieldValue(field).speakValue() );
            }
        }
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ;
    }

    @Override
    public boolean isSpeakFirst() {
        return false;
    }
}
