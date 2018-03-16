package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;
import uk.ac.cam.groupprojects.bravo.tts.Command;
import uk.ac.cam.groupprojects.bravo.tts.DelayCommand;
import uk.ac.cam.groupprojects.bravo.tts.SpeakCommand;

public class PausedScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean isActive = !state.isTimeChanging() &&
                            state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
                            state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
                            state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON;

        return isActive;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.PAUSED_SCREEN;
    }

    @Override
    public List<Command> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<Command> commands = new ArrayList<>();
        commands.add(new SpeakCommand("The bike is currently paused."));
        commands.add(new DelayCommand());

        ConfigData configData = bikeStateTracker.getConfig();
        if (configData.isSpokenField(BikeField.DISTANCE)) {
            ScreenNumber n = bikeStateTracker.getFieldValue(BikeField.DISTANCE, false);
            if (n != null) {
                commands.add(new SpeakCommand(n.formatSpeech()));
                commands.add(new DelayCommand());
            }
        }
        if (configData.isSpokenField(BikeField.TIME)) {
            ScreenNumber n = bikeStateTracker.getFieldValue(BikeField.TIME, false);
            if (n != null) {
                commands.add(new SpeakCommand(n.formatSpeech()));
                commands.add(new DelayCommand());
            }
        }
        return commands;
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
