package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ReadImage;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.FestivalMissingException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

/**
 * Created by david on 13/02/2018.
 */
public class AudibleAppliances {

    // Class necessary state
    private static Synthesiser synthesiser;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    // Running state
    private static BikeStateTracker bikeStateTracker;
    private static long lastSpeakTime = 0;
    private static ConfigData configData;
    private static BikeScreen currentScreen;

    public static void main(String[] args) {

        /***********************
         * Start up and checks *
         ***********************/

        printHeader();

        //Turn debug mode off and on
        DEBUG = args.length > 0 && args[0].compareToIgnoreCase("-d") == 0;

        try {
            /*
                We load the speech library first so that we can speak any errors
                out-loud. However if the speech library doesn't work we will need
                to play to pre-recorded sound clip.
             */
            System.out.println("Loading up speech library!");
            synthesiser = new Synthesiser();

            // Load config from json
            System.out.println("Loading in config from " + PATH_TO_CONFIG );
            configData = new ConfigData(PATH_TO_CONFIG);

            System.out.println("Config loaded successfully");
            System.out.println("Setting up required components");

            bikeStateTracker = new BikeStateTracker(configData, synthesiser);
            System.out.println("Components set up successfully!");

            running.set(true);

            /***********************
             * Start main run loop *
             ***********************/

            // Created thread to track the bike
            Thread runThread = new Thread( runTracker );
            runThread.start();

        } catch( FestivalMissingException e ){
            if ( DEBUG )
                e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load voice library!");
            printFooter();
        }catch (Exception e) {
            if ( DEBUG )
                e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load in config");
            if ( synthesiser != null ){
                synthesiser.speak("There was an error, please try again!");
                synthesiser.close();
            }
            printFooter();
        }
    }

    static Runnable runTracker = () -> {
        System.out.println();
        System.out.println("Starting the bike state tracker! ");
        synthesiser.speak("Welcome to Audible Appliances");

        int connectionAttempts = 0;
        while(running.get()) {
            if (DEBUG)
                System.out.println("Run while loop");
            try {

                long loopStartTime = System.currentTimeMillis();
                BufferedImage image = ReadImage.readImage( ApplicationConstants.IMAGE_PATH );
                long elapsedTime = System.currentTimeMillis() - loopStartTime;
                if (ApplicationConstants.DEBUG)
                    System.out.println("Time taken to time to take photo " + elapsedTime + "ms ");


                loopStartTime = System.currentTimeMillis();
                Thread imageThread = new Thread(new ProcessImageThread(image));
                imageThread.start();
                elapsedTime = System.currentTimeMillis() - loopStartTime;

                if (ApplicationConstants.DEBUG)
                    System.out.println("Time taken to spawn image process thread" + elapsedTime + "ms ");
            } catch (ConnectException e) {
                if (DEBUG) {
                    System.out.println("Failed to connect to image server.");
                }

                connectionAttempts++;
                if (connectionAttempts == ApplicationConstants.MAX_CONNECT_ATTEMPTS) {
                    synthesiser.speak("Failed to connect to the image server. Try rebooting the system.");

                    synthesiser.close();
                    return;
                }
            } catch (IOException e) {
                if (DEBUG)
                    e.printStackTrace();
            }
        }

        synthesiser.speak("Goodbye! Hope to see you again soon!");

        printFooter();
        synthesiser.close();
    };

    public static class ProcessImageThread implements Runnable {

        private BufferedImage image;

        public ProcessImageThread( BufferedImage image ){
            this.image = image;
        }

        @Override
        public void run() {
            try {
                if (ApplicationConstants.DEBUG)
                    System.out.println("ProcessImageThread process created");

                long loopStartTime = System.currentTimeMillis();

                // Segment image
                ImageSegments segmenter = new ImageSegments(configData);
                Map<ScreenBox, BufferedImage> imgSegs = new HashMap<>();
                for (ScreenBox box : ScreenBox.values()) {
                    imgSegs.put(box, segmenter.getImageBox(box, image));
                }
                if (ApplicationConstants.DEBUG)
                    System.out.println("Initialise the image segments");

                // Update tracker and then get current state
                bikeStateTracker.updateState(imgSegs);
                currentScreen = bikeStateTracker.getState();
                if (ApplicationConstants.DEBUG)
                    System.out.println("Updated BikeStateTracker");

                // Check if time to speak, and if yes then speak
                if (System.currentTimeMillis() - lastSpeakTime > currentScreen.getSpeakDelay()) {
                    currentScreen.speakItems(bikeStateTracker, synthesiser);
                    lastSpeakTime = System.currentTimeMillis();
                }
                if (ApplicationConstants.DEBUG)
                    System.out.println("Spoke");

                long elapsedTime = System.currentTimeMillis() - loopStartTime;

                if (ApplicationConstants.DEBUG)
                    System.out.println("Time taken to run image process thread (time to process photo) " + elapsedTime + "ms ");

            } catch (IOException | UnrecognisedDigitException e) {
                if (DEBUG)
                    e.printStackTrace();
            }
        }
    }

    /*******************************************
     * Pretty printing for command line output *
     *******************************************/

    private static void printHeader(){
        System.out.println("|----------------------------------------|");
        System.out.println("|----------------------------------------|");
        System.out.println("|----------------------------------------|");
        System.out.println("|---------- AUDIBLE APPLIANCES ----------|");
        System.out.println("|-------------- VERSION " + VERSION_NO + " -------------|");
        System.out.println("|------------- DEVELOPED BY: ------------|");
        System.out.println("|------------- Oliver Hope --------------|");
        System.out.println("|------------ Cameron Ramsay ------------|");
        System.out.println("|----------- Keith Collister ------------|");
        System.out.println("|------------ David Adeboye -------------|");
        System.out.println("|------------ Patryk Balicki ------------|");
        System.out.println("|------------ Tom Strudwick -------------|");
        System.out.println("|----------------------------------------|");
        System.out.println("|----------------------------------------|");
        System.out.println();
    }

    private static void printFooter(){
        System.out.println();
        System.out.println("|----------------------------------------|");
        System.out.println("|---------- AUDIBLE APPLIANCES ----------|");
        System.out.println("|----------------------------------------|");
    }
}