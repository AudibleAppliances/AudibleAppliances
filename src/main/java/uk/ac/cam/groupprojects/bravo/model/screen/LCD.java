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

    public boolean updateScreen( boolean[][] newScreen ){
        if ( validate( newScreen ) ){
            screen = newScreen;
            return true;
        }else {
            return false;
        }
    }

    private boolean validate( boolean[][] newScreen ){
        if ( newScreen.length != 10 )
            return false;

        for (boolean[] aNewScreen : newScreen) {
            if ( aNewScreen.length != 8 )
                return false;
        }

        return true;
    }

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

}
