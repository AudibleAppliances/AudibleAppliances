package uk.ac.cam.groupprojects.bravo.model.menu;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.main.AudibleAppliances;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by david on 01/03/2018.
 */
public class CheckScreenTest {

    /*
        This test just checks that all of the enums have screens
     */
    @Test
    public void checkScreens(){

        Map<ScreenEnum, BikeScreen> screens = new HashMap<>();
        AudibleAppliances.addScreens( screens );

        for ( ScreenEnum screenEnum: ScreenEnum.values() ){
            assertTrue("Checking that " + screenEnum.toString() + " is included in AudibleAppliances", screens.containsKey( screenEnum ));
        }


    }

}
