package uk.ac.cam.groupprojects.bravo.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

        // Build Command
        String command = "raspistill -o -";

        //TODO: check for errors in running command to give better error message

        // Run command and get output
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            BufferedImage image = ImageIO.read(p.getInputStream());
            p.getInputStream().close();

            return image;
        } catch (IOException e) {
            throw new CameraException("Error reading from camera");
        }
    }
}
