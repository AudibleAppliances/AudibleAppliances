package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
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
    private Map<BikeField, ScreenNumber> currentFields;
    private Map<ScreenBox, Boolean> activeText;

    /**
     * Other state
     */
    private ImageSegments segments;
    private ConfigData configData;

    public BikeStateTracker(ImageSegments segments, ConfigData configData){
        activeText = new HashMap<>();

        this.segments = segments;
        this.configData = configData;

        // Initialise currentFields;
        currentFields.put(BikeField.CAL, new Calories());
        currentFields.put(BikeField.DISTANCE, new Distance());
        currentFields.put(BikeField.LEVEL, new Level());
        currentFields.put(BikeField.PULSE, new Pulse());
        currentFields.put(BikeField.RPM, new RPM());
        currentFields.put(BikeField.SPEED, new Speed());
        currentFields.put(BikeField.TIME, new Time());
        currentFields.put(BikeField.WATT, new Watt());
    }

    public void updateState(BufferedImage newImage)
                throws IOException,UnrecognisedDigitException, NumberFormatException {

        HashMap<ScreenBox, BufferedImage> imgSegs = new HashMap<>();
        HashMap<ScreenBox, Boolean> isActive = new HashMap<>();

        for (ScreenBox box : ScreenBox.values()) {
            BufferedImage imgSeg = segments.getImageBox(box, newImage);
            imgSegs.put(box, imgSeg);
            isActive.put(box, SegmentActive.segmentActive(imgSeg));
        }

        // Read in data
        for (ScreenBox box : ScreenBox.values()) {
            if (isActive.get(box)) {
                for (BikeField field : box.getFields()) {
                    if (isActive.get(field.getTitleBox())) {
                        currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(newImage));
                    }
                }
            }
        }
   }

    /**
     * Speaks the values out loud as set in the config file
     *
     * @param synthesiser The synthesiser to speak the words
     */
    public void speakItems(Synthesiser synthesiser, ConfigData config) {

        for (BikeField field : BikeField.values()) {
            if (config.isSpokenField(field)) {
                synthesiser.speak(currentFields.get(field).speakValue());
            }
        }
    }

    public ConfigData getConfig() {
        return configData;
    }
}
