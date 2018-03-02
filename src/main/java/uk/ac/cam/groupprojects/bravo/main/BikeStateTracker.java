package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
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
        public Set<ScreenBox> activeBoxes;
        public int recognisedTime;

        public StateTime(LocalDateTime addedTime, Set<ScreenBox> activeBoxes, int recognisedTime) {
            this.addedTime = addedTime;
            this.activeBoxes = activeBoxes;
        }
    }

    /**
     * Current state that we are tracking on the bike
     */
    private Map<BikeField, ScreenNumber> currentFields;
    // Back of the list is the most recent state added
    // We keep 2 seconds of state, so we can determine if time is changing
    // We only look at 1 second of the state to determine if a segment is blinking,
    // otherwise we introduce a lot of latency when changing state
    private final LinkedList<StateTime> history;

    // State over time of the LCDs - 
    private final Map<ScreenBox, LCDState> boxStates;
    private boolean timeChanging;

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
    public BikeStateTracker(ImageSegments segments, ConfigData configData) {
        history = new LinkedList<>();
        boxStates = new HashMap<>();
        timeChanging = false;

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

    public void updateState(BufferedImage newImage)
                throws IOException, UnrecognisedDigitException, NumberFormatException {

        LocalDateTime currentTime = LocalDateTime.now();
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
        
        // Remove state information that's older than two complete blink cycle ago
        while (history.size() > 0) {
            if (olderThanBy(history.getFirst().addedTime, currentTime, 4 * ApplicationConstants.BLINK_FREQ)) {
                history.removeFirst();
            } else {
                break;
            }                
        }

        // Store the new state, with the time we recognised at the moment
        history.add(new StateTime(currentTime, activeSegs, currentFields.get(BikeField.TIME).getValue()));

        // Update which LCDs we know are solid/blinking
        updateSolidBlinking(currentTime);
        updateTimeChanging();
    }

    // Update which boxes are blinking and which are solid
    private void updateSolidBlinking(LocalDateTime currentTime) {
        // Reset all
        boxStates.clear();

        for (ScreenBox box : ScreenBox.values()) {
            // Default to each box being solid off
            boolean blinking = false;
            boolean active = false;

            // Loop over the last second of history we have
            if (history.size() > 0) {
                boolean currentlyActive = isBoxActiveNow(box);
                boolean historyMatches =
                        history.stream()
                               .filter(s ->
                                   !olderThanBy(s.addedTime, currentTime, 2 * ApplicationConstants.BLINK_FREQ))
                               .allMatch(s ->
                                   s.activeBoxes.contains(box) == currentlyActive);

                if (!historyMatches) { // Changes over time => blinking
                    blinking = true;
                } else {
                    active = currentlyActive;
                }
            }

            if (blinking) {
                boxStates.put(box, LCDState.BLINKING);
            } else if (active) {
                boxStates.put(box, LCDState.SOLID_ON);
            } else {
                boxStates.put(box, LCDState.SOLID_OFF);
            }
        }
    }

    private void updateTimeChanging() {
        if (history.size() == 0) {
            return;
        }

        int currentTime = history.getLast().recognisedTime;
        timeChanging = history.stream()
                              .allMatch(s -> s.recognisedTime == currentTime);
    }

    private boolean olderThanBy(LocalDateTime early, LocalDateTime late, long milliseconds) {
        Duration timeSpan = Duration.between(early, late);
        return timeSpan.toMillis() > milliseconds;
    }

    public ConfigData getConfig() {
        return configData;
    }

    public ScreenNumber getFieldValue(BikeField field) {
        return currentFields.get(field);
    }

    public Map<ScreenBox, LCDState> getBoxStates() {
        return boxStates;
    }

    public int boxStateIndicator(ScreenBox box, LCDState state) {
        if (getBoxStates().get(box) == state) {
            return 1;
        } else {
            return 0;
        }
    }

    // Returns true iff the given box is lit in the latest received image
    public boolean isBoxActiveNow(ScreenBox box) {
        return history.getLast().activeBoxes.contains(box);
    }
}
