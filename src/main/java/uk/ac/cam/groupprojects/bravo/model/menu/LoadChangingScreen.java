package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.Arrays;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Load;

// This is effectively a substate of the RunningScreen, specifically when dial's being rotated
// The "Load" indicator LCD lights up and we see the load level on LCD5
public class LoadChangingScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean active = state.isTimeChanging() &&
                           state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
                           state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
                           state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON &&
                           state.getBoxState(ScreenBox.LOAD) == LCDState.SOLID_ON &&
                           state.isBoxActiveNow(ScreenBox.LOAD);
        // Extra check for if the LOAD box is active in the latest image - without this, we
        // may have moved into a new state but not been in it long enough to have updated, so if
        // we read from the screen we'll get an old value.

        return active;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.LOAD_CHANGING_SCREEN;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        if (!bikeStateTracker.isBoxActiveNow(ScreenBox.LOAD))
            return Arrays.asList();
        Load load = (Load)bikeStateTracker.getFieldValue(BikeField.LOAD, false);
        if (load == null)
            return Arrays.asList();

        return Arrays.asList("Difficulty level " + load.getValue());
    }

    @Override
    public int getSpeakDelay() {
        return 3000;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}