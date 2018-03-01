package uk.ac.cam.groupprojects.bravo.tts;

/*
    Thrown if the voice list returned from festival is in an invalid format
*/
public class InvalidVoiceListException extends Exception {
    public static final long serialVersionUID = 1L;

    public InvalidVoiceListException(String message) {
        super(message);
    }
}