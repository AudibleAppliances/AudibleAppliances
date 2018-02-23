package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * This screen is the initial screen of when we turn on the device.
 * The only screen available from this screen is SelectionScreen2
 */
public class SelectionScreen1 extends BikeScreen {


    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECTION_SCREEN_1;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("Please click the start/stop button");
    }

    @Override
    public int getSpeakDelay() {
        return 1000;
    }


}