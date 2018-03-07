package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.*;

public class IntelligentCropping {
    private static final double THRESHOLD = 200;
    private static final double BLACK_THRESHOLD = 20;

    private static final double SAFETY_HALT = 0.5; // If we're going to overwrite more of the image than this percent, don't
    private static final double SECOND_SAFETY_HALT = 0.1; // In the second pass, use a lower safety

    private static final double[] FILL_COLOUR = new double[] { 0, 0, 0 };

    /**
     * Crops unneeded lit edges from image that cause problems with the OCR.
     *
     * @param image
     */
    public static void intelligentCrop(WritableRaster raw) {
        int earlyStop = (int)(raw.getHeight() * raw.getWidth() * SAFETY_HALT);
        boolean[][] visited = new boolean[raw.getHeight()][raw.getWidth()];

        // Keep track of the sets of points that we explored and flooded, and the point we explored but chose not
        // to flood - this 2nd set is used in the second pass
        Set<Point> flooded = new HashSet<>();
        Set<Point> notFlooded = new HashSet<>();

        Set<Point> frontier = new HashSet<>();
        for (int x = 0; x < raw.getWidth(); x++) {
            frontier.add(new Point(x, 0));
        }
        for (Point p : frontier) {
            if (visited[p.y][p.x])
                continue;

            floodFill(raw, p, THRESHOLD, visited, earlyStop, flooded, notFlooded);
        }

        if (flooded.size() >= earlyStop)
            return; // Don't overwrite more than a safe amount of the image
        if (flooded.isEmpty()) {
            return; // Don't need to do anything else
        }

        // Actually overwrite the chosen pixels with black
        blacken(raw, flooded);

        // Create a sub-raster that forms a bounding rectangle of the area that we overwrote pixels in
        // We operate on this smaller raster in the second pass, as we can ignore anything that we didn't touch
        // in the first pass
        Rectangle bounds = getBounds(raw, flooded);
        WritableRaster sub = raw.createWritableChild(bounds.x, bounds.y, bounds.width, bounds.height, 0, 0, null);
        earlyStop = (int)(sub.getWidth() * sub.getHeight() * SECOND_SAFETY_HALT);

        // Add all the points that we previously filled with black to the visited set, as we don't have to check them
        visited = new boolean[sub.getHeight()][sub.getWidth()];
        for (Point p : flooded) {
            p.translate(-bounds.x, -bounds.y);
            visited[p.y][p.x] = true;
        }

        // Run another flood fill, this time with a super low threshold to find isolated islands in the
        // sea of black overwritten pixels. Don't set an early stop, we want to flood as large as possible

        // Iterate over the edges of what we flooded in the first pass
        flooded = new HashSet<>();
        for (Point p : notFlooded) {
            p.translate(-bounds.x, -bounds.y);
            flooded.add(p); // Always flood the edge of the existing flooded area (1-pixel border)
            if (!sub.getBounds().contains(p) || visited[p.y][p.x]) {
                continue;
            }

            HashSet<Point> island = new HashSet<>();
            floodFill(sub, p, BLACK_THRESHOLD, visited, Integer.MAX_VALUE, island, null);

            if (island.size() < earlyStop) {
                flooded.addAll(island); // Only flood the island if it's small enough
            }

        }

        // Overwrite all the islands
        blacken(sub, flooded);
    }

    private static void floodFill(Raster raw, Point start, double threshold, boolean[][] visited,
                                  int earlyStop, final Set<Point> outputFlood, final Set<Point> outputNotFlooded) {
        Set<Point> frontier = new HashSet<>();
        frontier.add(start);

        Set<Point> flooded = new HashSet<>();
        Set<Point> notFlooded = new HashSet<>();
        while (!frontier.isEmpty()) {
            Iterator<Point> i = frontier.iterator();
            Point p = i.next();
            i.remove();

            visited[p.y][p.x] = true;
            if (!isAboveThreshold(raw, p, threshold) && outputNotFlooded != null) {
                // If we don't need to remove this pixel, move on.
                // Don't consider its neighbours and don't overwrite it
                notFlooded.add(p);
                continue;
            }

            flooded.add(p);
            if (flooded.size() > earlyStop) { // If we're going to overwrite too many pixels, stop
                break;
            }

            // Compute neighbours. Done nastily here because it's faster than eg. using a helper function
            if (p.x + 1 < raw.getWidth() && !visited[p.y][p.x + 1])
                frontier.add(new Point(p.x + 1, p.y));
            if (p.x - 1 >= 0 && !visited[p.y][p.x - 1])
                frontier.add(new Point(p.x - 1, p.y));
            if (p.y + 1 < raw.getHeight() && !visited[p.y + 1][p.x])
                frontier.add(new Point(p.x, p.y + 1));
            if (p.y - 1 >= 0 && !visited[p.y - 1][p.x])
                frontier.add(new Point(p.x, p.y - 1));
        }

        if (outputFlood != null)
            outputFlood.addAll(flooded);
        if (outputNotFlooded != null)
            outputNotFlooded.addAll(notFlooded);
    }

    private static void blacken(WritableRaster raw, Set<Point> points) {
        for (Point p : points) {
            raw.setPixel(p.x, p.y, FILL_COLOUR);
        }
    }

    private static Rectangle getBounds(Raster raw, Set<Point> points) {
        int x0 = Integer.MAX_VALUE, y0 = Integer.MAX_VALUE;
        int x1 = 1, y1 = 1; // The minimum allowed width/height are 1

        for (Point p : points) {
            x0 = Math.min(p.x, x0);
            y0 = Math.min(p.y, y0);
            x1 = Math.max(p.x, x1);
            y1 = Math.max(p.y, y1);
        }

        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        // Try to increase the height by 1 - this removes an edge case where there's a "barrier" between
        // two otherwise connected light areas at the bottom of the cropped image
        return new Rectangle(x0, y0, width, Math.min(height + 1, raw.getHeight()));
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