package uk.ac.cam.groupprojects.bravo.config;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.numbers.Program;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;

/**
 * Fields of the screen that can hold data (and the title active when shown on the display).
 *
 * @author Oliver Hope
 */
public enum BikeField {
    SPEED,
    TIME,
    DISTANCE,
    RPM,
    CAL,
    PULSE,
    GRAPH,
    PROGRAM,
    LOAD,
    WATT;

    /**
     * Get the ScreenBox that can contain this field
     *
     * @return The relevant ScreenBox
     */
    public ScreenBox getScreenBox() {
        switch (this) {
            case SPEED: return ScreenBox.LCD3;
            case TIME: return ScreenBox.LCD1;
            case DISTANCE: return ScreenBox.LCD4;
            case RPM: return ScreenBox.LCD3;
            case CAL: return ScreenBox.LCD6;
            case PULSE: return ScreenBox.LCD2;
            case GRAPH: return ScreenBox.GRAPH;
            case PROGRAM: return ScreenBox.PROGRAM;
            case LOAD: return ScreenBox.LCD5;
            case WATT: return ScreenBox.LCD5;
            default: return null;
        }
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
