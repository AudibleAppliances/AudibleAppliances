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
        return String.format("Press start to select program %d, or rotate to choose a different program.", getValue());
    }
}
