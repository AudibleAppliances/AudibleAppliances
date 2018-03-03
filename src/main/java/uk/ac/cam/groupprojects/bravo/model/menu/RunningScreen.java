package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 20/02/2018.
 */
public class RunningScreen extends BikeScreen {

    private boolean loadTip = false;

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

        if (state.isTimeChanging())
            matching += 1;

        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.RUNNING_SCREEN;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        if ( !loadTip ){
            synthesiser.speak("You can use the wheel to adjust the difficulty");
            loadTip = true;
        }
        ConfigData configData = bikeStateTracker.getConfig();
        for (BikeField field : BikeField.values()) {
            if (configData.isSpokenField(field)) {
                synthesiser.speak( bikeStateTracker.getFieldValue(field).speakValue() );
            }
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
