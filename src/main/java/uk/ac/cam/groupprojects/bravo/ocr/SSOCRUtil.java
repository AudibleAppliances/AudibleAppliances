package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

public class SSOCRUtil {
    protected static final String IMG_TYPE = "jpg";

    public static File saveTempFile(BufferedImage img) throws IOException {
        File f = File.createTempFile("audible", "." + IMG_TYPE, ApplicationConstants.TMP_DIR);
        //File f = File.createTempFile("audible", "." + IMG_TYPE);
        return saveFile(img, f);
    }
    public static File saveFile(BufferedImage img, File f) throws IOException {
        ImageIO.write(img, IMG_TYPE, f);

        return f;
    }

    public static BufferedImage resize(BufferedImage img, double percent) {
        int newWidth = (int)Math.round(img.getWidth() * percent);
        int newHeight = (int)Math.round(img.getHeight() * percent);

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        BufferedImage out = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = out.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();

        return out;
    }

    public static Process startSSOCR(List<String> args) throws IOException {
        List<String> pArgs = new ArrayList<>();
        pArgs.add("ssocr"); // Program name
        pArgs.addAll(args);

        ProcessBuilder builder = new ProcessBuilder(pArgs);
        builder.redirectErrorStream(true); // Merge stderr and stdout

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

    public static Process startRecognisingSSOCR(String inputPath, double threshold) throws IOException {
        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add("-1"); // Autodetect the number of digits

        args.add("-t"); // Threshold at 80% "white"
        args.add(String.valueOf(threshold));

        args.add("-T");

        args.add("invert"); // Get black text on white background (required for SSOCR to work)
        args.add("remove_isolated"); // Remove odd pixels
        args.add(inputPath);
        return startSSOCR(args);
    }

    protected static final double THRESHOLD = 160; // Empirically decided

    // This method is only suitable for ROUGH thresholding - for checking if a segment is active, for example.
    // It's not suitable for producing low-noise images for recognition with SSOCR
    public static BufferedImage roughThreshold(BufferedImage image) throws IOException {
        assertImageBGR(image);

        // https://stackoverflow.com/questions/8368078/java-bufferedimage-to-iplimage
        ToMat iplConverter = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();

        // Convert input image to an OpenCV Matrix
        Mat src = iplConverter.convertToMat(java2dConverter.convert(image));
        // Extract only one channel - input image is BGR, extract only the R component
        Mat singleChannel = new Mat(src.size(), opencv_core.CV_8UC1);
        MatVector mv = new MatVector(src.channels());
        mv.put(1, singleChannel);
        opencv_core.split(src, mv);

        // Threshold the resulting image using the scaled average lightness
        Mat dst = new Mat();

        opencv_imgproc.threshold(singleChannel, dst, THRESHOLD, 255, opencv_imgproc.THRESH_BINARY);

        // Convert the result back into a BufferedImage
        return java2dConverter.convert(iplConverter.convert(dst));
    }

    public static void assertImageBGR(BufferedImage image) {
        if (image.getType() != 5) {
            System.out.println("Input image type is " + image.getType() + ", not 5 (BGR) - developer error");
            Thread.dumpStack();
            System.exit(-42);
        }
    }
}