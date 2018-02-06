package uk.ac.cam.groupprojects.bravo.model;

import uk.ac.cam.groupprojects.bravo.model.digits.TimeDigit;

/**
 * Created by david on 06/02/2018.
 */
public class Time extends ScreenNumber {

    private ScreenNumber minutes;
    private ScreenNumber seconds;

    public Time() {
        super(0, 0);
        minutes = new TimeDigit();
        seconds = new TimeDigit();
    }

    public boolean setValue( int value ){
        return this.setValue( value / 60, value % 60 );
    }

    public boolean setValue( int minutes, int seconds ){
        return this.minutes.setValue( minutes ) && this.seconds.setValue( seconds );
    }

    @Override
    public String speakValue() {
        return String.format("The current time elapsed is %d minutes and %d seconds", minutes.getValue(), seconds.getValue() );
    }
}