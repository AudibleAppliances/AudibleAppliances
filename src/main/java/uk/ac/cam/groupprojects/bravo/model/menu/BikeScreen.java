package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

public abstract class BikeScreen {

    public abstract float screenProbability( BikeStateTracker bikeStateTracker );

    public abstract ScreenEnum getEnum();

}