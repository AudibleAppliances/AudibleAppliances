package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import uk.ac.cam.groupprojects.bravo.ocr.SSOCRUtil;;

public class IntelligentCropping {
    private static final double THRESHOLD = 0.7; // Pixels with red component under 70% are removed
    private static final double MAX_DEPTH_PERCENT = 0.1; // Up to 10% can be cut out

    public static void intelligentCrop(BufferedImage image) {
        SSOCRUtil.assertImageBGR(image);

        WritableRaster raw = image.getRaster();

        final int HEIGHT_LIMIT = (int)(raw.getHeight() * MAX_DEPTH_PERCENT);

        for (int x = 0; x < raw.getWidth(); x++) {
            for (int y = 0; y < HEIGHT_LIMIT; y++) {
                double red = raw.getSampleDouble(x, y, 2);

                // If the pixel is background (< thresh), then there's no bleed from something above,
                // so we move onto the next column. Otherwise, we set the pixel to black
                if (red < THRESHOLD) {
                    break;
                } else {
                    raw.setPixel(x, y, new double[] { 0, 0, 0 });
                }
            }
        }
    }
}