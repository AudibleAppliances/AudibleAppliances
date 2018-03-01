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

    /**
     *
     * @param segments ImageSegments object to crop out parts of the image for processing
     * @param configData Configuration that can be passed around with this object
     */
    public BikeStateTracker(ImageSegments segments, ConfigData configData){
        activeText = new HashMap<>();

        this.segments = segments;
        this.configData = configData;

        // Initialise currentFields;
        currentFields = new HashMap<>();
        currentFields.put(BikeField.CAL, new Calories());
        currentFields.put(BikeField.DISTANCE, new Distance());
        currentFields.put(BikeField.LEVEL, new Level());
        currentFields.put(BikeField.PULSE, new Pulse());
        currentFields.put(BikeField.RPM, new RPM());
        currentFields.put(BikeField.SPEED, new Speed());
        currentFields.put(BikeField.TIME, new Time());
        currentFields.put(BikeField.WATT, new Watt());
    }

    /**
     * Given an image of the screen, updates the state from what is on the screen
     *
     * @param newImage Image of the full screen
     * @throws IOException If there is a problem in checking if segment is active
     * @throws UnrecognisedDigitException If the digit in any segment cannot be recognised
     * @throws NumberFormatException If the digit recognised is not in a vlaid format
     */
    public void updateState(BufferedImage newImage)
                throws IOException, UnrecognisedDigitException, NumberFormatException {

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
                    if (field.getTitleBox() == null || isActive.get(field.getTitleBox())) {
                        currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(newImage));
                    }
                }
            }
        }
   }

    public ConfigData getConfig() {
        return configData;
    }

    public ScreenNumber getFieldValue(BikeField field) {
        return currentFields.get(field);
    }

    public boolean isBoxActive(ScreenBox box) {
        return activeText.get(box);
    }
}
