package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 01/03/2018.
 */
public class SelectHRCScreen extends BikeScreen {
    @Override
    public float screenActiveProbability(BikeStateTracker state) {
        int matching = 0;

        // Watt xor Load can be on
        if (state.getBoxState(ScreenBox.WATT) == LCDState.SOLID_ON ^ state.getBoxState(ScreenBox.LOAD) == LCDState.SOLID_ON)
            matching += 1;
        // Speed xor RPM can be on
        if (state.getBoxState(ScreenBox.SPEED) == LCDState.SOLID_ON ^ state.getBoxState(ScreenBox.RPM) == LCDState.SOLID_ON)
            matching += 1;

        matching += state.boxStateIndicator(ScreenBox.GRAPH, LCDState.SOLID_ON);

        matching += state.boxStateIndicator(ScreenBox.LCD1, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD2, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD3, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD4, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD5, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD6, LCDState.SOLID_ON);

        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_1, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_2, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_3, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_4, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_5_TOP, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_5_BOTTOM, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_6, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_7_BOTTOM, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_7_TOP, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_8, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_9_TOP, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_9_BOTTOM, LCDState.SOLID_ON);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_10, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_11, LCDState.SOLID_OFF);
        matching += state.boxStateIndicator(ScreenBox.LCD_TEXT_12, LCDState.SOLID_OFF);

        if (state.isTimeChanging())
            matching += 1;

        return (float)matching / 25; // 25 indicators for this state
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.SELECT_HRC;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        synthesiser.speak("Currently selecting HRC. Please click Start/Stop to start riding!");
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
