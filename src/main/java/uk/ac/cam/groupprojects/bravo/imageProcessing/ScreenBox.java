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
    GRAPH, PROGRAM, WATT, RPM, LOAD,

    // This is disgusting but desperate times
    LCD_TEXT_1,
    LCD_TEXT_3,
    LCD_TEXT_4,
    LCD_TEXT_5_TOP,
    LCD_TEXT_9,
    LCD_TEXT_10,
    LCD_TEXT_11;

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
}
