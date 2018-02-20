package uk.ac.cam.groupprojects.bravo.imageProcessing;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

/**
 * Tests for the ImageSegments class
 *
 * @author Oliver Hope
 */
public class ImageSegmentsTest {

    /**
     * Tests to see that ImageSegments can correctly load a valid config file without failing
     *
     * @throws Exception
     */
    @Test
    public void loadValidConfigTest() throws Exception {
        String configPath = URLDecoder.decode(getClass().getResource("/testConfig.json").getFile(), "UTF-8");
        ImageSegments segs = new ImageSegments(configPath);
    }

    /**
     * Tests if ImageSegments is correctly cropping images using a config file
     *
     * @throws Exception
     */
    @Test
    public void cropImageTest() throws Exception {
        String configPath = URLDecoder.decode(getClass().getResource("/testConfig.json").getFile(), "UTF-8");
        ImageSegments segs = new ImageSegments(configPath);

        BufferedImage img = ImageIO.read(
                new File(URLDecoder.decode(getClass().getResource("/fullImage.jpg").getFile(), "UTF-8")));

        BufferedImage croppedImg = ImageIO.read(
                new File(URLDecoder.decode(getClass().getResource("/croppedImage.png").getFile(), "UTF-8")));

        BufferedImage testResult = segs.getImageBox(BoxType.SPEED, img);

        Assert.assertTrue(compareImages(croppedImg, testResult));
    }

    /**
     * Compares two images pixel by pixel for equality.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return Whether the images are identical.
     */
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // Image size must be the same
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            // Loop over every pixel.
            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    // Compare the pixels for equality.
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        }
        else {
            return false;
        }

        return true;
    }
}
