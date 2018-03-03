package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Speed;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 08/02/2018.
 */
public class SpeedTest {

    @Test
    public void testBounds(){
        Speed speed = new Speed();
        assertEquals( false, speed.setValue(10, 30) );
        assertEquals( false, speed.setValue(2, -50) );
        assertEquals( true, speed.setValue(40, 5) );
    }

    @Test
    public void testOutput(){
        Speed speed = new Speed();
        speed.setValue(50, 4);
        assertEquals( "You are currently cycling at 50.4 miles per hour", speed.speakValue() );

        speed.setValue(10, 3);
        assertEquals( "You are currently cycling at 10.3 miles per hour", speed.speakValue() );

        speed.setValue(-40);
        assertEquals( "You are currently cycling at 10.3 miles per hour", speed.speakValue() );
    }

}
