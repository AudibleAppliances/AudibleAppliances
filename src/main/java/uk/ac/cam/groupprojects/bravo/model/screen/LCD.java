package uk.ac.cam.groupprojects.bravo.model.screen;

/**
 * Created by david on 12/02/2018.
 */
public class LCD {

    /*
        Used to store the state of the screen
        Stores which bits are turned on
        Each is a new column
     */
    private boolean[][] screen;

    public LCD(){
        screen = new boolean[10][8];
    }

    public LCD( boolean[][] newScreen ){
        this();
        updateScreen( newScreen );
    }

    /**
     * Validates the screen being passed in using the validate function
     * Then sets the screen variable to the passed in value
     * @param newScreen the latest update of the screen
     * @return If it was correct set (passed the validation tests)
     */
    public boolean updateScreen( boolean[][] newScreen ){
        if ( validate( newScreen ) ){
            screen = newScreen;
            return true;
        }else {
            return false;
        }
    }

    /**
     * Given a string (similar to one returned in returnString() )
     * Then it will validate and check whether it's in the correct format
     *
     * @param string
     * @return
     */
    public static boolean validateString( String string ){
        String[] parts = string.split(" ");
        if ( parts.length != 10 )
            return false;
        for ( String s : parts ){
            if ( !s.matches("^[01]{8}$") ){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * This function will validate any screen array that it is given.
     * It makes sure that that the outer array is 10 wide
     * And every inner array is 8 wide
     *
     * @param newScreen The array to validate
     * @return Whether the array passed the validation tests
     */
    private boolean validate( boolean[][] newScreen ){
        if ( newScreen.length != 10 )
            return false;

        for (boolean[] aNewScreen : newScreen) {
            if ( aNewScreen.length != 8 )
                return false;
        }

        return true;
    }

    /**
     * Returns the LCD screen encoded as a string
     * Will return in a form of 8 0s and 1s all joined by a space
     * @return
     */
    public String returnString(){
        String code = "";
        for ( int i = 0; i < screen.length; i++ ){
            for ( int j = 0; j < screen[i].length; j++ ){
                code += ( screen[i][j] ) ? "1" : "0";
            }
            if ( i != ( screen.length - 1 ) )
                code += " ";
        }
        return code;
    }

    /**
     *
     * Will attempt to use the current state to match it up with one of the
     * pre-defined formats in LCDFunction.
     *
     * Uses a linear search, should potentially maybe use a binary tree or
     * some sort of optimised searching algorithm
     * 
     * Custom algorithm could entail counting total number of dots lit up
     * ie some kind of radix/bucket search
     *
     * @return the LCDFunction
     */
    public LCDFunction matchSettings(){
        String toTest = returnString();
        for ( LCDFunction function: LCDFunction.values() ){
            for ( String pattern : function.getValues() ){
                if ( pattern.compareTo( toTest ) == 0 ){
                    return function;
                }
            }
        }
        return LCDFunction.NOT_DEF;
    }

    /**
     * Will use the audible name to create a string to speak out
     *
     * @return
     */
    public String speakSetting(){
        return "You are currently on the " +
                matchSettings().getAudibleName() + " mode";
    }
    
    public boolean get(int x, int y) {
    	
    	return screen[Math.min(9, Math.max(0, x))][Math.min(9, Math.max(0, y))];
    	
    }

}
