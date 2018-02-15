package uk.ac.cam.groupprojects.bravo.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Uses the Pi camera to to take images and load into java
 *
 * @author Oliver Hope
 */
public class PiCamera {

    /**
     * Takes an image using the pi camera and puts it in a BufferedImage (using raspistill)
     *
     * @return image taken as a BufferedImage
     */
    public static BufferedImage takeImage() throws CameraException {

        // Run command and get output
        try {
            String command = "raspistill -o -";
            Process child = Runtime.getRuntime().exec(command);

            InputStream in = child.getInputStream();
            BufferedImage image = ImageIO.read(in);
            in.close();

            return image;
        } catch (IOException e) {
            throw new CameraException("Error reading from camera");
        }
    }
}
