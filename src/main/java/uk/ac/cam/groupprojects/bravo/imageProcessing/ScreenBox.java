package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.config.BikeField;

/**
 * Types of boxes to read on the bike display screen. Each box also has the kinds of field it can contain.
 *
 * @author Oliver Hope
 */
public enum ScreenBox {
    LCD1,
    LCD2,
    LCD3,
    LCD4,
    LCD5,
    LCD6,
    GRAPH,
    SPEED,
    RPM,
    WATT,
    LOAD,

    // Text boxes along bottom of graph
    LCD_TEXT_1,
    LCD_TEXT_3,
    LCD_TEXT_4,
    LCD_TEXT_5_TOP,
    LCD_TEXT_9,
    LCD_TEXT_10,
    LCD_TEXT_11,

    // Program is the combination of two of the LCD boxes
    PROGRAM;

    private static final double BLACK_THRESHOLD = 60;
    private static final double BLUE_THRESHOLD = 80;

    public BikeField[] getFields() {
        switch (this) {
            case LCD1: return new BikeField[] { BikeField.TIME };
            case LCD2: return new BikeField[] { BikeField.PULSE };
            case LCD3: return new BikeField[] { BikeField.SPEED, BikeField.RPM };
            case LCD4: return new BikeField[] { BikeField.DISTANCE };
            case LCD5: return new BikeField[] { BikeField.WATT, BikeField.LOAD };
            case LCD6: return new BikeField[] { BikeField.CAL };
            default: return null;
        }
    }


    public boolean isBlueBackground() {
        switch (this) {
            case LCD1:
            case LCD2:
            case LCD3:
            case LCD4:
            case LCD5:
            case LCD6:
            case GRAPH:
            case SPEED:
            case RPM:
            case WATT:
            case LOAD: return true;
            default:   return false;
        }
    }
    public boolean isBlackBackground() {
        return !isBlueBackground();
    }

    public double getThreshold() {
        if (isBlackBackground()) {
            return BLACK_THRESHOLD;
        } else {
            return BLUE_THRESHOLD;
        }
    }

    public boolean needsScaling() {
        switch (this) {
            case LCD1: return true;
            default:   return false;
        }
    }
    public double scaleFactor() {
        switch (this) {
            case LCD1: return 0.5;
            default:   return 1;
        }
    }
}
