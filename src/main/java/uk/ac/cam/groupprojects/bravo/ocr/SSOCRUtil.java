package uk.ac.cam.groupprojects.bravo.ocr;

import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

public class SSOCRUtil {
    protected static final String IMG_TYPE = "png";

    public static File saveTempFile(BufferedImage img) throws IOException {
        File f = File.createTempFile("audible", "." + IMG_TYPE, ApplicationConstants.TMP_DIR);
        //File f = File.createTempFile("audible", "." + IMG_TYPE);
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

    public static BufferedImage threshold(BufferedImage image) throws IOException {
        // https://stackoverflow.com/questions/8368078/java-bufferedimage-to-iplimage
        ToMat iplConverter = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();

        Mat src = iplConverter.convertToMat(java2dConverter.convert(image));
        opencv_imgproc.cvtColor(src, src, opencv_imgproc.COLOR_BGR2HLS);
        Mat singleChannel = new Mat(src.size(), opencv_core.CV_8UC1);
        MatVector mv = new MatVector(src.channels());
        mv.put(1, singleChannel);
        opencv_core.split(src, mv);

        Mat dst = new Mat();
        opencv_imgproc.threshold(singleChannel, dst, 0, 255, opencv_imgproc.THRESH_OTSU);

        return java2dConverter.convert(iplConverter.convert(dst));
    }
}