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
    GRAPH, TEXT, SPEED, RPM, WATT, LOAD,

    //This is disgusting but desperate times
    TEXT_BOX_1,
    TEXT_BOX_2,
    TEXT_BOX_3,
    TEXT_BOX_4,
    TEXT_BOX_5,
    TEXT_BOX_6,
    TEXT_BOX_7,
    TEXT_BOX_8,
    TEXT_BOX_9,
    TEXT_BOX_10,
    TEXT_BOX_11,
    TEXT_BOX_12;

    private BikeField[] fields;

    ScreenBox(BikeField... fields) {
        this.fields = fields;
    }

    public BikeField[] getFields() {
        return fields.clone();
    }
}
