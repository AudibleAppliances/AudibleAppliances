package uk.ac.cam.groupprojects.bravo.tts;

/*
    Thrown if the voice list returned from festival is in an invalid format
*/
public class InvalidVoiceListException extends Exception {
    public InvalidVoiceListException(String message) {
        super(message);
    }
}