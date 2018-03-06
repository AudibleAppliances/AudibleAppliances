package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;;

// This could do with being improved
// Need to find the horizontal level at which the numbers actually start
// Alternatively, need to eliminate white from the top of the image when neighbours are white?
// A white-pixel flood-fill from the top of the image down would probably be the best approach

public class IntelligentCropping {
    private static final double THRESHOLD = 0.5; // Pixels with red component under 50% are removed
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
}