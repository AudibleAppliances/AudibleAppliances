package uk.ac.cam.groupprojects.bravo.config;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.numbers.Speed;

/**
 * Fields that can be spoken out loud
 *
 * @author Oliver Hope
 */
public enum BikeField {
    SPEED,
    TIME,
    DISTANCE,
    RPM(ScreenBox.RPM),
    LEVEL(ScreenBox.LEVEL),
    CAL,
    PULSE,
    GRAPH,
    PROGRAM(ScreenBox.PROGRAM),
    WATT(ScreenBox.WATT);

    private ScreenBox[] titleBoxes;

    BikeField(ScreenBox... title) {
        this.titleBoxes = title;
    }

    public ScreenBox getTitleBox() {
        if (titleBoxes.length == 0) return null;
        else return titleBoxes[0];
    }
}
