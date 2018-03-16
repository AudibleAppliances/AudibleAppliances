package uk.ac.cam.groupprojects.bravo.model.numbers;

public class Calories extends ScreenNumber{

    public Calories() {
        super(0, 999);
    }

    @Override
    public String formatSpeech() {
        return String.format("You have burnt %d calories", (int) getValue() );
    }
}
