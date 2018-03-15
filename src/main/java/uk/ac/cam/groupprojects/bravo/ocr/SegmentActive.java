package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class SegmentActive {
    // "Threshold" is effectively the percentage (100% = 1.0) of the image that's white
    public static boolean segmentActive(BufferedImage img) throws IOException {
        return segmentActive(img, 0.1); // Default threshold, empirically chosen
    }
    public static boolean segmentActive(BufferedImage img, double threshold) throws IOException {
        return imageAverage(img) > threshold;
    }

    public static double imageAverage(BufferedImage img) throws IOException {
        Raster raw = img.getRaster();

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