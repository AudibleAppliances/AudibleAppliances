package uk.ac.cam.groupprojects.bravo.model.numbers.digits;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

/**
 * Created by david on 08/02/2018.
 */
public class LowerDistanceDigit extends ScreenNumber {
    public LowerDistanceDigit() {
        super(0, 99);
    }

    @Override
    public String formatSpeech() {
        return null;
    }
}