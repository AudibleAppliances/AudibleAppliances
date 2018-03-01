package uk.ac.cam.groupprojects.bravo.model.menu;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

/**
 * Created by david on 20/02/2018.
 */
public class CyclingScreen extends BikeScreen {

    @Override
    public float screenProbability(BikeStateTracker bikeStateTracker) {
        return 0;
    }

    @Override
    public ScreenEnum getEnum() {
        return ScreenEnum.CYCLING_SCREEN;
    }

    @Override
    public void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser) {
        ConfigData configData = bikeStateTracker.getConfig();
        for (BikeField field : BikeField.values()) {
            if (configData.isSpokenField(field)) {
                synthesiser.speak( bikeStateTracker.getFieldValue(field).speakValue() );
            }
        }
    }

    @Override
    public int getSpeakDelay() {
        return 10000;
    }
}
