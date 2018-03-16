package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.Arrays;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Command;
import uk.ac.cam.groupprojects.bravo.tts.SpeakCommand;

public class ErrorScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {

        return state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON &&
               state.getBoxState(ScreenBox.LCD_TEXT_9) == LCDState.SOLID_ON;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.ERROR_SCREEN;
    }

    @Override
    public List<Command> formatSpeech(BikeStateTracker bikeStateTracker) {
        return Arrays.asList(new SpeakCommand("The bike is currently loading up, please wait!"));
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.DEFAULT_SPEAK_FREQ;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }

}
