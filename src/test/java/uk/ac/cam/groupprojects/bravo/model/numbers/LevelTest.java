package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Level;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 06/02/2018.
 */
public class LevelTest {

    @Test
    public void testBounds(){
        Level level = new Level();
        assertEquals( false, level.setValue(-5) );
        assertEquals( false, level.setValue(100) );
        assertEquals( true, level.setValue(50) );
    }

    @Test
    public void testOutput(){
        Level level = new Level();
        level.setValue(50);
        assertEquals( "Your current level is 50", level.speakValue() );

        level.setValue(40);
        assertEquals( "Your current level is 40", level.speakValue() );

        level.setValue(-40);
        assertEquals( "Your current level is 40", level.speakValue() );
    }

}
