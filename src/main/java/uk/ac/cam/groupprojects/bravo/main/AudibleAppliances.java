package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.config.ConfigException;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.IntelligentCropping;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ReadImage;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.FestivalMissingException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

/**
 * Created by david on 13/02/2018.
 */
public class AudibleAppliances {

    // Running state
    private static BikeStateTracker bikeStateTracker;

    public static void main(String[] args) {

        /***********************
         * Start up and checks *
         ***********************/

        printHeader();

        //Turn debug mode off and on
        DEBUG = args.length > 0 && args[0].compareToIgnoreCase("-d") == 0;

        // Disable IO caching - this increases speed of writing to the ram disk
        ImageIO.setUseCache(false);

        Synthesiser synthesiser = null;
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
            ConfigData configData = new ConfigData(PATH_TO_CONFIG);
            ImageSegments segmenter = new ImageSegments(configData);


            System.out.println("Config loaded successfully");
            System.out.println("Setting up required components");

            bikeStateTracker = new BikeStateTracker(configData, synthesiser);
            System.out.println("Components set up successfully!");

            ReadImage readImage = new ReadImage();

            // Created thread to track the bike
            // Main entry point
            runApplication(synthesiser, segmenter, readImage);

        } catch(FestivalMissingException e) {
            e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load voice library!");
        } catch (ConfigException e) {
            e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load in config");
        } catch (Exception e) { // Catch global errors from the application
            e.printStackTrace();
            if (synthesiser != null) {
                synthesiser.speak("There was an error, please try again!");
            }
        }
        finally {
            if (synthesiser != null) {
                synthesiser.speak("Goodbye! Hope to see you again soon!");
                synthesiser.close();
            }
            printFooter();
        }
    }

    static void runApplication(Synthesiser synthesiser, ImageSegments segmenter, ReadImage readImage) throws IOException {
        System.out.println();
        System.out.println("Starting the bike state tracker! ");
        synthesiser.speak("Welcome to Audible Appliances");

        int connectionAttempts = 0;
        while (true) {
            if (DEBUG)
                System.out.println("Run while loop");

            try {
                BufferedImage image = readImage.read(ApplicationConstants.IMAGE_PATH);
                // Read successfully, reset the failed connection attempt counter
                connectionAttempts = 0;

                // Segment the image
                long start = System.currentTimeMillis();
                Map<ScreenBox, BufferedImage> imgSegs = new HashMap<>();
                for (ScreenBox box : ScreenBox.values()) {
                    BufferedImage boxImage = segmenter.getImageBox(box, image);

                    // Crop any bleed from LCDs above this one
                    IntelligentCropping.intelligentCrop(boxImage);

                    imgSegs.put(box, boxImage);
                }
                long elapsedTime = System.currentTimeMillis() - start;
                if (ApplicationConstants.DEBUG)
                    System.out.println("Time taken to segment the image: " + elapsedTime + "ms ");


                // Update tracker state
                start = System.currentTimeMillis();
                bikeStateTracker.updateState(imgSegs);
                elapsedTime = System.currentTimeMillis() - start;
                if (ApplicationConstants.DEBUG)
                    System.out.println("Time taken to update state: " + elapsedTime + "ms ");

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
            } catch (UnrecognisedDigitException e) {
                if (DEBUG)
                    e.printStackTrace();
            }
        }
    };

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