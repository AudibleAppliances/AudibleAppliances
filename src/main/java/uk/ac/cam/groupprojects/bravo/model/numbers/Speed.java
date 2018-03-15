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
    public String formatSpeech() {
        int lower = lowerSpeedDigit.getValue();
        String lowerStr;
        if (lower > 10) {
            lowerStr = "0"+Integer.toString(lower);
        }
        else {
            lowerStr = Integer.toString(lower);
        }

        return String.format( "You are currently cycling at %d.%s miles per hour", higherSpeedDigit.getValue(), lowerStr);
    }

    @Override
    public int getValue(){
        return this.higherSpeedDigit.getValue() * 100 + this.lowerSpeedDigit.getValue();
    }
}