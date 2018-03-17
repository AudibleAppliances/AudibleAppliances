package uk.ac.cam.groupprojects.bravo.main;

import java.io.File;

/**
 * Created by david on 13/02/2018.
 */
public class ApplicationConstants {

    public static final String VERSION_NO = "0.3";
    public static final String PATH_TO_CONFIG = "/home/pi/config.json";

    // Directory mounted in RAM for fast access - used by all the application temp files
    public static final String TMP_DIR_PATH = "/mnt/rd";
    public static final File TMP_DIR = new File(TMP_DIR_PATH);
    public static final String IMAGE_PATH = TMP_DIR_PATH + "/image.jpg";

    public static final int DAEMON_PORT = 40000;

    public static boolean DEBUG;

    public static final int MAX_CONNECT_ATTEMPTS = 5;

    public static final int BLINK_FREQ_MILLIS = 500; // Blink frequency in ms

    public static final int DEFAULT_SPEAK_FREQ = 10000;
    public static final int RUNNING_SPEAK_FREQ = 60000;

    public static final int DEFAULT_SPEECH_PAUSE = 750;

    public static final double LCD1_SCALE = 0.5;
}
