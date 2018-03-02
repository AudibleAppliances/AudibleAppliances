package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

public abstract class BikeScreen {
    public abstract float screenActiveProbability(BikeStateTracker bikeStateTracker);

    public abstract ScreenEnum getEnum();

    public abstract void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser );

    public abstract int getSpeakDelay();

    //When we change to this screen, do we immediately want to notify the user
    public abstract boolean isSpeakFirst();
}