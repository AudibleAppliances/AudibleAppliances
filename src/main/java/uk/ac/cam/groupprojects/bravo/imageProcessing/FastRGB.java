package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by david on 03/03/2018.
 *
 * From https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
 */
public class FastRGB {

    private int width;
    private byte[] pixels;

    FastRGB(BufferedImage image) {
        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
    }

    int getRGB(int x, int y) {
        int pos = (y * 3 * width) + (x * 3);

        int argb = ((int) pixels[pos++] & 0xff); // blue
        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos++] & 0xff) << 16); // red
        return argb;
    }
}
