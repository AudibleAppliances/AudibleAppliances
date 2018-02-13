package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Time;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 08/02/2018.
 */
public class TimeTest {

    @Test
    public void testBounds(){
        Time time = new Time();
        assertEquals( false, time.setValue(10, 30) );
        assertEquals( false, time.setValue(2, -50) );
        assertEquals( true, time.setValue(4, 50) );
    }

    @Test
    public void testOutput(){
        Time time = new Time();
        time.setValue(50);
        assertEquals( "The current time elapsed is 0 minutes and 50 seconds", time.speakValue() );

        time.setValue(1, 30);
        assertEquals( "The current time elapsed is 1 minutes and 30 seconds", time.speakValue() );

        time.setValue(-40);
        assertEquals( "The current time elapsed is 1 minutes and 30 seconds", time.speakValue() );
    }

}
