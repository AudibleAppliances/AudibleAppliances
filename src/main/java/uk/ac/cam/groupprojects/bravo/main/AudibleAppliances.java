package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ReadImage;
import uk.ac.cam.groupprojects.bravo.model.menu.*;
import uk.ac.cam.groupprojects.bravo.ocr.UnrecognisedDigitException;
import uk.ac.cam.groupprojects.bravo.tts.FestivalMissingException;
import uk.ac.cam.groupprojects.bravo.tts.Synthesiser;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

/**
 * Created by david on 13/02/2018.
 */
public class AudibleAppliances {

    private static final Map<ScreenEnum, BikeScreen> screens = new HashMap<>();

    private static Synthesiser synthesiser;
    private static BikeScreen currentScreen;
    private static BikeStateTracker bikeStateTracker;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private static long lastSpeakTime = 0;

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

            running.set(true);

            addScreens(screens);

            //Created thread to track the bike
            Thread runThread = new Thread( runTracker );
            runThread.start();

            //Created thread to read from command line
            Thread inputThread = new Thread( handleInput );
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (running.get()) {
                String input = reader.readLine();
                if (input.trim().length() > 0 && input.compareToIgnoreCase("exit") == 0){
                    running.set(false);
                    System.out.println("ENDING Audible Appliances");
                    return;
                } else {
                    System.out.println("Type 'exit' to close the application!");
                }
            }
            System.out.println("DONE1");
        } catch (IOException e) {
            if (DEBUG)
                e.printStackTrace();
            System.out.println("FATAL ERROR: Cannot read from System.in");
            printFooter();
        }
    };

    static Runnable runTracker = () -> {
        System.out.println();
        System.out.println("Starting the bike state tracker! ");
        synthesiser.speak("Welcome to Audible Appliances");

        while(running.get()) {
            long loopStartTime = System.currentTimeMillis();

            try {
                Thread imageThread = new Thread( new ProcessImageThread( ReadImage.readImage( ApplicationConstants.IMAGE_PATH ) ) );
                imageThread.start();
            } catch ( IOException e) {
                if (DEBUG)
                    e.printStackTrace();
            }

            if (System.currentTimeMillis() - lastSpeakTime > currentScreen.getSpeakDelay()) {
                currentScreen.speakItems(bikeStateTracker, synthesiser);
                lastSpeakTime = System.currentTimeMillis();
            }

            long elapsedTime = System.currentTimeMillis() - loopStartTime;
            if (ApplicationConstants.DEBUG)
                System.out.println("That cycle took " + elapsedTime + "ms ");
        }

        synthesiser.speak("Goodbye! Hope to see you again soon!");

        printFooter();
        synthesiser.close();
    };

    public static class ProcessImageThread implements Runnable{

        private BufferedImage image;

        public ProcessImageThread( BufferedImage image ){
            this.image = image;
        }

        @Override
        public void run() {
            try {
                bikeStateTracker.updateState(image);
            } catch (IOException | UnrecognisedDigitException e) {
                if (DEBUG)
                    e.printStackTrace();
            }
        }
    }

    public static void addScreens( Map<ScreenEnum, BikeScreen> screens ){
        screens.put(ScreenEnum.OFF_SCREEN, new OffScreen());

        screens.put(ScreenEnum.ERROR_SCREEN, new ErrorScreen());
        screens.put(ScreenEnum.INITIAL_SCREEN, new InitialScreen());
        screens.put(ScreenEnum.RUNNING_SCREEN, new RunningScreen());
        screens.put(ScreenEnum.PAUSED_SCREEN, new PausedScreen());

        screens.put(ScreenEnum.TIME_SELECT, new TimeSelectScreen());
        screens.put(ScreenEnum.PROGRAM, new ProgramScreen());

        screens.put(ScreenEnum.SELECT_MANUAL, new SelectManualScreen());
        screens.put(ScreenEnum.SELECT_HRC, new SelectHRCScreen());
        screens.put(ScreenEnum.SELECT_USER_PROGRAM, new SelectUserProgramScreen());
        screens.put(ScreenEnum.SELECT_WATTS, new SelectWattScreen());
        screens.put(ScreenEnum.SELECT_PROGRAM, new SelectProgramScreen());
    }

    private static void detectState(){
        System.out.println();
        System.out.println("DETECTING CHANGE SCREEN STATE");

        BikeScreen bestScreen = screens.get(ScreenEnum.OFF_SCREEN);
        float bestScreenProb = 0;
        for (BikeScreen screen: screens.values()) {
            float prob = screen.screenActiveProbability(bikeStateTracker);

            if (prob > bestScreenProb) {
                bestScreen = screen;
                bestScreenProb = prob;
            }
        }
        currentScreen = bestScreen;

        System.out.println("Establishing bike state is " + currentScreen.getEnum().toString());
        System.out.println();
    }

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