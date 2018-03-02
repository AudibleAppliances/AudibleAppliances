package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.Set;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

public abstract class BikeScreen {
    // The sets of LCDs on the bike's screen that need to be solid/blinking for us
    // to recognise that we're in this state.
    protected final Set<BikeField> solidFields;
    protected final Set<BikeField> blinkingFields;

    protected BikeScreen(Set<BikeField> solidFields, Set<BikeField> blinkingFields) {
        this.solidFields = solidFields;
        this.blinkingFields = blinkingFields;
    }

    public float screenActiveProbability(BikeStateTracker bikeStateTracker) {
        int matchingFields = 0;
        
        return 0;
    }

    public abstract ScreenEnum getEnum();

    public abstract void speakItems(BikeStateTracker bikeStateTracker, Synthesiser synthesiser );

    public abstract int getSpeakDelay();

    //When we change to this screen, do we immediately want to notify the user
    public abstract boolean isSpeakFirst();
}