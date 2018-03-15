package uk.ac.cam.groupprojects.bravo.model.numbers;

/**
 * Created by david on 20/02/2018.
 */
public class Program extends ScreenNumber {

    public Program() {
        super(0, 15);
    }

    @Override
    public String formatSpeech() {
        return String.format("Press enter to select program %d, or rotate to choose a different program.", getValue());
    }
}
