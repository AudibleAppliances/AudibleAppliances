package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * This screen is where you choose the program you want.
 * 
 */
public class SelectionScreen2 extends BikeScreen {

    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECTION_SCREEN_2;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak( bikeStateTracker.getFieldValue(BikeField.PROGRAM ).speakValue() );
        synthesiser.speak("Please click the start/stop button");
    }

    @Override
    public int getSpeakDelay() {
        return 4000;
    }
}
