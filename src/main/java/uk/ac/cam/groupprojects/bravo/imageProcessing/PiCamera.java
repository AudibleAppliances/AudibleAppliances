package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Uses the Pi camera to to take images and load into java
 *
 * @author Oliver Hope
 */
public class PiCamera {

    private Process running;

    public PiCamera(){

    }

    public void setupCamera() throws CameraException{
        try {
            String command = "raspistill -s -n -w 1707 -h 1280 -th none -q 85 -t 0 -o /mnt/rd/image.jpg";
            running = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            if ( ApplicationConstants.DEBUG )
                e.printStackTrace();
        }

    }

    /**
     * Takes an image using the pi camera and puts it in a BufferedImage (using raspistill)
     *
     * @return image taken as a BufferedImage
     */
    public BufferedImage takeImageFile() throws CameraException{
        // Run command and get output
        try {

            long startTime = System.currentTimeMillis();

            String command = "pkill -SIGUSR1 raspistill";

            Runtime.getRuntime().exec(command);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to take picture: " + elapsedTime );

            BufferedImage image = ImageIO.read(new File("/mnt/rd/image.jpg"));
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to load picture: " + elapsedTime );

            return image;
        } catch (IOException e) {
            throw new CameraException("Error reading from camera");
        }
    }

    public void finish(){
        running.destroyForcibly();
    }


}
