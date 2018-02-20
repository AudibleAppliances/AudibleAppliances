package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.imageProcessing.CameraException;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.PiCamera;
import uk.ac.cam.groupprojects.bravo.model.menu.MenuScreen;
import uk.ac.cam.groupprojects.bravo.model.menu.ScreenEnum;
import uk.ac.cam.groupprojects.bravo.model.menu.SelectionScreen1;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.FestivalMissingException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

/**
 * Created by david on 13/02/2018.
 */
public class AudibleAppliances {

    private static Map<ScreenEnum, MenuScreen> screens = new HashMap<>();

    private static Synthesiser synthesiser;
    private static ImageSegments segments;
    private static MenuScreen currentScreen;
    private static BikeStateTracker bikeStateTracker;
    private static boolean running = false;

    private static int timeTracker = 0;

    public static void main(String[] args) {
        printHeader();

        //Load config

        try {
            /*
                We load the speech library first so that we can speak any errors
                out-loud. However if the speech library doesn't work we will need
                to play to pre-recorded sound clip.
             */
            System.out.println("Loading up speech library!");
            synthesiser = new Synthesiser();

            System.out.println("Loading in config from " + PATH_TO_CONFIG );
            //segments = new ImageSegments( PATH_TO_CONFIG );
            System.out.println("Config loaded successfully");
            System.out.println("Setting up required components");
            bikeStateTracker = new BikeStateTracker( segments );

            System.out.println("Components set up successfully!");

            running = true;

            //Need to initialise all of the screens
            screens.put( ScreenEnum.SELECTION_SCREEN_1, new SelectionScreen1() );

            //Created thread to track the bike
            Thread runThread = new Thread( runTracker );
            runThread.start();

            //Created thread to read from command line
            Thread inputThread = new Thread( handleInput );
            inputThread.setDaemon(true);
            inputThread.start();

        } catch( FestivalMissingException e ){
            if ( DEBUG )
                e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load voice library!");
            printFooter();
        } catch (Exception e) {
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

    static Runnable handleInput = () -> {
        try {
            System.out.println("Type 'exit' to close the application");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader( System.in ) );
            while ( running ){
                String input = reader.readLine();
                if ( input.trim().length() > 0 && input.compareToIgnoreCase("exit") == 0 ){
                    running = false;
                    System.out.println("ENDING Audible Appliances");
                }else {
                    System.out.println("Type 'exit' to close the application!");
                }
            }
            System.out.println("DONE1");
        }catch( IOException e ){
            if ( DEBUG )
                e.printStackTrace();
            System.out.println("FATAL ERROR: Cannot read from System.in");
            printFooter();
        }
    };

    static Runnable runTracker = () ->{
        System.out.println();
        System.out.println("Starting the bike state tracker! ");
        synthesiser.speak("Welcome to Audible Appliances");

        //FIRST RUN CODE
        /*
            We need to establish which screen we are in.
         */
        boolean initialScreenEstablished = false;
        if ( running ){
            while ( !initialScreenEstablished ){
                System.out.println("Establishing the state of the bike!");
                try {
                    bikeStateTracker.processNewImage( PiCamera.takeImage() );

                    float maxProb = 0.0f;
                    MenuScreen maxScreen = screens.get( ScreenEnum.SELECTION_SCREEN_1 );

                    for ( MenuScreen screen: screens.values() ){
                        float prob = screen.screenProbability( bikeStateTracker );
                        if ( prob > maxProb ){
                            maxProb = screen.screenProbability( bikeStateTracker );
                            maxScreen = screen;
                        }
                        System.out.println( screen.getEnum().toString() + " : " + prob );
                    }

                    System.out.println();

                    if ( maxProb > ApplicationConstants.minProb ){
                        currentScreen = maxScreen;
                        initialScreenEstablished = true;
                        System.out.println("Establishing bike state is " + currentScreen.getEnum().toString() );
                    }

                } catch (Exception e) {
                    if ( DEBUG )
                        e.printStackTrace();
                }
            }
        }

        while( running ){
            try {
                Thread.sleep( UPDATE_FREQ );
                timeTracker += UPDATE_FREQ;
            } catch (InterruptedException e) {
                if ( DEBUG )
                    e.printStackTrace();
            }

            try {
                bikeStateTracker.processNewImage( PiCamera.takeImage() );
            } catch (CameraException e) {
                if ( DEBUG )
                    e.printStackTrace();
            } catch (UnrecognisedDigitException e) {
                if ( DEBUG )
                    e.printStackTrace();
            } catch (IOException e) {
                if ( DEBUG )
                    e.printStackTrace();
            }

            if ( timeTracker == SPEAK_FREQ ){
                timeTracker = 0;
                bikeStateTracker.speakItems();
            }
        }
        synthesiser.speak("Goodbye! Hope to see you again soon!");
        synthesiser.close();
        printFooter();
    };

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
