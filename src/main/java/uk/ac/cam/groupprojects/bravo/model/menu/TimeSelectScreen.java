package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class TimeSelectScreen extends BikeScreen {
    @Override
    public float screenActiveProbability(BikeStateTracker bikeStateTracker) {
        // TODO    
        return 0;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.TIME_SELECT;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {

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
