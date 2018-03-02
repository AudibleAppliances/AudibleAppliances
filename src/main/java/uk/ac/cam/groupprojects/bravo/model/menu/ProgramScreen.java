package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
// This is the screen where the user selects which program they want to use
public class ProgramScreen extends BikeScreen {
    @Override
    public float screenActiveProbability(BikeStateTracker bikeStateTracker) {
        int matching = 0;

        

        return (float)matching / (float)ScreenBox.values().length;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.PROGRAM;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        //We need to print what program it is
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
