package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.*;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;

// Beware - here be the dragons of optimisation

public class IntelligentCropping {
    private static final double THRESHOLD = 150;
    private static final double BLACK_THRESHOLD = 20;

    private static final double SAFETY_HALT = 0.5; // If we're going to overwrite more of the image than this percent, don't

    private static final double[] FILL_COLOUR = new double[] { 0, 0, 0 };

    /**
     * Crops unneeded lit edges from image that cause problems with the OCR.
     *
     * @param image
     */
    public static void intelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();

        // We treat the visited array as a 1D array of integers which map to the 2D pixel grid in the normal way:
        // i = width * y + x
        // Efficiency...
        int earlyStop = (int)(raw.getHeight() * raw.getWidth() * SAFETY_HALT);
        boolean[] visited = new boolean[raw.getHeight() * raw.getWidth()];
        Set<Integer> toOverwrite = new HashSet<>();

        // Flood from the entire top row, with a reasonable threshold to detect light pixels
        for (int x = 0; x < raw.getWidth(); x++) {
            Set<Integer> flooded = new HashSet<>();
            floodFill(raw, x, THRESHOLD, visited, earlyStop, flooded);
            if (flooded.size() >= earlyStop) {
                return; // If we're going to overwrite too much, don't do it
            }
            toOverwrite.addAll(flooded);
        }

        if (toOverwrite.isEmpty()) {
            return; // Don't need to do anything else
        }

        // Actually overwrite the chosen pixels with black
        blacken(raw, toOverwrite);

        // Create a sub-raster that forms a bounding rectangle of the area that we overwrote pixels in
        // We operate on this smaller raster in the second pass, as we can ignore anything that we didn't touch
        // in the first pass
        Rectangle bounds = getBounds(raw, toOverwrite);
        WritableRaster sub = raw.createWritableChild(bounds.x, bounds.y, bounds.width, bounds.height, 0, 0, null);
        earlyStop = (int)(sub.getWidth() * sub.getHeight() * SAFETY_HALT);

        // Run another flood fill, this time with a super low threshold to find isolated islands in the
        // sea of black overwritten pixels. Don't set an early stop, we want to flood as large as possible
        visited = new boolean[sub.getHeight() * sub.getWidth()];

        // Add all the points that we previously filled with black to the visited set, as we don't have to check them
        for (int p : toOverwrite) {
            int px = p % raw.getWidth() - bounds.x;
            int py = p / raw.getWidth() - bounds.y;
            visited[py * bounds.width + px] = true; // Translate by (-bounds.x, -bounds.y)
        }
        for (int y = 0; y < sub.getHeight(); y++) {
            for (int x = 0; x < sub.getWidth(); x++) {
                int current = y * sub.getWidth() + x;
                if (visited[current]) {
                    continue;
                }

                Set<Integer> flooded = new HashSet<>();
                floodFill(sub, current, BLACK_THRESHOLD, visited, Integer.MAX_VALUE, flooded);

                if (flooded.size() >= earlyStop) {
                    continue; // If we've flooded a large area, we've got a significantly large island so don't remove it
                }

                blacken(sub, flooded);
            }
        }
    }

    private static void blacken(WritableRaster raw, Set<Integer> points) {
        for (int p : points) {
            raw.setPixel(p % raw.getWidth(), p / raw.getWidth(), FILL_COLOUR);
        }
    }
    
    private static void floodFill(Raster raw, int start, double threshold, boolean[] visited,
                                  int earlyStop, Set<Integer> outputFlood) {
        Set<Integer> frontier = new HashSet<>();
        frontier.add(start);

        Set<Integer> flooded = new HashSet<>();
        while (!frontier.isEmpty()) {
            Iterator<Integer> i = frontier.iterator();
            int p = i.next();
            i.remove();

            visited[p] = true;
            if (!isAboveThreshold(raw, p, threshold)) {
                // If we don't need to remove this pixel, move on.
                // Don't consider its neighbours and don't overwrite it
                continue;
            }

            flooded.add(p);
            if (flooded.size() > earlyStop) { // If we're going to overwrite too many pixels, stop
                break;
            }

            // Compute neighbours. Done nastily here because it's faster than eg. using a helper function
            int px = p % raw.getWidth();
            int py = p / raw.getWidth();

            if (px + 1 < raw.getWidth() && !visited[p + 1])
                frontier.add(p + 1);
            if (px - 1 >= 0 && !visited[p - 1])
                frontier.add(p - 1);
            if (py + 1 < raw.getHeight() && !visited[p + raw.getWidth()])
                frontier.add(p + raw.getWidth());
            if (py - 1 >= 0 && !visited[p - raw.getWidth()])
                frontier.add(p - raw.getWidth());
        }

        outputFlood.addAll(flooded);
    }

    private static Rectangle getBounds(Raster raw, Set<Integer> points) {
        int x0 = Integer.MAX_VALUE, y0 = Integer.MAX_VALUE;
        int x1 = 0, y1 = 0; // The minimum allowed width/height are 1

        for (int p : points) {
            int px = p % raw.getWidth();
            int py = p / raw.getWidth();
            x0 = Math.min(px, x0);
            y0 = Math.min(py, y0);
            x1 = Math.max(px, x1);
            y1 = Math.max(py, y1);
        }

        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        return new Rectangle(x0, y0, width, height);
    }

    /**
     * Given an point and an image, decides if it should be overwritten or not
     *
     * @param raw
     * @param p
     * @return
     */
    private static boolean isAboveThreshold(Raster raw, Integer p, double threshold) {
        return raw.getSampleDouble(p % raw.getWidth(), p / raw.getWidth(), 1) > threshold;
    }
}