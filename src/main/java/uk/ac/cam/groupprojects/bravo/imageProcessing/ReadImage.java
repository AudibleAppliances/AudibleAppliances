package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

/**
 * Reads image, communicating with daemon process to not corrupt writes.
 *
 * @author Oliver Hope
 */
public class ReadImage {

    /**
     * Reads image from file, checking with daemon if ok to read.
     *
     * @return Image read
     * @throws IOException If there is a problem reading or communicating with daemon
     */
    public static BufferedImage readImage( String imagePath ) throws IOException {

        Socket s = new Socket("127.0.0.1", ApplicationConstants.DAEMON_PORT);
        DataInputStream in = new DataInputStream(s.getInputStream());
        OutputStream out = s.getOutputStream();

        // Send REQ
        out.write(2);

        // Wait for ACK
        if (in.readByte() == 1) {

            if ( ApplicationConstants.DEBUG )
                System.out.println("Attempting to read image from: " + imagePath );

            BufferedImage img = ImageIO.read(new File(imagePath));

            // Send DONE
            out.write(1);
            s.close();

            if (ApplicationConstants.DEBUG )
                System.out.println("Returned image!");

            return img;
        }
        // Exception if incorrect reply
        else {
            s.close();
            throw new IOException();
        }
    }



}
