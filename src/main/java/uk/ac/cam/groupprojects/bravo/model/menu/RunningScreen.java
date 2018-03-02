package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 20/02/2018.
 */
public class RunningScreen extends BikeScreen {

    private boolean loadTip = false;

    @Override
    public float screenActiveProbability(BikeStateTracker bikeStateTracker) {
        int matching = 0;
        
        return 0;
        //throw new UnsupportedOperationException("Not implemented");
        //return (float)matching / (float)ScreenBox.values().length;
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
