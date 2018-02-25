package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

public class SSOCRUtil {
    protected static final String IMG_TYPE = "png";

    public static File saveTempFile(BufferedImage img) throws IOException {
        File f = File.createTempFile("audible", "." + IMG_TYPE, ApplicationConstants.TMP_DIR);
        return saveFile(img, f);
    }
    public static File saveFile(BufferedImage img, File f) throws IOException {
        ImageIO.write(img, IMG_TYPE, f);
        return f;
    }

    public static Process startSSOCR(List<String> args) throws IOException {
        List<String> pArgs = new ArrayList<>();
        pArgs.add("ssocr"); // Program name
        pArgs.addAll(args);

        ProcessBuilder builder = new ProcessBuilder(pArgs);

        return builder.start();
    }
    public static void waitFor(Process p) {
        try {
            p.waitFor();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static Process startRecognisingSSOCR(String inputPath) throws IOException {
        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add("-1"); // Autodetect the number of digits
        args.add("invert"); // Get black text on white background (required for SSOCR to work)
        args.add(inputPath);
        return startSSOCR(args);
    }

    public static BufferedImage makeMonochrome(BufferedImage image) throws IOException {
        File f = null;
        try {
            f = saveTempFile(image);
            makeMonochrome(f.getPath(), f.getPath());
            BufferedImage result = ImageIO.read(f);
            return result;
        }
        finally {
            if (f != null)
                f.delete();
        }
    }
    public static void makeMonochrome(String imagePath, String outputPath) throws IOException {
        List<String> args = new ArrayList<>();

        // Perform thresholding only on green channel. This means that white/green text is
        // saved, while the blue background is deleted.
        args.add("g_threshold");
        args.add("invert"); // Invert to get black text on a white background (as required by SSOCR)
        args.add("-o");
        args.add(outputPath);
        args.add(imagePath);

        Process p = startSSOCR(args);
        waitFor(p);
    }
}