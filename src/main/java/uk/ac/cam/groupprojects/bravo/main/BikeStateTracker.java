package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.config.SpokenFields;
import uk.ac.cam.groupprojects.bravo.graphProcessing.Graph;
import uk.ac.cam.groupprojects.bravo.imageProcessing.BoxType;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
import uk.ac.cam.groupprojects.bravo.model.screen.LCD;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    //private Program currentProgram; TODO
    //private WATT currentWatt; TODO: add WATT

    private LCD lcdScreen;

    /**
     * Other state
     */
    private ImageSegments segments;

    public BikeStateTracker( ImageSegments segments ){
        currentCalories = new Calories();
        currentDistance = new Distance();
        currentLevel = new Level();
        currentPulse = new Pulse();
        currentSpeed = new Speed();
        currentTime = new Time();
        currentRPM = new RPM();
        //currentProgram = new Program; TODO
        //currentWATT = new Watt(); TODO: add watt

        lcdScreen = new LCD();

        this.segments = segments;
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
        if ( SegmentRecogniser.segmentActive(segments.getImageBox( BoxType.WATT, newImage)) ) {
            temp = segments.getImageBox( BoxType.LCD5, newImage );
            //currentWATT.setValue(SegmentRecogniser.recogniseInt(temp));
        }
        else if ( SegmentRecogniser.segmentActive(segments.getImageBox( BoxType.RPM, newImage))) {
            temp = segments.getImageBox( BoxType.LCD5, newImage );
            currentRPM.setValue(SegmentRecogniser.recogniseInt(temp));
        }

        if ( SegmentRecogniser.segmentActive(segments.getImageBox( BoxType.PROGRAM, newImage)) ) {
            temp = segments.getImageBox( BoxType.LCD6, newImage );
            //currentProgram.setValue(SegmentRecogniser.recogniseInt(temp));
        }
        else if ( SegmentRecogniser.segmentActive(segments.getImageBox( BoxType.LEVEL, newImage)) ) {
            temp = segments.getImageBox( BoxType.LCD6, newImage );
            currentLevel.setValue(SegmentRecogniser.recogniseInt(temp));
        }
    }

    public void speakItems(Synthesiser synthesiser, ConfigData config) {

        for (SpokenFields type : SpokenFields.values()) {
            if (config.isSpokenField(type)) {
                String speakVal = "";
                switch (type) {
                    case CAL: speakVal = currentCalories.speakValue(); break;
                    case DISTANCE: speakVal = currentDistance.speakValue(); break;
                    case PROGRAM: speakVal = currentLevel.speakValue(); break;
                    case PULSE: speakVal = currentPulse.speakValue(); break;
                    case SPEED: speakVal = currentSpeed.speakValue(); break;
                    case TIME: speakVal = currentTime.speakValue(); break;
                    //TODO: add Watt and Program
                }
                synthesiser.speak(speakVal);
            }
        }
    }
}
