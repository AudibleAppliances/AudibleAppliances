package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class SegmentActive {
    public static boolean segmentActive(BufferedImage img) throws IOException {
        return segmentActive(img, 0.03); // Default percentage lit up, empirically chosen
    }
    public static boolean segmentActive(BufferedImage img, double percent) throws IOException {
        // Use the 0th channel as our input should be black and white - threshold 0.5 for same reason
        return imageAverage(img, 0.5, 0) > percent;
    }

    public static double imageAverage(BufferedImage img, double threshold, int channel) {
        Raster raw = img.getRaster();

        double sum = 0;
        for (int y = 0; y < raw.getHeight(); y++) {
            for (int x = 0; x < raw.getWidth(); x++) {
                double pixelVal = raw.getSampleDouble(x, y, channel) / 255;

                if (pixelVal > threshold) {
                    sum += 1;
                }
            }
        }

        double n = raw.getWidth() * raw.getHeight();
        double average = sum / n;
        return average;
    }
}