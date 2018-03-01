package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 20/02/2018.
 */
public class WattTest {

    @Test
    public void testBounds(){
        Watt watt = new Watt();
        assertEquals( false, watt.setValue(-5) );
        assertEquals( false, watt.setValue(4000) );
        assertEquals( true, watt.setValue(50) );
    }

    @Test
    public void testOutput(){
        Watt watt = new Watt();
        watt.setValue(50);
        assertEquals( "Your power output is currently 50 watts", watt.speakValue() );

        watt.setValue(40);
        assertEquals( "Your power output is currently 40 watts", watt.speakValue() );

        watt.setValue(-40);
        assertEquals( "Your power output is currently 40 watts", watt.speakValue() );
    }

}
