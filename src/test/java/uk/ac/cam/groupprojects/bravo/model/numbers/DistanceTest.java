package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Distance;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 08/02/2018.
 */
public class DistanceTest {

    @Test
    public void testBounds(){
        Distance distance = new Distance();
        assertEquals( false, distance.setValue(10, 30) );
        assertEquals( false, distance.setValue(2, -50) );
        assertEquals( true, distance.setValue(40, 5) );
    }

    @Test
    public void testOutput(){
        Distance distance = new Distance();
        distance.setValue(50, 4);
        assertEquals( "You have currently travelled 50.4 kilometres", distance.speakValue() );

        distance.setValue(10, 3);
        assertEquals( "You have currently travelled 10.3 kilometres", distance.speakValue() );

        distance.setValue(-40);
        assertEquals( "You have currently travelled 10.3 kilometres", distance.speakValue() );
    }

}
