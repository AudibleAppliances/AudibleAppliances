package uk.ac.cam.groupprojects.bravo.config;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.Program;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;

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
    LOAD(ScreenBox.LOAD),
    WATT(ScreenBox.WATT);

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

    /**
     * Get a new instance of the ScreenNumber associated with the enum value
     *
     * @return new ScreenNumber instance (of relevant class)
     */
    public ScreenNumber getScreenNumber() {
        switch(this) {
            case SPEED: return new Speed();
            case TIME: return new Time();
            case DISTANCE: return new Distance();
            case RPM: return new RPM();
            case CAL: return new Calories();
            case PULSE: return new Pulse();
            case GRAPH: return null;
            case PROGRAM: return new Program();
            case LOAD: return new Load();
            case WATT: return new Watt();
            default: return null;
        }
    }
}
