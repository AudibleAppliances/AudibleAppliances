package uk.ac.cam.groupprojects.bravo.model.numbers;

import uk.ac.cam.groupprojects.bravo.model.numbers.digits.HigherSpeedDigit;
import uk.ac.cam.groupprojects.bravo.model.numbers.digits.LowerSpeedDigit;

public class Speed extends ScreenNumber {

    private HigherSpeedDigit higherSpeedDigit;
    private LowerSpeedDigit lowerSpeedDigit;

    public Speed() {
        super(0, 100);

        higherSpeedDigit = new HigherSpeedDigit();
        lowerSpeedDigit = new LowerSpeedDigit();
    }

    public boolean setValue( int value ){
        return value > 0 && this.setValue( value / 100, value % 100 );
    }

    public boolean setValue( int val1, int val2 ){
        return higherSpeedDigit.setValue( val1 ) && lowerSpeedDigit.setValue( val2 );
    }

    @Override
    public String speakValue() {
        return String.format( "You are currently cycling at %d.%d miles per hour", higherSpeedDigit.getValue(), lowerSpeedDigit.getValue() );
    }
}