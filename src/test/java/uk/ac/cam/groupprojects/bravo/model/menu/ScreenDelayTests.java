package uk.ac.cam.groupprojects.bravo.model.menu;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.main.AudibleAppliances;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by david on 01/03/2018.
 */
public class ScreenDelayTests {

    @Test
    public void testScreenDelays(){

        Map<ScreenEnum, BikeScreen> screens = new HashMap<>();
        AudibleAppliances.addScreens( screens );

        for ( BikeScreen screen: screens.values() ){
            assertTrue("Checking that " + screen.getEnum().toString() + " has a delay larger than 0", screen.getSpeakDelay() > 0 );
        }

    }

}
