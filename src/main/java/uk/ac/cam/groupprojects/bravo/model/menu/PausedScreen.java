package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class PausedScreen extends BikeScreen {
    @Override
    public boolean getFeatures(BikeStateTracker state) {

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
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("The bike is currently paused!");
        ConfigData configData = bikeStateTracker.getConfig();
        if ( configData.isSpokenField(BikeField.DISTANCE ) ){
            synthesiser.speak( bikeStateTracker.getFieldValue( BikeField.DISTANCE ).speakValue() );
        }
        if ( configData.isSpokenField(BikeField.TIME ) ){
            synthesiser.speak( bikeStateTracker.getFieldValue( BikeField.TIME ).speakValue() );
        }
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
