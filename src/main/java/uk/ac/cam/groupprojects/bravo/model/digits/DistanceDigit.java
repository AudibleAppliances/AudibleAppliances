package uk.ac.cam.groupprojects.bravo.model.digits;

import uk.ac.cam.groupprojects.bravo.model.ScreenNumber;

public class DistanceDigit extends ScreenNumber{
    public DistanceDigit() {
        super(0, 100);
    }

    @Override
    public String speakValue() {
        return null;
    }
}
