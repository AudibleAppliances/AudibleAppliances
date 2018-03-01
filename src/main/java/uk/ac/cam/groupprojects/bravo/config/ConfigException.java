package uk.ac.cam.groupprojects.bravo.config;

/**
 * Holds a message of what has gone wrong during the reading or loading of the config file
 *
 * @author Oliver Hope
 */
public class ConfigException extends Exception {
    public ConfigException(String msg) {
        super(msg);
    }
}
