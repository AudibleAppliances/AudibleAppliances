package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

public abstract class BikeScreen {
    public abstract boolean isActiveScreen(BikeStateTracker state);

    public abstract ScreenEnum getEnum();

    public abstract String formatSpeech(BikeStateTracker bikeStateTracker);

    public abstract int getSpeakDelay();

    //When we change to this screen, do we immediately want to notify the user
    public abstract boolean isSpeakFirst();
}