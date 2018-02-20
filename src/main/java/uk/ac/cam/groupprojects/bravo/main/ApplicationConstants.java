package uk.ac.cam.groupprojects.bravo.main;

/**
 * Created by david on 13/02/2018.
 */
public class ApplicationConstants {

    public static final String VERSION_NO = "0.1";
    public static final String PATH_TO_CONFIG = "/home/pi/config.json";

    public static final boolean DEBUG = false;


    //The probability that we are in a state, we need it to be at least this
    public static final float MIN_PROB = 0.7f;

    //How many tries we'll take to try and establish the max initial tries
    public static final int MAX_INITIAL_TRIES = 10;


    public static final int INITIAL_FREQ = 1000;

    public static final int UPDATE_FREQ = 1000;
    public static final int SPEAK_FREQ = 10000;

}
