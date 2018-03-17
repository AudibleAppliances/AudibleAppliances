package uk.ac.cam.groupprojects.bravo.model.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;

public class RunningScreen extends BikeScreen {
    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean active = state.isTimeChanging() &&
                           state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
                           state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.SOLID_ON &&
                           state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.SOLID_ON &&
                           state.getBoxState(ScreenBox.LOAD) == LCDState.SOLID_OFF;

        return active;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<String> dialogs = new ArrayList<>();

        ConfigData configData = bikeStateTracker.getConfig();
        final BikeField[] readableFields = new BikeField[]
        {
            BikeField.SPEED,
            BikeField.DISTANCE,
            BikeField.RPM,
            BikeField.CAL,
            BikeField.WATT,
        };

        for (BikeField field : readableFields) {
            System.out.println(field.toString());
            try { System.in.read() ; } catch (Exception e) { }
            if (configData.isSpokenField(field)) {
                System.out.println("Is spoken");
                ScreenNumber n = null;

                if (field.hasIndicatorBox()) {
                    try {
                        // Get the last reading from when the indicator was lit up
                        n = bikeStateTracker.getLastActiveReading(field);
                    }
                    catch (IOException | UnrecognisedDigitException e) {
                        // Do nothing - an error'll get picked up on later
                        System.out.println("getLastActiveReading call failed");
                    }
                }
                else {
                    // Just get the value of the field
                    n = bikeStateTracker.getFieldValue(field, false);
                }

                if (n != null)
                    dialogs.add(n.formatSpeech());
            }
        }

        return dialogs;
    }

    @Override
    public int getSpeakDelay() {
        return ApplicationConstants.RUNNING_SPEAK_FREQ;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }
}
