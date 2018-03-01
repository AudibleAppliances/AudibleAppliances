package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;

import java.awt.image.BufferedImage;

/**
 *  Segments the image from processing using a config file. Also performs preprocessing (thresholding) to make the OCR
 *  more accurate
 *
 *  @author Oliver Hope
 */
public class ImageSegments {

    private ConfigData mConfigData;

    /**
     * Loads config file in to object
     *
     * @param config Config data to determine segments
     */
    public ImageSegments(ConfigData config) {
        mConfigData = config;
    }

    /**
     * Takes and image and the type of box we want and returns just that box from the image
     *
     * @param type BoxType that we want to crop
     * @param image Image of exercise bike screen
     * @return Crop of BoxType from image
     */
    public BufferedImage getImageBox(ScreenBox type, BufferedImage image) {
        BoxInfo box = mConfigData.getBox(type);

        // Calculate coordinates
        int x = (int) Math.round(box.getCorner().x/100.0 * image.getWidth());
        int y = (int) Math.round(box.getCorner().y/100.0 * image.getHeight());
        int w = (int) Math.round(box.getWidth()/100.0 * image.getWidth());
        int h = (int) Math.round(box.getHeight()/100.0 * image.getHeight());

        // Crop image and return
        return image.getSubimage(x, y, w, h);
    }
}
