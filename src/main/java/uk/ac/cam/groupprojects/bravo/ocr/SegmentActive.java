package uk.ac.cam.groupprojects.bravo.ocr;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class SegmentActive {
    // "Threshold" is effectively the percentage (100% = 1.0) of the image that's white
    public static boolean segmentActive(BufferedImage img) throws IOException {
        return segmentActive(img, 0.01); // 1% white is a good default threshold (empirical)
    }
    public static boolean segmentActive(BufferedImage img, double threshold) throws IOException {
        double ave = imageAverage(img);
        boolean result = ave > threshold;
        if (ApplicationConstants.DEBUG) {
            System.out.println("Threshold: "+threshold);
            System.out.println("Average:   "+ave);
            System.out.println("RESULT:    "+result);
        }
        return result;
    }

    public static double imageAverage(BufferedImage img) throws IOException {
        BufferedImage grey = SSOCRUtil.roughThreshold(img);
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