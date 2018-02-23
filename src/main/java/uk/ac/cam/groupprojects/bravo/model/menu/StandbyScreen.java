package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 22/02/2018.
 */
public class StandbyScreen extends BikeScreen {

    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.OFF_SCREEN;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("The screen is currently off, please click any button to continue!");
    }

    @Override
    public int getSpeakDelay() {
        return 10000;
    }
}
