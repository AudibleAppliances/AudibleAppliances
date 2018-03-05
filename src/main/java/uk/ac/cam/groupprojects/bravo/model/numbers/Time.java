package uk.ac.cam.groupprojects.bravo.model.numbers;

import uk.ac.cam.groupprojects.bravo.model.numbers.digits.HigherTimeDigit;
import uk.ac.cam.groupprojects.bravo.model.numbers.digits.LowerTimeDigit;

/**
 * Created by david on 06/02/2018.
 */
public class Time extends ScreenNumber {

    private int oldVal;
    private boolean oldResult;

    private ScreenNumber minutes;
    private ScreenNumber seconds;

    public Time() {
        super(0, 0);
        minutes = new HigherTimeDigit();
        seconds = new LowerTimeDigit();
        oldVal = -1;
        oldResult = false;
    }

    public boolean setValue( int value ) {
        if (value > 0 && value < 60) {
            return setValue(0, value);
        } else {
            return value > 0 && setValue(value / 100, value % 100);
        }
    }

    public boolean setValue( int minutes, int seconds ){
        oldVal = this.minutes.getValue() * 60 + this.seconds.getValue();
        return this.minutes.setValue( minutes )
                && this.seconds.setValue( seconds );
    }

    @Override
    public String speakValue() {
        boolean result = oldResult;
        if ( oldVal != -1 ){
            int newVal = this.minutes.getValue() * 60 + this.seconds.getValue();
            /*
                If we can tell if one is larger than the other, we will use this
                If not then we'll use the old result.
             */
            if ( newVal != 0 && oldVal != 0 && newVal > oldVal ){
                result = true;
            }else if ( newVal != 0 && oldVal != 0 && oldVal < newVal ){
                result = false;
            }
        }

        String toReturn;

        if ( result ){
            toReturn = String.format("The current time elapsed is %d minutes and %d seconds", minutes.getValue(), seconds.getValue() );
        }else {
            toReturn = String.format("The current time elapsed is %d minutes and %d seconds", minutes.getValue(), seconds.getValue() );
        }
        oldResult = result;

        return toReturn;
    }
}
