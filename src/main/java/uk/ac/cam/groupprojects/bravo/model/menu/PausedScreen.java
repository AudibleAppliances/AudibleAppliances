package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class PausedScreen extends BikeScreen {
    @Override
    public float screenActiveProbability(BikeStateTracker bikeStateTracker) {
        int matching = 0;

        // TODO: We need to know if the graph is blinking.......

        return (float)matching / (float)ScreenBox.values().length;
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
