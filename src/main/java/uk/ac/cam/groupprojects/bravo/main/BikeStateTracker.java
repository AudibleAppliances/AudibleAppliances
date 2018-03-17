package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.imageProcessing.IntelligentCropping;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentActive;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BikeStateTracker {

    // Holds data about the bike at a given time (enough to compute the state, so LCD and the recognised time)
    class StateTime {
        public final long addedMillis;
        public final Set<ScreenBox> activeBoxes;
        public final Integer bikeTime;
        public BikeScreen state;

        public StateTime(long addedMillis, Set<ScreenBox> activeBoxes) {
            this(addedMillis, activeBoxes, null);
        }
        public StateTime(long addedMillis, Set<ScreenBox> activeBoxes, Integer bikeTime) {
            this.addedMillis = addedMillis;
            this.activeBoxes = activeBoxes;
            this.bikeTime = bikeTime;
            state = null;
        }
    }

    // Holds the last image we saw for each segment *when it was active* - no blank images here
    class ImageTime {
        public final long addedMillis;
        public final boolean segmentActive;
        private final BufferedImage boxImage;
        private ScreenNumber recognisedValue;
        private Double brightness;

        public ImageTime(long addedMillis, BufferedImage boxImage, boolean segmentActive) {
            this.addedMillis = addedMillis;
            this.boxImage = boxImage;
            this.segmentActive = segmentActive;

            recognisedValue = null;
            brightness = null;
        }

        public ScreenNumber getRecognisedValue(BikeField field) throws IOException, UnrecognisedDigitException {
            if (recognisedValue == null) {
                IntelligentCropping.intelligentCrop(boxImage.getRaster());

                int value = SegmentRecogniser.recogniseInt(boxImage, field.getScreenBox().getThreshold());
                System.out.println("Recognised " + field.toString() + ": " + value);

                recognisedValue = field.getScreenNumber();
                recognisedValue.setValue(value);
            }

            return recognisedValue;
        }

        // Brightness on the non-thresholded image
        public double getBrightness() {
            if (brightness == null) {
                brightness = SegmentActive.imageAverage(boxImage, 0.5, 0);
            }

            return brightness;
        }
    }

    // Necessary class state
    private final ConfigData configData;
    private final Synthesiser synthesiser;

    private static long lastSpeakTime;

    // Back of the list is the most recent state added
    // We keep 2 seconds of state, so we can determine if time is changing
    // We only look at 1 second of the state to determine if a segment is blinking,
    // otherwise we introduce a lot of latency when changing state
    private final LinkedList<StateTime> history;

    private final Map<ScreenBox, LCDState> boxStates; // Which LCDs are active right now?
    private final Map<ScreenBox, LinkedList<ImageTime>> latestImages; // Latest non-blank images of each LCD
    private BikeScreen currentScreen; // What do we think the current state is?
    private boolean timeChanging; // Is the time LCD counting up/down?

    public BikeStateTracker(ConfigData config, Synthesiser synth) {
        history = new LinkedList<>();
        boxStates = new HashMap<>();
        latestImages = new HashMap<>();
        for (ScreenBox b : ScreenBox.values())
            latestImages.put(b, new LinkedList<>());

        timeChanging = false;
        configData = config;
        synthesiser = synth;

        lastSpeakTime = 0;
    }

    /**
     * Update the current state using pre-segmented image
     *
     * @param imgSegs
     * @throws IOException
     * @throws UnrecognisedDigitException
     * @throws NumberFormatException
     */
    public void updateState(Map<ScreenBox, BufferedImage> imgSegs)
                throws IOException, UnrecognisedDigitException, NumberFormatException {
        long currentTime = System.currentTimeMillis();
        Set<ScreenBox> activeSegs = new HashSet<>();

        // Store an image of each LCD
        for (ScreenBox box : ScreenBox.values()) {
            BufferedImage boxImage = imgSegs.get(box);
            BufferedImage thresholded = SSOCRUtil.roughThreshold(boxImage);
            
            boolean segmentActive = SegmentActive.segmentActive(thresholded);
            if (segmentActive) {
                // If the LCD is active, record it and update the latest image we have of it
                activeSegs.add(box);
            }
            latestImages.get(box).add(new ImageTime(currentTime, boxImage, segmentActive));
        }

        // Store the new state, with the time we recognised at the moment
        Time timeState = (Time)getFieldValue(BikeField.TIME, false);
        Integer bikeTime = null;
        if (timeState != null) { // If this is null, it just means we've only just started and don't have enough data yet
            bikeTime = timeState.getValue();
        }
        history.add(new StateTime(currentTime, activeSegs, bikeTime));

        // DEBUG
        if (bikeTime == null) {
            System.out.println("Failed to recognise the time");
        }

        // Remove state information that's older than two complete blink cycles (2s) ago
        removeOldHistory(currentTime);

        int imageHistoryMaxLength = latestImages.values().stream()
                                                          .map(l -> l.size())
                                                          .max(Comparator.<Integer>naturalOrder()).get();
        System.out.println("Items in state history: " + history.size());
        System.out.println("Items in image history: " + imageHistoryMaxLength);

        // Update which LCDs we know are solid/blinking
        updateSolidBlinking(currentTime);
        updateTimeChanging();

        // DEBUG: Output the state of every box
        if (ApplicationConstants.DEBUG) {
            System.out.println("Time Changing: " + isTimeChanging());
            for (ScreenBox box : ScreenBox.values()) {
                System.out.println(box.toString() + ": " + getBoxState(box).toString());
            }
            System.out.println();
        }

        BikeScreen instantaneousScreen = null;
        for (ScreenEnum s : ScreenEnum.values()) {
            BikeScreen screen = s.getBikeScreen();
            boolean inState = screen.isActiveScreen(this);
            if (inState) {
                instantaneousScreen = screen;
                break;
            }
        }

        // Update the record of the state we're in at the moment
        history.getLast().state = instantaneousScreen;

        System.out.println();
        if (instantaneousScreen == null) {
            instantaneousScreen = ScreenEnum.INVALID_SCREEN.getBikeScreen();
            System.out.println("Failed to identify state");
        }
        else {
            System.out.println("Instantaneous State: " + instantaneousScreen.getEnum().toString());
        }
        if (currentScreen == null) {
            System.out.println("No stable state");
        }
        else {
            System.out.println("Stable State: " + currentScreen.getEnum().toString());
        }
        System.out.println();

        // Only update the screen if all the history we have agrees in us being in this screen
        boolean stateChanged = false;
        if (!history.isEmpty()) {
            BikeScreen newState = history.getLast().state;
            if (newState != null && newState.getEnum() != currentScreen.getEnum() && stableState(currentTime)) {
                currentScreen = newState;
                stateChanged = true;
            }
        }

        System.out.println("Items in speak queue: " + synthesiser.getQueueSize());

        // Check if it's the time to speak, and if yes then speak
        if (currentScreen != null) {
            // Check that we've been in this state for half the history
            // (half to reduce the latency of speaking after switching to a new state)

            boolean stableState = stableState(currentTime, 2 * ApplicationConstants.BLINK_FREQ_MILLIS);

            // If we've changed state, get rid of all the messages we still need to speak from the old state
            if (stateChanged) {
                synthesiser.clearQueue();
            }

            // Speak if we've stably changed state and the current state demands we speak immediately,
            // or if we've just not spoken in a while
            boolean speakOverdue = System.currentTimeMillis() - lastSpeakTime > currentScreen.getSpeakDelay();
            System.out.println("Stable: " + stableState);
            System.out.println("State changed: " + stateChanged);
            System.out.println("Speech overdue: " + speakOverdue);
            if (stableState &&
                ((stateChanged && currentScreen.isSpeakFirst()) || speakOverdue)) {

                List<String> dialogs = currentScreen.formatSpeech(this);
                for (String text : dialogs) {
                    synthesiser.speak(text);
                }
                lastSpeakTime = System.currentTimeMillis();
            }
        }
    }

    private boolean stableState(long currentTime) {
        return stableState(currentTime, 100 * ApplicationConstants.BLINK_FREQ_MILLIS);
    }
    private boolean stableState(long currentTime, long lookback) {
        if (history.isEmpty()) {
            return false; // Treat no state as not being stable
        }

        List<StateTime> recentHistory = history.stream()
                            .filter(s -> currentTime - lookback < s.addedMillis)
                            .collect(Collectors.toList());

        boolean allNull = recentHistory.stream().allMatch(s -> s.state == null);
        BikeScreen screen = recentHistory.get(0).state;
        boolean allSameState = screen != null && recentHistory.stream()
                            .allMatch(s -> s.state != null && s.state.getEnum() == screen.getEnum());

        return allNull || allSameState;
    }

    // Update which LCDs are blinking and which are solid
    private void updateSolidBlinking(long currentTime) {
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
                               .filter(s -> currentTime - 2 * ApplicationConstants.BLINK_FREQ_MILLIS < s.addedMillis)
                               .allMatch(s -> s.activeBoxes.contains(box) == currentlyActive);

                if (!historyMatches) { // Changes over time => blinking
                    blinking = true;
                } else {
                    active = currentlyActive;
                }
            }

            if (blinking) {
                boxStates.put(box, LCDState.BLINKING);
            } else {
                boxStates.put(box, active ? LCDState.SOLID_ON : LCDState.SOLID_OFF);
            }
        }
    }

    // Update whether the time LCD is counting over time or not
    private void updateTimeChanging() {
        if (history.size() == 0) {
            return;
        }

        Integer currentBikeTime = history.getLast().bikeTime;
        timeChanging = !history.stream()
                               .allMatch(s -> s.bikeTime == currentBikeTime);
    }

    private void removeOldHistory(long currentTime) {
        while (history.size() > 0) {
            if (currentTime - 4 * ApplicationConstants.BLINK_FREQ_MILLIS > history.getFirst().addedMillis) {
                history.removeFirst();
            }
            else {
                break;
            }
        }

        for (ScreenBox box : ScreenBox.values()) {
            while (latestImages.get(box).size() > 0) {
                if (currentTime - 2 * ApplicationConstants.BLINK_FREQ_MILLIS > latestImages.get(box).getFirst().addedMillis) {
                    latestImages.get(box).removeFirst();
                }
                else {
                    break;
                }
            }
        }

        System.out.println();
    }

    public ConfigData getConfig() {
        return configData;
    }

    public ScreenNumber getFieldValue(BikeField field, boolean blinking) {
        //System.out.println("Getting field value " + field.toString());
        ScreenBox containingBox = field.getScreenBox();

        ImageTime image = null;
        if (!blinking) {
            // Get the latest active image - we don't care about whether it's the brightest, as it's not blinking
            Iterator<ImageTime> i = latestImages.get(containingBox).descendingIterator();
            while (i.hasNext()) {
                ImageTime img = i.next();
                if (img.segmentActive) {
                    image = img;
                    break;
                }
            }
        }
        else {
            // Get the image in the history with the greatest brightness - not the latest active one
            ImageTime maxBrightness = null;
            for (ImageTime i : latestImages.get(containingBox)) {
                if (maxBrightness == null || i.getBrightness() > maxBrightness.getBrightness()) {
                    maxBrightness = i;
                }
            }

            image = maxBrightness;
            if (image != null) {
                System.out.println("Got max brightness of blinking field " + field.toString() + " as " + image.brightness);
            }
        }

        if (image == null) { // No images for this box yet
            System.out.println("Got null image");
            return null;
        }

        ScreenNumber recognisedValue = null;
        try {
            recognisedValue = image.getRecognisedValue(field);
        }
        catch (IOException e) {
            System.out.println("IOException when recognising " + field.toString());
            e.printStackTrace();
            return null;
        }
        catch (NumberFormatException | UnrecognisedDigitException e) {
            System.out.println("Failed to recognise digit for " + field.toString());
            System.out.println(e.getMessage());
            return null;
        }


        if (ApplicationConstants.DEBUG) {
            if (recognisedValue == null)
                System.out.println("Value of " + field.toString() + ": lastImage.recognisedValue null");
            else
                System.out.println("Value of " + field.toString() + ": " + image.recognisedValue.getValue());
        }
        return recognisedValue;
    }

    public LCDState getBoxState(ScreenBox box) {
        return boxStates.get(box);
    }

    public boolean isTimeChanging() {
        return timeChanging;
    }

    // Returns true iff the given LCD is lit in the latest received image
    public boolean isBoxActiveNow(ScreenBox box) {
        return history.getLast().activeBoxes.contains(box);
    }
}
