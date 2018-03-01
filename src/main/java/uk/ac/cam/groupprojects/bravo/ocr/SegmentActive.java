package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class SegmentActive {
    // "Threshold" is effectively the percentage (100% = 1.0) of the image that's blue
    public static boolean segmentActive(BufferedImage img) throws IOException {
        return segmentActive(img, 0.05); // 5% blue is a good default threshold (empirical)
    }
    public static boolean segmentActive(BufferedImage img, double threshold) throws IOException {
        return imageAverage(img) > threshold;
    }

    public static double imageAverage(BufferedImage img) throws IOException {
        BufferedImage grey = SSOCRUtil.makeMonochrome(img);
        Raster raw = grey.getRaster();

        double sum = 0;
        for (int y = 0; y < raw.getHeight(); y++) {
            for (int x = 0; x < raw.getWidth(); x++) {
                sum += raw.getSampleDouble(x, y, 0);
            }
        }
        sum /= 255.0; // Pixel values are 0-255 (not 0-1, even though they're as doubles)

        double n = raw.getWidth() * raw.getHeight();
        double average = sum / n;
        return 1.0 - average; // Make black = 0.0: it's more intuitive
    }
}