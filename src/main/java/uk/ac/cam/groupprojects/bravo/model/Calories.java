package uk.ac.cam.groupprojects.bravo.model;

public class Calories extends ScreenNumber{

    public Calories(int minBound, int maxBound) {
        super(0, 999);
    }

    @Override
    public String speakValue() {
        return String.format("You have burnt %d calories in this session", (int) getValue() );
    }
}
