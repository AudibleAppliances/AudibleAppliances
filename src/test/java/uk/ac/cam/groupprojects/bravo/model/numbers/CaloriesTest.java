package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Calories;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 06/02/2018.
 */
public class CaloriesTest {

    @Test
    public void testBounds(){
        Calories cal = new Calories();
        assertEquals( false, cal.setValue(-5) );
        assertEquals( false, cal.setValue(4000) );
        assertEquals( true, cal.setValue(50) );
    }

    @Test
    public void testOutput(){
        Calories cal = new Calories();
        cal.setValue(50);
        assertEquals( "You have burnt 50 calories in this session", cal.speakValue() );

        cal.setValue(40);
        assertEquals( "You have burnt 40 calories in this session", cal.speakValue() );

        cal.setValue(-40);
        assertEquals( "You have burnt 40 calories in this session", cal.speakValue() );
    }

}
