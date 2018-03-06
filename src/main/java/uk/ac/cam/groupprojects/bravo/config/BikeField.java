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
    SPEED(ScreenBox.LCD3),
    TIME(ScreenBox.LCD1),
    DISTANCE(ScreenBox.LCD4),
    RPM(ScreenBox.LCD3),
    CAL(ScreenBox.LCD6),
    PULSE(ScreenBox.LCD2),
    GRAPH(ScreenBox.GRAPH),
    PROGRAM(ScreenBox.PROGRAM),
    LOAD(ScreenBox.LCD5),
    WATT(ScreenBox.LCD5);

    // Boxes containing titles showing if the display is active
    private final ScreenBox screenBox;

    private BikeField(ScreenBox box) {
        this.screenBox = box;
        if (box == null) {
            System.out.println(this.toString());
            Thread.dumpStack();
        }
    }

    /**
     * Get the ScreenBox that can contain this field
     *
     * @return The relevant ScreenBox
     */
    public ScreenBox getScreenBox() {
        return screenBox;
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
