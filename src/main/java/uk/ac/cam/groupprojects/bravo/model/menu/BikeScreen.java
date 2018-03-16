package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.List;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

public abstract class BikeScreen {
    public abstract boolean isActiveScreen(BikeStateTracker state);

    public abstract ScreenEnum getEnum();

    public abstract List<String> formatSpeech(BikeStateTracker bikeStateTracker);

    public abstract int getSpeakDelay();

    // When we change to this screen, do we immediately want to notify the user
    public abstract boolean isSpeakFirst();
}