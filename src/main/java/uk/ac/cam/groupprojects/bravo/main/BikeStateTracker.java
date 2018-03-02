package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentActive;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by david on 13/02/2018.
 */
public class BikeStateTracker {
    class StateTime {
        public LocalDateTime addedTime;
        public Set<ScreenBox> activeText;

        public StateTime(LocalDateTime addedTime, Set<ScreenBox> activeText) {
            this.addedTime = addedTime;
            this.activeText = activeText;
        }
    }

    /**
     * Current state that we are tracking on the bike
     */
    private Map<BikeField, ScreenNumber> currentFields;
    private final LinkedList<StateTime> history;

    /**
     * Other state
     */
    private final ImageSegments segments;
    private final ConfigData configData;

    /**
     *
     * @param segments ImageSegments object to crop out parts of the image for processing
     * @param configData Configuration that can be passed around with this object
     */
    public BikeStateTracker(ImageSegments segments, ConfigData configData){
        history = new LinkedList<>();

        this.segments = segments;
        this.configData = configData;

        // Initialise currentFields;
        currentFields = new HashMap<>();
        currentFields.put(BikeField.CAL, new Calories());
        currentFields.put(BikeField.DISTANCE, new Distance());
        currentFields.put(BikeField.LOAD, new Load());
        currentFields.put(BikeField.PULSE, new Pulse());
        currentFields.put(BikeField.RPM, new RPM());
        currentFields.put(BikeField.SPEED, new Speed());
        currentFields.put(BikeField.TIME, new Time());
        currentFields.put(BikeField.WATT, new Watt());
    }

    public void updateBlinkingState(BufferedImage img) {
        throw new UnsupportedOperationException();
    }
    public void updateRecognisableState(BufferedImage img) {
        throw new UnsupportedOperationException();
    }
    
    public void updateState(BufferedImage newImage)
                throws IOException, UnrecognisedDigitException, NumberFormatException {

        LocalDateTime updateTime = LocalDateTime.now();
        Set<ScreenBox> activeSegs = new HashSet<>();

        // Map each screen region to the image of that region
        // Compute which LCD segments are lit up (active)
        HashMap<ScreenBox, BufferedImage> imgSegs = new HashMap<>();
        for (ScreenBox box : ScreenBox.values()) {
            BufferedImage imgSeg = segments.getImageBox(box, newImage);
            imgSegs.put(box, imgSeg);
            if (SegmentActive.segmentActive(imgSeg)) {
                activeSegs.add(box);
            }
        }

        // Recognise the text in each region of the screen
        // TODO (Keith): Maybe compute this lazily to improve performance?
        for (ScreenBox box : ScreenBox.values()) {
            if (activeSegs.contains(box)) {
                for (BikeField field : box.getFields()) {
                    if (field.getTitleBox() == null || activeSegs.contains(field.getTitleBox())) {
                        currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(newImage));
                    }
                }
            }
        }
        
        // 
        while (true) {
            Duration timeSpan = Duration.between(history.getFirst().addedTime, updateTime);
            if (timeSpan.toMillis() < 2 * BLINK_FREQ) {

            }
        }

        history.add(new StateTime(updateTime, activeSegs));
    }

    public ConfigData getConfig() {
        return configData;
    }

    public ScreenNumber getFieldValue(BikeField field) {
        return currentFields.get(field);
    }

    // Returns true iff the given box is lit in the latest received image
    public boolean isBoxActive(ScreenBox box) {
        return history.getLast().activeText.contains(box);
    }
}
