package uk.ac.cam.groupprojects.bravo.tts;

/*
    Thrown if the festival program produces invalid output when the rate's being changed.
*/
public class RateSetException extends Exception {
    public static final long serialVersionUID = 1L;

    public RateSetException(String message) {
        super(message);
    }
}