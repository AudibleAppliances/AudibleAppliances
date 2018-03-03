package uk.ac.cam.groupprojects.bravo.model;

import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;

/**
 * Created by david on 20/02/2018.
 */
public class Program extends ScreenNumber {

    public Program() {
        super(0, 15);
    }


    @Override
    public String speakValue() {
        return String.format("You have selected program %d", getValue() );
    }
}
