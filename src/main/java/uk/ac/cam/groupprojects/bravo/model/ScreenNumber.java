package uk.ac.cam.groupprojects.bravo.model;

/**
 * Created by david on 06/02/2018.
 */
public abstract class ScreenNumber {

    private final int minBound;
    private final int maxBound;

    private double val;

    public ScreenNumber( int minBound, int maxBound ){
        this.minBound = minBound;
        this.maxBound = maxBound;
        this.val = 0;
    }

    /**
     * This functions checks the bounds of the value before setting it.
     *
     * @param newVal
     * @return if the value has been set successfully
     */
    public boolean setValue( double newVal ){
        if ( minBound >= newVal && newVal <= maxBound ){
            this.val = newVal;
            return true;
        }
        return false;
    }

    /**
     * Returns the value
     * @return
     */
    public double getValue(){
        return val;
    }

    /**
     * Gives a string that can be spoken out loud
     * @return
     */
    public abstract String speakValue();

}
