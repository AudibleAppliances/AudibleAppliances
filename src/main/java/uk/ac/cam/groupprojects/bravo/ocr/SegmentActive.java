package uk.ac.cam.groupprojects.bravo.ocr;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;

public class SegmentActive {
    public static boolean segmentActive(BufferedImage img) {
        return segmentActive(img, 500);
    }
    public static boolean segmentActive(BufferedImage img, double threshold) {
        return imageVariance(img) > threshold;
    }

    public static double imageVariance(BufferedImage img) {
        BufferedImage grey = makeGreyscale(img);
        Raster raw = grey.getRaster();

        double sum = 0;
        double squareSum = 0;
        for (int y = 0; y < raw.getHeight(); y++) {
            for (int x = 0; x < raw.getHeight(); x++) {
                double pixel = raw.getSampleDouble(x, y, 0);
                sum += pixel;
                squareSum += pixel * pixel;
            }
        }

        double n = grey.getWidth() * grey.getHeight();
        double average = sum / n;
        double variance = (squareSum / n) - average * average;

        return variance;
    }
    
    private static BufferedImage makeGreyscale(BufferedImage img) {
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = out.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return out;
    }
}