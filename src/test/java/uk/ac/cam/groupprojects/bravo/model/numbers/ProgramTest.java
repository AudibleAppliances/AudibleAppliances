package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;

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
        assertEquals( "You are selecting program 10", program.formatSpeech() );

        program.setValue(12);
        assertEquals( "You are selecting program 12", program.formatSpeech() );

        program.setValue(-40);
        assertEquals( "You are selecting program 12", program.formatSpeech() );
    }

}
