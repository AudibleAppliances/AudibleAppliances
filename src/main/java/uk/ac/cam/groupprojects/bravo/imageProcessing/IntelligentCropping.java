package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.*;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;

public class IntelligentCropping {
    private static final double THRESHOLD = 150;
    private static final double BLACK_THRESHOLD = 20;

    private static final double SAFETY_HALT = 0.5; // If we're going to overwrite more of the image than this percent, don't

    /**
     * Crops unneeded lit edges from image that cause problems with the OCR.
     *
     * @param image
     */
    public static void intelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();

        int earlyStop = (int)(raw.getHeight() * raw.getWidth() * SAFETY_HALT);
        Set<Point> visited = new HashSet<>();
        Set<Point> toOverwrite = new HashSet<>();

        long start = System.currentTimeMillis();
        // Flood from the entire top row, with a reasonable threshold to detect light pixels
        for (int x = 0; x < raw.getWidth(); x++) {
            Set<Point> flooded = new HashSet<>();
            floodFill(raw, new Point(x, 0), THRESHOLD, visited, earlyStop, flooded);
            if (flooded.size() >= earlyStop) {
                return; // If we're going to overwrite too much, don't do it
            }
            toOverwrite.addAll(flooded);
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Initial pass: " + duration + "ms");

        // Actually overwrite the chosen pixels with black
        start = System.currentTimeMillis();
        blacken(raw, toOverwrite);
        duration = System.currentTimeMillis() - start;
        System.out.println("Initial pass: " + duration + "ms");

        // Run another flood fill, this time with a super low threshold to find isolated islands in the
        // sea of black overwritten pixels. Don't set an early stop, we want to flood as large as possible
        start = System.currentTimeMillis();
        visited = new HashSet<>();
        for (int x = 0; x < raw.getWidth(); x++) {
            for (int y = 0; y < raw.getHeight(); y++) {
                Point current = new Point(x, y);
                if (visited.contains(current))
                    continue;

                Set<Point> flooded = new HashSet<>();
                floodFill(raw, new Point(x, y), BLACK_THRESHOLD, visited, Integer.MAX_VALUE, flooded);
                if (flooded.size() >= earlyStop) {
                    continue; // If we've flooded a large area, we've got a significantly large island so don't remove it
                }

                blacken(raw, flooded);
            }
        }
        duration = System.currentTimeMillis() - start;
        System.out.println("Secondary pass: " + duration + "ms");
    }

    private static void blacken(WritableRaster raw, Set<Point> points) {
        for (Point p : points) {
            raw.setPixel(p.x, p.y, new double[] { 0, 0, 0 });
        }
    }
    
    private static void floodFill(Raster raw, Point start, double threshold, Set<Point> visited,
                                     int earlyStop, Set<Point> outputFlood) {
        Set<Point> frontier = new HashSet<>();
        frontier.add(start);

        Set<Point> flooded = new HashSet<>();
        while (!frontier.isEmpty()) {
            Iterator<Point> i = frontier.iterator();
            Point current = i.next();
            i.remove();

            visited.add(current);
            if (!isAboveThreshold(raw, current, threshold)) {
                // If we don't need to remove this pixel, move on.
                // Don't consider its neighbours and don't overwrite it
                continue;
            }

            flooded.add(current);
            if (flooded.size() > earlyStop) { // If we're going to overwrite too many pixels, stop
                break;
            }

            for (Point neighbour : getNeighbours(current, raw)) {
                if (!visited.contains(neighbour)) {
                    frontier.add(neighbour);
                }
            }
        }

        outputFlood.addAll(flooded);
    }

    /**
     * Given an image and point, returns a list of "reachable neighbours". ie adjacent pixel that need overwriting
     *
     * @param p
     * @param raw
     * @return
     */
    private static List<Point> getNeighbours(Point p, Raster raw) {
        List<Point> l = new ArrayList<>();
        int maxX = raw.getWidth();
        int maxY = raw.getHeight();

        if (p.x + 1 < maxX)
            l.add(new Point(p.x + 1, p.y));
        if (p.x - 1 >= 0)
            l.add(new Point(p.x - 1, p.y));
        if (p.y + 1 < maxY)
            l.add(new Point(p.x, p.y + 1));
        if (p.y - 1 >= 0)
            l.add(new Point(p.x, p.y - 1));

        return l;
    }

    /**
     * Given an point and an image, decides if it should be overwritten or not
     *
     * @param raw
     * @param p
     * @return
     */
    private static boolean isAboveThreshold(Raster raw, Point p, double threshold) {
        // Based on empirical testing
        return raw.getSampleDouble(p.x, p.y, 1) > threshold;
    }

}