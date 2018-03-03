package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.model.numbers.*;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentActive;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
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

    // Necessary class state
    private final Map<ScreenEnum, BikeScreen> screens = new HashMap<>();
    private final ConfigData configData;
    private final Synthesiser synthesiser;

    // Current Screen state
    private static BikeScreen currentScreen;

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

    public BikeStateTracker(ConfigData config, Synthesiser synth) {
        history = new LinkedList<>();
        boxStates = new HashMap<>();
        timeChanging = false;
        configData = config;
        synthesiser = synth;

        // Initialise currentFields;
        currentFields = new HashMap<>();
        currentFields.put(BikeField.CAL, new Calories());
        currentFields.put(BikeField.DISTANCE, new Distance());
        currentFields.put(BikeField.PULSE, new Pulse());
        currentFields.put(BikeField.RPM, new RPM());
        currentFields.put(BikeField.LOAD, new Load());
        currentFields.put(BikeField.SPEED, new Speed());
        currentFields.put(BikeField.TIME, new Time());
        currentFields.put(BikeField.WATT, new Watt());

        // Initialise screens
        screens.put(ScreenEnum.OFF_SCREEN, new OffScreen());
        screens.put(ScreenEnum.ERROR_SCREEN, new ErrorScreen());
        screens.put(ScreenEnum.INITIAL_SCREEN, new InitialScreen());
        screens.put(ScreenEnum.RUNNING_SCREEN, new RunningScreen());
        screens.put(ScreenEnum.PAUSED_SCREEN, new PausedScreen());
        screens.put(ScreenEnum.PROGRAM, new ProgramScreen());
        screens.put(ScreenEnum.SELECT_MANUAL, new SelectManualScreen());
        screens.put(ScreenEnum.SELECT_HRC, new SelectHRCScreen());
        screens.put(ScreenEnum.SELECT_USER_PROGRAM, new SelectUserProgramScreen());
        screens.put(ScreenEnum.SELECT_WATTS, new SelectWattScreen());
        screens.put(ScreenEnum.SELECT_PROGRAM, new SelectProgramScreen());

        // Set default screen
        currentScreen = screens.get(ScreenEnum.OFF_SCREEN);
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

        //////////////////////////////////////////
        // Update the screen (ie overall state) //
        //////////////////////////////////////////

        if (ApplicationConstants.DEBUG)
            System.out.println("DETECTING CHANGE SCREEN STATE");

        BikeScreen newScreen = null;
        BikeScreen oldScreen = currentScreen;

        for (BikeScreen screen : screens.values()) {
            boolean inState = screen.isActiveScreen(this);
            if (inState) {
                newScreen = screen;
                break;
            }
        }

        if (newScreen != null) {
            currentScreen = newScreen;
            if (newScreen != oldScreen && newScreen.isSpeakFirst())
                newScreen.speakItems(this, synthesiser);
        }
        else {
            if (ApplicationConstants.DEBUG)
                System.out.println("Failed to recognise state.");
        }

        System.out.println("Establishing bike state is " + currentScreen.getEnum().toString());
        System.out.println();

        ////////////////////////////////////////
        // Update the state (ie field values) //
        ////////////////////////////////////////

        LocalDateTime currentTime = LocalDateTime.now();
        Set<ScreenBox> activeSegs = new HashSet<>();

        // Map each screen region to the image of that region
        // Compute which LCD segments are lit up (active)
        for (ScreenBox box : ScreenBox.values()) {
            if (SegmentActive.segmentActive(imgSegs.get(box))) {
                activeSegs.add(box);
            }
        }

        // Recognise the text in each region of the screen
        if (currentScreen.getEnum() == ScreenEnum.RUNNING_SCREEN || currentScreen.getEnum() == ScreenEnum.PROGRAM) {
            for (ScreenBox box : ScreenBox.values()) {
                if (activeSegs.contains(box)) {
                    for (BikeField field : box.getFields()) {
                        if (field.getTitleBox() == null || activeSegs.contains(field.getTitleBox())) {
                            if (ApplicationConstants.DEBUG) {
                                System.out.println("Running OCR for " + field.toString());
                                long startTime = System.currentTimeMillis();
                                long elapsedTime = System.currentTimeMillis() - startTime;
                                System.out.println("That took " + elapsedTime);
                            }
                            currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(imgSegs.get(box)));
                        }
                    }
                }
            }
        }
        else {
            currentFields.get(BikeField.TIME).setValue(SegmentRecogniser.recogniseInt(imgSegs.get(ScreenBox.LCD1)));
        }
        
        // Remove state information that's older than two complete blink cycles (2s) ago
        while (history.size() > 0) {
            if (currentTime.minus(4 * ApplicationConstants.BLINK_FREQ, ChronoUnit.MILLIS)
                           .isAfter(history.getFirst().addedTime)) {
                history.removeFirst();
            } else {
                break;
            }                
        }

        if (ApplicationConstants.DEBUG) {
            System.out.println("Current state snapshots:");
            for (StateTime s : history) {
                System.out.println(s.addedTime.get(ChronoField.MILLI_OF_DAY));
            }
            System.out.println();
        }

        // Store the new state, with the time we recognised at the moment
        history.add(new StateTime(currentTime, activeSegs, currentFields.get(BikeField.TIME).getValue()));

        // Update which LCDs we know are solid/blinking
        updateSolidBlinking(currentTime);
        updateTimeChanging();

        if (ApplicationConstants.DEBUG) {
            System.out.println("Current State:");
            System.out.println("Time Changing: " + isTimeChanging());
            for (ScreenBox box : ScreenBox.values()) {
                System.out.println(box.toString() + ": " + getBoxState(box).toString());
            }
            System.out.println();
        }

    }

    /**
     * Return the current bike state
     */
    public BikeScreen getState() {
        System.out.println();
        System.out.println("DETECTING CHANGE SCREEN STATE");

        BikeScreen newScreen = null;
        for (BikeScreen screen : screens.values()) {
            boolean inState = screen.isActiveScreen(this);

            if (inState) {
                newScreen = screen;
                break;
            }
        }
        if (newScreen != null)
            currentScreen = newScreen;
        else {
            if (ApplicationConstants.DEBUG) {
                System.out.println("Failed to recognise state.");
            }
        }

        System.out.println("Establishing bike state is " + currentScreen.getEnum().toString());
        System.out.println();

        return currentScreen;
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
                                   currentTime.minus(2 * ApplicationConstants.BLINK_FREQ, ChronoUnit.MILLIS)
                                              .isBefore(s.addedTime))
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

    public ConfigData getConfig() {
        return configData;
    }

    public ScreenNumber getFieldValue(BikeField field) {
        return currentFields.get(field);
    }

    public LCDState getBoxState(ScreenBox box) {
        return boxStates.get(box);
    }

    public boolean isTimeChanging() {
        return timeChanging;
    }

    // Returns true iff the given box is lit in the latest received image
    public boolean isBoxActiveNow(ScreenBox box) {
        return history.getLast().activeBoxes.contains(box);
    }
}
