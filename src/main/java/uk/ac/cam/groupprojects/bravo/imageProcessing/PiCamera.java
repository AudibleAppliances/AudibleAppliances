package uk.ac.cam.groupprojects.bravo.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

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
            String command = "raspistill -s -n -w 1707 -h 1280 -th none -q 85 -t 0 -o " + TMP_DIR_PATH + "/image.jpg";
            running = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            if ( DEBUG )
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

            if ( DEBUG )
                System.out.println("Time taken to take picture: " + elapsedTime );

            BufferedImage image = ImageIO.read(new File(TMP_DIR, "image.jpg"));
            elapsedTime = System.currentTimeMillis() - startTime;

            if ( DEBUG )
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
