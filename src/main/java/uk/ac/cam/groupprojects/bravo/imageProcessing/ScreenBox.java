package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.config.BikeField;

/**
 * Types of boxes to read on the bike display screen. Each box also has the kinds of field it can contain.
 *
 * @author Oliver Hope
 */
public enum ScreenBox {

    LCD1(BikeField.TIME),
    LCD2(BikeField.PULSE),
    LCD3(BikeField.SPEED, BikeField.RPM),
    LCD4(BikeField.DISTANCE),
    LCD5(BikeField.WATT, BikeField.LOAD),
    LCD6(BikeField.CAL),
    GRAPH, PROGRAM, WATT, RPM, LOAD,

    // This is disgusting but desperate times
    LCD_TEXT_1,
    LCD_TEXT_3,
    LCD_TEXT_4,
    LCD_TEXT_5_TOP,
    LCD_TEXT_9,
    LCD_TEXT_10,
    LCD_TEXT_11;

    private BikeField[] fields;

    ScreenBox(BikeField... fields) {
        this.fields = fields;
    }

    public BikeField[] getFields() {
        return fields.clone();
    }

    public boolean isBlueBackground() {
        switch(this) {
            case LCD1:
            case LCD2:
            case LCD3:
            case LCD4:
            case LCD5:
            case LCD6: return true;
            // All others
            default:   return false;
        }
    }
    public boolean isBlackBackground() {
        return !isBlueBackground();
    }
}
