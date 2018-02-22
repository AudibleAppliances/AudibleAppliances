package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;

/**
 * This screen is the initial screen of when we turn on the device.
 * The only screen available from this screen is SelectionScreen2
 */
public class SelectionScreen1 extends MenuScreen {


    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {

        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECTION_SCREEN_1;
    }

}
