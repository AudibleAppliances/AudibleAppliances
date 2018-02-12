package uk.ac.cam.groupprojects.bravo.model.numbers;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.model.numbers.RPM;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 08/02/2018.
 */
public class RPMTest {

    @Test
    public void testBounds(){
        RPM rpm = new RPM();
        assertEquals( false, rpm.setValue(-5) );
        assertEquals( false, rpm.setValue(4000) );
        assertEquals( true, rpm.setValue(50) );
    }

    @Test
    public void testOutput(){
        RPM rpm = new RPM();
        rpm.setValue(50);
        assertEquals( "Your current rotations per minute is 50", rpm.speakValue() );

        rpm.setValue(40);
        assertEquals( "Your current rotations per minute is 40", rpm.speakValue() );

        rpm.setValue(-40);
        assertEquals( "Your current rotations per minute is 40", rpm.speakValue() );
    }

}
