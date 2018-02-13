package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

public class TimeDigit extends ScreenNumber {

    public TimeDigit() {
        super(0, 59);
    }

    @Override
    public String speakValue() {
        return null;
    }

}
