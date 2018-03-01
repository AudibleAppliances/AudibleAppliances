package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ReadImage;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.FestivalMissingException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

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

    private static Map<ScreenEnum, BikeScreen> screens = new HashMap<>();

    private static Synthesiser synthesiser;
    private static BikeScreen currentScreen;
    private static BikeStateTracker bikeStateTracker;
    private static boolean running = false;

    private static int timeTracker = 0;

    public static void main(String[] args) {
        printHeader();

        //Turn debug mode off and on
        DEBUG = args.length > 0 && args[0].compareToIgnoreCase("-d") == 0;

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
            ConfigData configData = new ConfigData(PATH_TO_CONFIG);
            System.out.println("Config loaded successfully");
            System.out.println("Setting up required components");
            bikeStateTracker = new BikeStateTracker( new ImageSegments(configData) , configData);
            System.out.println("Components set up successfully!");

            running = true;

            //Need to initialise all of the screens
            screens.put( ScreenEnum.SELECTION_SCREEN_1, new SelectionScreen1() );
            screens.put( ScreenEnum.SELECTION_SCREEN_2, new SelectionScreen2() );
            screens.put( ScreenEnum.OFF_SCREEN, new StandbyScreen() );
            screens.put( ScreenEnum.CYCLING_SCREEN, new CyclingScreen() );

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
        int initialScreenCounter = 0;
        long startTime, elapsedTime;
        if ( running ){
            System.out.println("Establishing the state of the bike!");
            synthesiser.speak("Please hold while we try and establish the state of the exercise bike");
            while ( !initialScreenEstablished ){
                startTime = System.currentTimeMillis();
                System.out.println("Try " + initialScreenCounter );
                try {
                    bikeStateTracker.updateState(ReadImage.readImage());

                    float maxProb = 0.0f;
                    BikeScreen maxScreen = screens.get( ScreenEnum.SELECTION_SCREEN_1 );

                    for ( BikeScreen screen: screens.values() ){
                        float prob = screen.screenProbability( bikeStateTracker );
                        if ( prob > maxProb ){
                            maxProb = screen.screenProbability( bikeStateTracker );
                            maxScreen = screen;
                        }
                        if ( DEBUG )
                            System.out.println( screen.getEnum().toString() + " : " + prob );
                    }

                    System.out.println();

                    if ( maxProb > ApplicationConstants.MIN_PROB ){
                        currentScreen = maxScreen;
                        initialScreenEstablished = true;
                        System.out.println("Establishing bike state is " + currentScreen.getEnum().toString() );
                    }

                } catch (Exception e) {
                    if ( DEBUG )
                        e.printStackTrace();
                }
                initialScreenCounter++;
                if ( initialScreenCounter > ApplicationConstants.MAX_INITIAL_TRIES ){
                    //Default to SELECTION_SCREEN_1
                    currentScreen = screens.get( ScreenEnum.SELECTION_SCREEN_1 );
                    initialScreenEstablished = true;
                    System.out.println(" INITIAL SCREEN COUNTER LIMIT REACHED defaulting to " + currentScreen.getEnum().toString() );
                }
                try {
                    Thread.sleep( INITIAL_FREQ );
                    timeTracker += INITIAL_FREQ;
                } catch (InterruptedException e) {
                    if ( DEBUG )
                        e.printStackTrace();
                }

                //In case they type exit during this stage
                if ( !running )
                    initialScreenEstablished = true;

                elapsedTime = System.currentTimeMillis() - startTime;
                if ( ApplicationConstants.DEBUG )
                    System.out.println("That cycle took " + elapsedTime + "ms ");

            }

            //In case they type exit during this stage
            if ( running )
                synthesiser.speak("State established!");
        }
        while( running ){
            startTime = System.currentTimeMillis();
            try {
                Thread.sleep( UPDATE_FREQ );
                timeTracker += UPDATE_FREQ;
            } catch (InterruptedException e) {
                if ( DEBUG )
                    e.printStackTrace();
            }

            try {
                bikeStateTracker.updateState(ReadImage.readImage());
            } catch (UnrecognisedDigitException | IOException e) {
                if ( DEBUG )
                    e.printStackTrace();
            }

            if ( timeTracker == currentScreen.getSpeakDelay() ) {
                timeTracker = 0;
                currentScreen.speakItems( bikeStateTracker, synthesiser );
            }
            detectChangeState();

            elapsedTime = System.currentTimeMillis() - startTime;
            if ( ApplicationConstants.DEBUG )
                System.out.println("That cycle took " + elapsedTime + "ms ");

        }

        synthesiser.speak("Goodbye! Hope to see you again soon!");

        printFooter();
        synthesiser.close();
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

    /**
     * This function detects if the screen has changed.
     */
    private static void detectChangeState(){
        System.out.println();
        System.out.println("DETECTING CHANGE SCREEN STATE");

        float maxProb = 0.0f;
        BikeScreen maxScreen = currentScreen;

        for ( BikeScreen screen: screens.values() ){
            float prob = screen.screenProbability( bikeStateTracker );
            if ( prob > maxProb ){
                maxProb = screen.screenProbability( bikeStateTracker );
                maxScreen = screen;
            }
            if ( DEBUG )
                System.out.println( screen.getEnum().toString() + " : " + prob );
        }

        if ( maxProb > ApplicationConstants.MIN_PROB ){
            System.out.println("Establishing bike state is " + maxScreen.getEnum().toString() );
            currentScreen = maxScreen;
        }

        System.out.println();
    }
}