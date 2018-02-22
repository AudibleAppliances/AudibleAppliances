package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeFields;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.graphProcessing.Graph;
import uk.ac.cam.groupprojects.bravo.imageProcessing.BoxType;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.Program;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
import uk.ac.cam.groupprojects.bravo.model.screen.LCD;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentActive;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 13/02/2018.
 */
public class BikeStateTracker {

    /**
     * Current state that we are tracking on the bike
     */
    private Calories currentCalories;
    private Distance currentDistance;
    private Level currentLevel;
    private Pulse currentPulse;
    private Speed currentSpeed;
    private Time currentTime;
    private RPM currentRPM;
    private Program currentProgram;
    private Watt currentWatt;

    private Map<BoxType, Boolean> activeText;

    private LCD lcdScreen;

    /**
     * Other state
     */
    private ImageSegments segments;
    private ConfigData configData;

    public BikeStateTracker(ImageSegments segments, ConfigData configData){
        currentCalories = new Calories();
        currentDistance = new Distance();
        currentLevel = new Level();
        currentPulse = new Pulse();
        currentSpeed = new Speed();
        currentTime = new Time();
        currentRPM = new RPM();
        currentProgram = new Program();
        currentWatt = new Watt();

        activeText = new HashMap<>();

        lcdScreen = new LCD();

        this.segments = segments;
        this.configData = configData;
    }

    public void processNewImage( BufferedImage newImage )
                throws IOException,UnrecognisedDigitException, NumberFormatException {
        BufferedImage temp;

        // Read fixed boxes
        temp = segments.getImageBox( BoxType.LCD2, newImage );
        currentSpeed.setValue(SegmentRecogniser.recogniseInt(temp));

        temp = segments.getImageBox( BoxType.LCD1, newImage );
        currentTime.setValue(SegmentRecogniser.recogniseInt(temp));

        temp = segments.getImageBox( BoxType.LCD3, newImage );
        currentDistance.setValue(SegmentRecogniser.recogniseInt(temp));

        temp = segments.getImageBox( BoxType.LCD4, newImage );
        currentCalories.setValue(SegmentRecogniser.recogniseInt(temp));

        temp = segments.getImageBox( BoxType.LCD7, newImage );
        currentPulse.setValue(SegmentRecogniser.recogniseInt(temp));

        temp = segments.getImageBox( BoxType.GRAPH, newImage );
        lcdScreen = new Graph( temp ).get();

        // Read changing boxes
        if ( SegmentActive.segmentActive(segments.getImageBox( BoxType.WATT, newImage)) ) {
            activeText.put(BoxType.WATT, true);
            activeText.put(BoxType.RPM, false);
            temp = segments.getImageBox( BoxType.LCD5, newImage );
            currentWatt.setValue(SegmentRecogniser.recogniseInt(temp));
        }
        else if ( SegmentActive.segmentActive(segments.getImageBox( BoxType.RPM, newImage))) {
            activeText.put(BoxType.RPM, true);
            activeText.put(BoxType.WATT, false);
            temp = segments.getImageBox( BoxType.LCD5, newImage );
            currentRPM.setValue(SegmentRecogniser.recogniseInt(temp));
        }

        if ( SegmentActive.segmentActive(segments.getImageBox( BoxType.PROGRAM, newImage)) ) {
            activeText.put(BoxType.PROGRAM, true);
            activeText.put(BoxType.LEVEL, false);
            temp = segments.getImageBox( BoxType.LCD6, newImage );
            currentProgram.setValue(SegmentRecogniser.recogniseInt(temp));
        }
        else if ( SegmentActive.segmentActive(segments.getImageBox( BoxType.LEVEL, newImage)) ) {
            activeText.put(BoxType.LEVEL, true);
            activeText.put(BoxType.PROGRAM, false);
            temp = segments.getImageBox( BoxType.LCD6, newImage );
            currentLevel.setValue(SegmentRecogniser.recogniseInt(temp));
        }
    }

    /**
     * Speaks the values out loud as set in the config file
     *
     * @param synthesiser The systhesiser to speak the words
     * @param config Config holding which fields to be spoken
     */
    public void speakItems(Synthesiser synthesiser, ConfigData config) {

        for (BikeFields type : BikeFields.values()) {
            if (config.isSpokenField(type)) {
                String speakVal = "";
                switch (type) {
                    case CAL: speakVal = currentCalories.speakValue(); break;
                    case DISTANCE: speakVal = currentDistance.speakValue(); break;
                    case LEVEL: speakVal = currentLevel.speakValue(); break;
                    case PULSE: speakVal = currentPulse.speakValue(); break;
                    case SPEED: speakVal = currentSpeed.speakValue(); break;
                    case TIME: speakVal = currentTime.speakValue(); break;
                    case PROGRAM: speakVal = currentProgram.speakValue(); break;
                    case WATT: speakVal = currentWatt.speakValue(); break;
                }
                synthesiser.speak(speakVal);
            }
        }
    }

    public ConfigData getConfig() {
        return configData;
    }
}
