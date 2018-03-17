package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Program;

// This is the screen where the user selects which program they want to use
public class ProgramScreen extends BikeScreen {
    private Program programValue;
    private long lastTimeLongSpoken = 0; // Keep track of the last time we spoke the "long version" of this
    private final long TIME_BETWEEN_LONG_SPEECH = 30000;

    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean isActive = !state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_9) == LCDState.SOLID_OFF;

        if (isActive) {
            programValue = (Program)state.getFieldValue(BikeField.PROGRAM, true);

            if (programValue == null) {
                System.out.println("Program value is null");
            } else {
                int value;
                if (programValue.getValue() > 10) {
                    String converted = String.valueOf(programValue.getValue()).replaceAll("8", "0");
                    value = Integer.parseInt(converted);
                }
                else {
                    value = programValue.getValue();
                }
                programValue.setValue(value);

                System.out.println("Got program value: " + programValue.getValue());
            }
        }

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.PROGRAM;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<String> dialog = new ArrayList<>();

        // Check if we're on schedule to give a longer speech
        long currentTime = System.currentTimeMillis();
        if (currentTime - TIME_BETWEEN_LONG_SPEECH > lastTimeLongSpoken) {
            dialog.add("Rotate to select a program. Press start to begin that program.");
            lastTimeLongSpoken = currentTime;
        }

        if (programValue != null) {
            dialog.add(programValue.formatSpeech());
        }

        return dialog;
    }

    @Override
    public int getSpeakDelay() {
        // Wait a long time if we're waiting for the long speech to finish
        if (System.currentTimeMillis() - 5000 < lastTimeLongSpoken) {
            return 10000;
        }
        else { // Otherwise don't wait long at all
            return 4000;
        }
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
