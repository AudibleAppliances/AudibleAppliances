package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class SelectHRCScreen extends BikeScreen {
    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECT_HRC;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("Currently selecting HRC. Please click Start/Stop to start riding!");
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ / 2;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
