package uk.ac.cam.groupprojects.bravo.tts;

/*
    Thrown if changing the voice on a Synthesiser failed.
    Non-fatal error - we can always fall back to another voice or a default voice.
*/
public class VoiceMissingException extends Exception {
    public static final long serialVersionUID = 1L;

    public VoiceMissingException() { }

    public VoiceMissingException(String message) {
        super(message);
    }
}