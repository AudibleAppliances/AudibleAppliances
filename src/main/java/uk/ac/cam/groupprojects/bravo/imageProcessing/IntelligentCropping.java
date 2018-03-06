package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.*;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;

// This could do with being improved
// Need to find the horizontal level at which the numbers actually start
// Alternatively, need to eliminate white from the top of the image when neighbours are white?
// A white-pixel flood-fill from the top of the image down would probably be the best approach

public class IntelligentCropping {

    /**
     * Crops unneeded lit edges from image that cause problems with the OCR.
     *
     * @param image
     */
    public static void intelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();

        Set<Point> visited = new HashSet<>();
        Set<Point> toOverwrite = new HashSet<>();
        Set<Point> toVisit = new HashSet<>();

        // Entire front row is initial frontier
        for (int i = 0; i < raw.getWidth(); i++) {
            toVisit.add(new Point(i, 0));
        }

        Point current;

        while (!toVisit.isEmpty()) {
            Iterator<Point> i = toVisit.iterator();
            current = i.next();
            i.remove();
            visited.add(current);
            if (!threshTest(raw, current)) {
                // If we don't need to remove this pixel, move on.
                // Don't consider its neighbours and don't overwrite it
                continue;
            }
            toOverwrite.add(current);

            for (Point neighbour : getNeighbours(current, raw)) {
                if (!visited.contains(neighbour)) {
                    toVisit.add(neighbour);
                }
            }
        }

        // If we're going to overwrite more than 50% of the image........ don't.
        int area = raw.getWidth() * raw.getHeight();
        if (toOverwrite.size() < area * 0.5) {
            for (Point p : toOverwrite) {
                raw.setPixel(p.x, p.y, new double[] { 0, 0, 0 });
            }
        }
    }

    /**
     * Given an image and point, returns a list of "reachable neighbours". ie adjacent pixel that need overwriting
     *
     * @param p
     * @param raw
     * @return
     */
    private static List<Point> getNeighbours(Point p, WritableRaster raw) {
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
    private static boolean threshTest(Raster raw, Point p) {
        // Based on empirical testing
        return raw.getSampleDouble(p.x, p.y, 1) > 160;
    }

}