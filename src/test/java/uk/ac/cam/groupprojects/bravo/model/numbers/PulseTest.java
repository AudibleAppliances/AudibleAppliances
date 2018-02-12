package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.Pulse;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 08/02/2018.
 */
public class PulseTest {

    @Test
    public void testBounds(){
        Pulse pulse = new Pulse();
        assertEquals( false, pulse.setValue(-5) );
        assertEquals( false, pulse.setValue(400) );
        assertEquals( true, pulse.setValue(50) );
    }

    @Test
    public void testOutput(){
        Pulse pulse = new Pulse();
        pulse.setValue(50);
        assertEquals( "Your current heart rate is 50 beats per minute", pulse.speakValue() );

        pulse.setValue(40);
        assertEquals( "Your current heart rate is 40 beats per minute", pulse.speakValue() );

        pulse.setValue(-40);
        assertEquals( "Your current heart rate is 40 beats per minute", pulse.speakValue() );
    }

}
