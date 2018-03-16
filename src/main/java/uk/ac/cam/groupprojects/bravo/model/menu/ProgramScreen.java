package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.Arrays;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Program;

// This is the screen where the user selects which program they want to use
public class ProgramScreen extends BikeScreen {
    private Program programValue;

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
                if (programValue.getValue() > 10) {
                    String converted = String.valueOf(programValue.getValue()).replaceAll("8", "0");
                    programValue.setValue(Integer.parseInt(converted));
                }
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
        if (programValue != null) {
            return Arrays.asList(programValue.formatSpeech());
        } else {
            return Arrays.asList();
        }
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ;
    }

    @Override
    public boolean isSpeakFirst() {
        return false;
    }
}
