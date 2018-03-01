package uk.ac.cam.groupprojects.bravo.model.numbers;

import uk.ac.cam.groupprojects.bravo.model.numbers.digits.HigherTimeDigit;
import uk.ac.cam.groupprojects.bravo.model.numbers.digits.LowerTimeDigit;

/**
 * Created by david on 06/02/2018.
 */
public class Time extends ScreenNumber {

    private ScreenNumber minutes;
    private ScreenNumber seconds;

    public Time() {
        super(0, 0);
        minutes = new HigherTimeDigit();
        seconds = new LowerTimeDigit();
    }

    public boolean setValue( int value ){
        return value > 0 && setValue( value / 60, value % 60 );
    }

    public boolean setValue( int minutes, int seconds ){
        return minutes < 10 //Need extra restrictions
                && this.minutes.setValue( minutes )
                && this.seconds.setValue( seconds );
    }

    @Override
    public String speakValue() {
        return String.format("The current time elapsed is %d minutes and %d seconds", minutes.getValue(), seconds.getValue() );
    }
}
