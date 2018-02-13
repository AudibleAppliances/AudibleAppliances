package uk.ac.cam.groupprojects.bravo.model.numbers;

/**
 * Created by david on 06/02/2018.
 */
public class Level extends ScreenNumber {

    public Level() {
        super(0, 99);
    }

    @Override
    public String speakValue() {
        return String.format( "Your current level is %d", getValue() );
    }
}
