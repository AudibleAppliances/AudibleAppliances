package uk.ac.cam.groupprojects.bravo.model.numbers;

/**
 * Created by david on 20/02/2018.
 */
public class Program extends ScreenNumber {

    public Program() {
        // ScreenNumber imposes a range, but to correct some glitches in the OCR we need access
        // to the original value. Dirty hack - remove the bound
        super(0, Integer.MAX_VALUE);
    }

    @Override
    public String formatSpeech() {
        // This shouldn't really be called - take a look at ProgramScreen's subclasses
        return String.format("Program %d.", getValue());
    }
}
