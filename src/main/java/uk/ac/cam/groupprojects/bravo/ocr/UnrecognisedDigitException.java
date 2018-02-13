package uk.ac.cam.groupprojects.bravo.ocr;

// Thrown if a digit in the input image can't be identified
public class UnrecognisedDigitException extends Exception {
    public UnrecognisedDigitException(String message) {
        super(message);
    }
}