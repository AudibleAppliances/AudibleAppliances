package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

/**
 * Created by david on 01/03/2018.
 */
public class LowerTimeDigit extends ScreenNumber {

    public LowerTimeDigit() {
        super(0, 59);
    }

    @Override
    public String speakValue() {
        return null;
    }

}
