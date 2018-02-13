package uk.ac.cam.groupprojects.bravo.main;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

/**
 * Created by david on 13/02/2018.
 */
public class AudibleAppliances {

    private static boolean VERBOSE = false;

    public static void main(String[] args) {
        if ( args.length > 0 && args[0] == "v" ){
            VERBOSE = true;
        }
        printHeader();

        //Load config
        System.out.println("Loading in config...");
        /*try {
            ImageSegments segments = new ImageSegments( PATH_TO_CONFIG );

        } catch (ConfigException e) {
            if ( DEBUG )
                e.printStackTrace();
            System.out.println("FATAL ERROR: Could not load in file");
            printFooter();
        } */
    }

    private static void printHeader(){
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("---------- AUDIBLE APPLIANCES ----------");
        System.out.println("-------------- VERSION " + VERSION_NO + " -------------");
        System.out.println("------------- DEVELOPED BY: ------------");
        System.out.println("------------- Oliver Hope --------------");
        System.out.println("------------ Cameron Ramsay ------------");
        System.out.println("----------- Keith Collister ------------");
        System.out.println("------------ David Adeboye -------------");
        System.out.println("------------ Patryk Balicki ------------");
        System.out.println("------------ Tom Strudwick -------------");
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println();
    }

    private static void printFooter(){
        System.out.println("----------------------------------------");
        System.out.println("---------- AUDIBLE APPLIANCES ----------");
        System.out.println("----------------------------------------");
    }

}
