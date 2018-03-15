package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.Scanner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SegmentRecogniser {
    public static int recogniseInt(String imagePath, double threshold)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        String s = recognise(imagePath, threshold);

        s = s.replaceAll("[^0-9]", "");
        return Integer.parseInt(s);
    }
    public static int recogniseInt(BufferedImage img, double threshold)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        String s = recognise(img, threshold);

        s = s.replaceAll("[^0-9]", "");
        return Integer.parseInt(s);
    }

    public static String recognise(BufferedImage img, double threshold) throws IOException, UnrecognisedDigitException {
        File f = null;
        try {
            f = SSOCRUtil.saveTempFile(img);
            return recognise(f.getPath(), threshold);
        }
        finally {
            if (f != null)
                f.delete();
        }
    }

    public static String recognise(String imagePath, double threshold) throws IOException, UnrecognisedDigitException {
        Process p = SSOCRUtil.startRecognisingSSOCR(imagePath, threshold);

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