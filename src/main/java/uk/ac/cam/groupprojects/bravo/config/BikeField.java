package uk.ac.cam.groupprojects.bravo.config;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;

/**
 * Fields of the screen that can hold data (and the title active when shown on the display).
 *
 * @author Oliver Hope
 */
public enum BikeField {
    SPEED(ScreenBox.SPEED),
    TIME,
    DISTANCE,
    RPM(ScreenBox.RPM),
    CAL,
    PULSE,
    GRAPH,
    PROGRAM,
    WATT(ScreenBox.WATT),
    LOAD(ScreenBox.LOAD);

    // Boxes containing titles showing if the display is active
    private ScreenBox[] titleBoxes;

    BikeField(ScreenBox... title) {
        this.titleBoxes = title;
    }

    /**
     * Get the box containing the relevant title
     *
     * @return The relevant ScreenBox, or null if there isn't one.
     */
    public ScreenBox getTitleBox() {
        if (titleBoxes.length == 0) return null;
        else return titleBoxes[0];
    }
}
