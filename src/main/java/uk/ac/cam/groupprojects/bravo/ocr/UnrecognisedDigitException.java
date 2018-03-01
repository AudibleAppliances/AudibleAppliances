package uk.ac.cam.groupprojects.bravo.ocr;

// Thrown if a digit in the input image can't be identified
public class UnrecognisedDigitException extends Exception {
    public static final long serialVersionUID = 1L;

    public UnrecognisedDigitException(String message) {
        super(message);
    }
}