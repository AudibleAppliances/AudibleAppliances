package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class SelectProgramScreen extends BikeScreen {

    private int currentProgram = 1;

    @Override
    public boolean isActiveScreen(BikeStateTracker state) {

        boolean isActive = !state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_9) == LCDState.BLINKING;

        // If this is the current state, we get the program number
        if (isActive) {
            currentProgram = state.getFieldValue(BikeField.PROGRAM).getValue();
        }

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECT_PROGRAM;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("Currently selecting program. The current program is program " +
                currentProgram + ". Rotate to change or enter to select your desired program.");
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ / 2;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
