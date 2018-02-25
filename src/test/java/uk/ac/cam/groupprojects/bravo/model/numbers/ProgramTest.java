package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.Program;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 06/02/2018.
 */
public class ProgramTest {

    @Test
    public void testBounds(){
        Program program = new Program();
        assertEquals( false, program.setValue(-5) );
        assertEquals( false, program.setValue(60) );
        assertEquals( true, program.setValue(10) );
    }

    @Test
    public void testOutput(){
        Program program = new Program();
        program.setValue(10);
        assertEquals( "You have selected program 10", program.speakValue() );

        program.setValue(12);
        assertEquals( "You have selected program 12", program.speakValue() );

        program.setValue(-40);
        assertEquals( "You have selected program 12", program.speakValue() );
    }

}
