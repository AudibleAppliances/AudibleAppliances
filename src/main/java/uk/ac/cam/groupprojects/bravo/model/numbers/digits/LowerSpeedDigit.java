package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

/**
 * Created by david on 08/02/2018.
 */
public class LowerSpeedDigit extends ScreenNumber {

    public LowerSpeedDigit() {
        super(0, 9);
    }

    @Override
    public String speakValue() {
        return null;
    }
}
