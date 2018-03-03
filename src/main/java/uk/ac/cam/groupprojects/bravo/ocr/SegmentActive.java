package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class SegmentActive {
    // "Threshold" is effectively the percentage (100% = 1.0) of the image that's white
    public static boolean segmentActive(BufferedImage img) throws IOException {
        return segmentActive(img, 0.05); // 5% white is a good default threshold (empirical)
    }
    public static boolean segmentActive(BufferedImage img, double threshold) throws IOException {
        return imageAverage(img) > threshold;
    }

    public static double imageAverage(BufferedImage img) throws IOException {
        Raster raw = img.getRaster();

        final int bands = 3;

        double sum = 0;
        for (int y = 0; y < raw.getHeight(); y++) {
            for (int x = 0; x < raw.getWidth(); x++) {
                double pixelAvg = 0;
                for (int b = 0; b < bands; b++) {
                    pixelAvg += raw.getSampleDouble(x, y, b);
                }
                pixelAvg /= 3 * 255;

                if (pixelAvg > 0.4) {
                    sum += 1;
                }
            }
        }

        double n = raw.getWidth() * raw.getHeight();
        double average = sum / n;
        return average;
    }
}