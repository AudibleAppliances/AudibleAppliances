package uk.ac.cam.groupprojects.bravo.imageProcessing;

/**
 * Exception for if camera is disconnected or errors in some other way
 *
 * @author Oliver Hope
 */
public class CameraException extends Exception {
    public CameraException(String msg) {
        super(msg);
    }
}
