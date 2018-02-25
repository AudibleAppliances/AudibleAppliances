package uk.ac.cam.groupprojects.bravo.model.screen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 12/02/2018.
 */
public class LCDFunctionTest {

    @Test
    public void validateLCDFunctions(){

        /*
            These tests are for identifying which function failed
         */

        int noFunctionsTested = 0;

        noFunctionsTested++;
        for ( String pattern: LCDFunction.MANUAL.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.STEPS.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.HILL.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.ROLLING.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.VALLEY.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.FAT_BURN.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.RAMP.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.MOUNTAIN.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.INTERVALS.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        // Increment for random, as no relevant tests to run for it
        noFunctionsTested++;

        noFunctionsTested++;
        for ( String pattern: LCDFunction.PLATEAU.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.FARTLEK.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.PRECIPCE.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.USER_1.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.USER_2.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.USER_3.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction.USER_4.getValues() ){
            assertEquals( true, LCD.validateString( pattern ) );
        }

        noFunctionsTested++; //Added for NO_DEF
        /*
            In case a new function is added but not added above
         */
        assertEquals( noFunctionsTested, LCDFunction.values().length );


    }

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
