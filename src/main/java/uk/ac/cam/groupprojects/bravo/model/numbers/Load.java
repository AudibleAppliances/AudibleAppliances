package uk.ac.cam.groupprojects.bravo.model.numbers;

/**
 * Created by david on 01/03/2018.
 */
public class Load extends ScreenNumber {

    public Load(){
        super(1, 16);
    }

    @Override
    public String speakValue() {
        return String.format( "Your current load is %d", getValue() );
    }
}
