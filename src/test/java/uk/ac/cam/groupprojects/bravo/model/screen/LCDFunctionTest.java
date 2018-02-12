package uk.ac.cam.groupprojects.bravo.model.screen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 12/02/2018.
 */
public class LCDFunctionTest {

    @Test
    public void matchManual(){
        boolean[][] testScreen = new boolean[10][8];
        testScreen[0] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[1] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[2] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[3] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[4] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[5] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[6] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[7] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[8] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[9] = new boolean[]{ true, true, true, false, false, false, false, false };

        boolean[][] testScreen1 = new boolean[10][8];
        testScreen1[0] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[1] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[2] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[3] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[4] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[5] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[6] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[7] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[8] = new boolean[]{ true, true, true, true, false, false, false, false };
        testScreen1[9] = new boolean[]{ true, true, true, true, false, false, false, false };


        LCD lcd1 = new LCD();
        assertEquals( true, lcd1.updateScreen( testScreen ) );
        assertEquals( LCDFunction.MANUAL, lcd1.matchSettings() );

        assertEquals( true, lcd1.updateScreen( testScreen1 ) );
        assertEquals( LCDFunction.MANUAL, lcd1.matchSettings() );
    }
}
