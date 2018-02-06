package uk.ac.cam.groupprojects.bravo.model;

import uk.ac.cam.groupprojects.bravo.model.digits.DistanceDigit;

public class Distance extends ScreenNumber {

    private ScreenNumber km;
    private ScreenNumber m;

    public Distance() {
        super(0, 0);
        km = new DistanceDigit();
        m = new DistanceDigit();
    }

    public boolean setDistance( int km, int m ){
        return this.km.setValue( km ) && this.m.setValue( m );
    }

    @Override
    public String speakValue() {
        return String.format("You have currently travelled %d kilometres and %d metres", km.getValue(), m.getValue() );
    }
}
