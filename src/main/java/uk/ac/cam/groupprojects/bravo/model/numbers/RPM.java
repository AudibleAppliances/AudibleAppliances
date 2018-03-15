package uk.ac.cam.groupprojects.bravo.model.numbers;


public class RPM extends ScreenNumber {

    public RPM() {
        super(0, 999);
    }

    @Override
    public String formatSpeech() {
        return String.format("Your current R P M is %d", getValue() );
    }
}
