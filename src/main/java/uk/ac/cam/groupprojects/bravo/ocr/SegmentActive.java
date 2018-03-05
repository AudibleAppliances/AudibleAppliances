package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;

public class SegmentActive {
    // "Threshold" is effectively the percentage (100% = 1.0) of the image that's white
    public static boolean segmentActive(ScreenBox box, BufferedImage img) throws IOException {
        return segmentActive(box, img, 0.05); // 5% white is a good default threshold (empirical)
    }
    public static boolean segmentActive(ScreenBox box, BufferedImage img, double threshold) throws IOException {
        return imageAverage(box, img) > threshold;
    }

    public static double imageAverage(ScreenBox box, BufferedImage img) throws IOException {
        BufferedImage grey = SSOCRUtil.threshold(box, img);
        Raster raw = grey.getRaster();

        double sum = 0;
        for (int y = 0; y < raw.getHeight(); y++) {
            for (int x = 0; x < raw.getWidth(); x++) {
                double pixelVal = raw.getSampleDouble(x, y, 0) / 255;

                if (pixelVal > 0.4) {
                    sum += 1;
                }
            }
        }

        double n = raw.getWidth() * raw.getHeight();
        double average = sum / n;
        return average;
    }
}