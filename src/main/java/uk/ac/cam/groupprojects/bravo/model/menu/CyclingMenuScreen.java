package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

/**
 * Created by david on 20/02/2018.
 */
public class CyclingMenuScreen extends MenuScreen {

    public float screenProbability(){
        return 0.1f;
    }


    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.CYCLING_SCREEN;
    }
}
