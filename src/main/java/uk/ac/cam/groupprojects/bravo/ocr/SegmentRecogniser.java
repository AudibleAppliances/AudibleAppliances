package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.Scanner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SegmentRecogniser {
    public static int recogniseInt(String imagePath)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        String s = recognise(imagePath);

        s = s.replaceAll("[^0-9]", "");
        return Integer.parseInt(s);
    }
    public static int recogniseInt(BufferedImage img) 
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        String s = recognise(img);

        s = s.replaceAll("[^0-9]", "");
        return Integer.parseInt(s);
    }

    public static String recognise(BufferedImage img) throws IOException, UnrecognisedDigitException {
        File f = null;
        try {
            f = SSOCRUtil.saveTempFile(img);
            return recognise(f.getPath());
        }
        finally {
            if (f != null)
                f.delete();
        }
    }

    public static String recognise(String imagePath) throws IOException, UnrecognisedDigitException {
        Process p = SSOCRUtil.startRecognisingSSOCR(imagePath);

        try (Scanner s = new Scanner(p.getInputStream())) {
            String output = s.nextLine();
            System.out.println("Got \"" + output +"\" from SSOCR");

            if (output.equals("-")) {
                throw new UnrecognisedDigitException("Failed to parse digit in file: " + imagePath);
            }

            return clean(output);
        }
        finally {
            SSOCRUtil.waitFor(p);
        }
    }
    // Cleanup the output of the recogniser
    // Remove trailing decimals, trim whitespace
    private static String clean(String input) {
        return input.trim().replaceAll("\\.$", "");
    }
}