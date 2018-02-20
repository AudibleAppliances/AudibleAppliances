package uk.ac.cam.groupprojects.bravo.model.numbers;

/**
 * Created by david on 20/02/2018.
 */
public class Watt extends ScreenNumber {

    public Watt(){
        super(0,99);
    }

    @Override
    public String speakValue() {
        return String.format("Your power output is currently %d watts", getValue() );
    }
}
