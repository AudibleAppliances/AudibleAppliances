package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

public class HigherDistanceDigit extends ScreenNumber{
    public HigherDistanceDigit() {
        super(0, 99);
    }

    @Override
    public String speakValue() {
        return null;
    }
}
