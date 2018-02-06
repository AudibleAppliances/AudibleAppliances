import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PiCamera {

    /**
     * Takes an image using the pi camera and puts it in a BufferedImage (using raspistill)
     *
     * @param options raspistill options
     * @return image taken as a BufferedImage
     */
    public static BufferedImage takeImage() throws IOException {

        // Build Command
        String command = "raspistill";


        // Run command and get output
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            BufferedImage image = ImageIO.read(p.getInputStream());
            p.getInputStream().close();

            return image;
        } catch (IOException e) {
            throw new IOException("Error reading from camera");
        }
    }
}
