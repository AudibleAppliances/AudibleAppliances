package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 06/02/2018.
 */
public class LoadTest {

    @Test
    public void testBounds(){
        Load load = new Load();
        assertEquals( false, load.setValue(-5) );
        assertEquals( false, load.setValue(100) );
        assertEquals( true, load.setValue(16) );
    }

    @Test
    public void testOutput(){
        Load load = new Load();
        load.setValue(16);
        assertEquals( "Your current load is 16", load.speakValue() );

        load.setValue(9);
        assertEquals( "Your current load is 9", load.speakValue() );

        load.setValue(-40);
        assertEquals( "Your current load is 9", load.speakValue() );
    }

}
