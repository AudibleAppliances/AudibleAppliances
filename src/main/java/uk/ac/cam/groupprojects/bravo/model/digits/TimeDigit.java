package uk.ac.cam.groupprojects.bravo.model.digits;

import uk.ac.cam.groupprojects.bravo.model.ScreenNumber;

public class TimeDigit extends ScreenNumber {

    public TimeDigit() {
        super(0, 59);
    }

    @Override
    public String speakValue() {
        return null;
    }

}
