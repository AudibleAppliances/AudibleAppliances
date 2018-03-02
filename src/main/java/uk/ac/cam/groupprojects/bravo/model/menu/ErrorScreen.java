package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class ErrorScreen extends BikeScreen {
    @Override
    public float screenActiveProbability(BikeStateTracker state) {
        int matching = 0;
        for (ScreenBox box : ScreenBox.values()) {
            matching += state.boxStateIndicator(box, LCDState.SOLID_OFF);
        }

        return (float)matching / (float)ScreenBox.values().length;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.ERROR_SCREEN;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("The bike is currently loading up, please wait!");
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
