package uk.ac.cam.groupprojects.bravo.model.numbers;

public class Pulse extends ScreenNumber {

    public Pulse() {
        super(0, 240);
    }

    @Override
    public String speakValue() {
        return String.format("Your current heart rate is %d beats per minute", getValue() );
    }
}
