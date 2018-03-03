package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.HashMap;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

public abstract class BikeScreen {
    public abstract HashMap<ScreenBox, LCDState> getFeatures(BikeStateTracker state);

    public abstract ScreenEnum getEnum();

    public abstract void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser );

    public abstract int getSpeakDelay();

    //When we change to this screen, do we immediately want to notify the user
    public abstract boolean isSpeakFirst();
}