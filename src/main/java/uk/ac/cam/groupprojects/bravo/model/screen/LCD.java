package uk.ac.cam.groupprojects.bravo.model.screen;

/**
 * Created by david on 12/02/2018.
 */
public class LCD {

    /*
        Used to store the state of the screen
        Stores the height of the bar (0 - 7)
        Each is a new column
     */
    private int[] screen;

    public LCD(){
        screen = new int[10];
    }

    public LCD( int[] newScreen ){
        this();
        updateScreen( newScreen );
    }

    public boolean updateScreen( int[] newScreen ){
        if ( validate( newScreen ) ){
            screen = newScreen;
            return true;
        }else {
            return false;
        }
    }

    private boolean validate( int[] newScreen ){
        if ( newScreen.length != 10 )
            return false;

        for (int aNewScreen : newScreen) {
            if (aNewScreen < 0 || aNewScreen > 7) {
                return false;
            }
        }

        return true;
    }

}
