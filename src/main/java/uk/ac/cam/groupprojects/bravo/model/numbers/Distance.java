package uk.ac.cam.groupprojects.bravo.model.numbers;

import uk.ac.cam.groupprojects.bravo.model.numbers.digits.HigherDistanceDigit;
import uk.ac.cam.groupprojects.bravo.model.numbers.digits.LowerDistanceDigit;

public class Distance extends ScreenNumber {

    private ScreenNumber km;

    //We actually store what we read, so this is in 100m increments
    private ScreenNumber m;

    public Distance() {
        super(0, 0);
        km = new HigherDistanceDigit();
        m = new LowerDistanceDigit();
    }

    public boolean setValue( int value ){
        return value > 0 && setValue( value / 100, value % 100 );
    }

    public boolean setValue( int km, int m ){
        return
                this.km.setValue( km )
                && this.m.setValue( m );
    }

    @Override
    public String formatSpeech() {
        int lower = m.getValue();
        String lowerStr;
        if (lower > 10) {
            lowerStr = "0"+Integer.toString(lower);
        }
        else {
            lowerStr = Integer.toString(lower);
        }
        return String.format("You've cycled %d.%s miles", km.getValue(), lowerStr);
    }

    @Override
    public int getValue(){
        return this.km.getValue() * 100 + this.m.getValue();
    }

}
