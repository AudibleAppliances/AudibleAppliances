package uk.ac.cam.groupprojects.bravo.tts;

/*
    Thrown if the "festival" program can't be run.
    Fatal error - means that the system this is being run on hasn't been properly configured.
*/
public class FestivalMissingException extends Exception {
    public static final long serialVersionUID = 1L;

    public FestivalMissingException(Exception inner) {
        super(inner);
    }
}