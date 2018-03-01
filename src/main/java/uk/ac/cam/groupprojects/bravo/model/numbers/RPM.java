package uk.ac.cam.groupprojects.bravo.model.numbers;


public class RPM extends ScreenNumber {

    public RPM() {
        super(0, 999);
    }

    @Override
    public String speakValue() {
        return String.format("Your current rotations per minute is %d", getValue() );
    }
}
