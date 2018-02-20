package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SegmentRecogniser {
    public static int recogniseInt(String imagePath)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Integer.parseInt(recognise(imagePath));
    }

    public static float recogniseFloat(String imagePath)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Float.parseFloat(recognise(imagePath));
    }
    public static int recogniseInt(BufferedImage img) 
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Integer.parseInt(recognise(img));
    }

    public static float recogniseFloat(BufferedImage img)
                throws IOException, NumberFormatException, UnrecognisedDigitException {
        return Float.parseFloat(recognise(img));
    }

    public static String recognise(BufferedImage img) throws IOException, UnrecognisedDigitException {
        File f = null;
        try {
            f = saveFile(img);
            return recognise(f.getPath());
        }
        finally {
            if (f != null)
                f.delete();
        }
    }

    private static File saveFile(BufferedImage img) throws IOException {
        final String imgType = "png";

        File f = File.createTempFile("audible", imgType);
        ImageIO.write(img, imgType, f);
        return f;
    }

    public static String recognise(String imagePath) throws IOException, UnrecognisedDigitException {
        List<String> args = new ArrayList<>();
        args.add("ssocr"); // Program name
        args.add("-d -1"); // Autodetect the number of digits
        args.add("invert"); // Convert to monochrome then invert colours (required)
        args.add(imagePath);
        ProcessBuilder builder = new ProcessBuilder(args);

        Process p = builder.start();
        try (Scanner s = new Scanner(p.getInputStream())) {
            String output = s.nextLine();

            if (output.equals("-")) {
                throw new UnrecognisedDigitException("Failed to parse digit in file: " + imagePath);
            }

            return clean(output);
        }
        finally {
            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Flag this thread as having been interrupted
            }
        }
    }
    // Cleanup the output of the recogniser
    // Remove trailing decimals, trim whitespace
    private static String clean(String input) {
        return input.trim().replaceAll("\\.$", "");
    }
}