package uk.ac.cam.groupprojects.bravo.model;

public class Speed extends ScreenNumber {

    public Speed() {
        super(0, 100);
    }

    @Override
    public String speakValue() {
        return String.format( "You are currently cycling at %.2f kilometres per hour", getValue() );
    }
}
