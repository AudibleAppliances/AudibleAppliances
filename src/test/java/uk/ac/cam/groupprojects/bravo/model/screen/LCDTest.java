package uk.ac.cam.groupprojects.bravo.model.screen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 12/02/2018.
 */
public class LCDTest {

    @Test
    public void testValidation(){

        boolean[][] testScreen = new boolean[7][8];
        testScreen[0] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[1] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[2] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[3] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[4] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[5] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen[6] = new boolean[]{ true, true, true, false, false, false, false, false };

        boolean[][] testScreen1 = new boolean[10][8];
        testScreen1[0] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[1] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[2] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[3] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[4] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[5] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[6] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[7] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[8] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[9] = new boolean[]{ true, true, true, false, false, false, false };

        boolean[][] testScreen2 = new boolean[10][8];
        testScreen2[0] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[1] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[2] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[3] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[4] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[5] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[6] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[7] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[8] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen2[9] = new boolean[]{ true, true, true, false, false, false, false, false };

        LCD lcd1 = new LCD();

        assertEquals( false, lcd1.updateScreen( testScreen ) );
        assertEquals( false, lcd1.updateScreen( testScreen1 ) );
        assertEquals( true, lcd1.updateScreen( testScreen2 ) );
    }

    @Test
    public void testStringValidation(){
        assertEquals( true,
                LCD.validateString("10000000 10000000 10000000 10000000 " +
                        "10000000 10000000 10000000 10000000 10000000 10000000") );
        assertEquals( false,
                LCD.validateString("10000000 10000000 10000000 10000000 " +
                        "20000000 10000000 10000000 10000000 10000000 10000000") );

        assertEquals( false,
                LCD.validateString("1000000 10000000 10000000 10000000 " +
                        "10000000 10000000 10000000 10000000 10000000 10000000") );
    }

    @Test
    public void testStrings(){
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
        testScreen1[0] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[1] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[2] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[3] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[4] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[5] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[6] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[7] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[8] = new boolean[]{ true, true, true, false, false, false, false, false };
        testScreen1[9] = new boolean[]{ true, true, true, false, false, false, false };

        LCD lcd1 = new LCD();
        assertEquals( "00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", lcd1.returnString() );
        assertEquals( true, lcd1.updateScreen( testScreen ) );
        assertEquals( "11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000", lcd1.returnString() );
        assertEquals( false, lcd1.updateScreen( testScreen1 ) );
        assertEquals( "11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000 11100000", lcd1.returnString() );

    }

}
