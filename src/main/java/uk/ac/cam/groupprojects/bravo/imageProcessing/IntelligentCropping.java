package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.*;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;;

// This could do with being improved
// Need to find the horizontal level at which the numbers actually start
// Alternatively, need to eliminate white from the top of the image when neighbours are white?
// A white-pixel flood-fill from the top of the image down would probably be the best approach

public class IntelligentCropping {
    private static final double THRESHOLD = 0.95; // Pixels with red component under 50% are removed
    private static final double MAX_DEPTH_PERCENT = 0.1; // Up to 10% can be cut out

    public static void intelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();

        final int HEIGHT_LIMIT = (int)(raw.getHeight() * MAX_DEPTH_PERCENT);

        for (int y = 0; y < HEIGHT_LIMIT; y++) {
            boolean trim = false;
            for (int x = 0; x < raw.getWidth(); x++) {
                double red = raw.getSampleDouble(x, y, 2) / 255;

                if (red > THRESHOLD) {
                    trim = true;  
                    break;
                }
            }
            // If we found a pixel over the threshold on this row, remove it
            if (trim) {
                for (int x = 0; x < raw.getWidth(); x++) {
                    raw.setPixel(x, y, new double[] { 0, 0, 0 });
                }
            } else {
                // If we haven't found any over-threshold pixels on this row, stop scanning down
                break;
            }
        }
    }

    /**
     * Crops unneeded lit edges from image that cause problems with the OCR.
     *
     * @param image
     */
    public static void moreIntelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();
        int maxX = raw.getWidth();
        int maxY = raw.getHeight();

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        // Entire front row is initial frontier
        for (int i=0; i<raw.getWidth(); i++){
            queue.add(new Point(i,0));
            visited.add(new Point(i, 0));
        }

        Point current;

        while (!queue.isEmpty()) {
            current = queue.poll();

            for (Point neighbour : getNeighbours(current, maxX, maxY, raw)) {
                if (!visited.contains(neighbour)) {
                    queue.offer(neighbour);
                    visited.add(neighbour);
                }
            }
        }

        // TODO: If not too many selected, overwrite visited pixels
        //if (visited.size() < 1000000000) {
            for (Point p : visited) {
                raw.setPixel(p.x, p.y, new double[] { 0, 0, 0 });
            }
        //}
    }

    private static List<Point> getNeighbours(Point p, int maxX, int maxY, WritableRaster raw) {
        List<Point> l = new ArrayList<>();

        if (p.x+1 <  maxX && threshTest(raw, p)) l.add(new Point(p.x+1, p.y));
        if (p.x-1 >= 0    && threshTest(raw, p))    l.add(new Point(p.x-1, p.y));
        if (p.y+1 <  maxY && threshTest(raw, p)) l.add(new Point(p.x, p.y+1));
        if (p.y-1 >= 0    && threshTest(raw, p))    l.add(new Point(p.x, p.y-1));

        return l;
    }

    private static boolean threshTest(Raster raw, Point p) {
        return raw.getSampleDouble(p.x, p.y, 2) > 250;
    }

}