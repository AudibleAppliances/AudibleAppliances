package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.Scanner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SegmentRecogniser {
    public static int recogniseInt(String imagePath)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Integer.parseInt(recognise(imagePath));
    }
    public static int recogniseInt(BufferedImage img) 
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Integer.parseInt(recognise(img));
    }

    public static float recogniseFloat(String imagePath)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Float.parseFloat(recognise(imagePath));
    }
    public static float recogniseFloat(BufferedImage img)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Float.parseFloat(recognise(img));
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