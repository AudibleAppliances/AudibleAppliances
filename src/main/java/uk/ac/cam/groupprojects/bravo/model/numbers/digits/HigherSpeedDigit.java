package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

/**
 * Created by david on 08/02/2018.
 */
public class HigherSpeedDigit extends ScreenNumber {

    public HigherSpeedDigit() {
        super(0, 99);
    }

    @Override
    public String speakValue() {
        return null;
    }
}
