package uk.ac.cam.groupprojects.bravo.model.digits;

import uk.ac.cam.groupprojects.bravo.model.ScreenNumber;

/**
 * Created by david on 08/02/2018.
 */
public class LowerDistanceDigit extends ScreenNumber {
    public LowerDistanceDigit() {
        super(0, 9);
    }

    @Override
    public String speakValue() {
        return null;
    }
}