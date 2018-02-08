package uk.ac.cam.groupprojects.bravo.model.digits;

import uk.ac.cam.groupprojects.bravo.model.ScreenNumber;

public class HigherDistanceDigit extends ScreenNumber{
    public HigherDistanceDigit() {
        super(0, 99);
    }

    @Override
    public String speakValue() {
        return null;
    }
}
