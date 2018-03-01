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
        for ( String pattern: LCDFunction_.MANUAL.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.STEPS.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.HILL.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.ROLLING.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.VALLEY.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.FAT_BURN.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.RAMP.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.MOUNTAIN.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.INTERVALS.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.PLATEAU.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.FARTLEK.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.PRECIPCE.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.USER_1.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.USER_2.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.USER_3.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++;
        for ( String pattern: LCDFunction_.USER_4.getValues() ){
            assertEquals( true, LCD_.validateString( pattern ) );
        }

        noFunctionsTested++; //Added for NO_DEF
        /*
            In case a new function is added but not added above
         */
        assertEquals( noFunctionsTested, LCDFunction_.values().length );


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


        LCD_ lcd1 = new LCD_();
        assertEquals( true, lcd1.updateScreen( testScreen ) );
        assertEquals( LCDFunction_.MANUAL, lcd1.matchSettings() );

        assertEquals( true, lcd1.updateScreen( testScreen1 ) );
        assertEquals( LCDFunction_.MANUAL, lcd1.matchSettings() );
    }
}
