package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.config.BikeField;

/**
 * Types of boxes to read on the bike display screen. Each box also has the kinds of field it can contain.
 *
 * @author Oliver Hope
 */
public enum ScreenBox {
    LCD1(BikeField.TIME),
    LCD2(BikeField.SPEED),
    LCD3(BikeField.DISTANCE),
    LCD4(BikeField.CAL),
    LCD5(BikeField.WATT, BikeField.RPM),
    LCD6(BikeField.PROGRAM, BikeField.LEVEL),
    LCD7(BikeField.PULSE),
    GRAPH, WATT, RPM, PROGRAM, LEVEL;

    private BikeField[] fields;

    ScreenBox(BikeField... fields) {
        this.fields = fields;
    }

    public BikeField[] getFields() {
        return fields.clone();
    }
}
