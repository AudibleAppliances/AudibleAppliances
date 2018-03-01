package uk.ac.cam.groupprojects.bravo.main;

import java.io.File;

/**
 * Created by david on 13/02/2018.
 */
public class ApplicationConstants {

    public static final String VERSION_NO = "0.1";
    public static final String PATH_TO_CONFIG = "/home/pi/config.json";

    // Directory mounted in RAM for fast access - used by all the application temp files
    public static final String TMP_DIR_PATH = "/mnt/rd";
    public static final File TMP_DIR = new File(TMP_DIR_PATH);
    public static final String IMAGE_PATH = TMP_DIR_PATH + "/image.jpg";

    public static final int DAEMON_PORT = 40000;

    public static boolean DEBUG;


    //The probability that we are in a state, we need it to be at least this
    public static final float MIN_PROB = 0.7f;

    //How many tries we'll take to try and establish the max initial tries
    public static final int MAX_INITIAL_TRIES = 10;


    public static final int INITIAL_FREQ = 1000;

    public static final int UPDATE_FREQ = 1000;
    public static final int SPEAK_FREQ = 10000;

}
