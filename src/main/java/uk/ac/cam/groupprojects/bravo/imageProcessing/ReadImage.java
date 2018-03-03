package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.util.FastImageIO;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
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
    public static BufferedImage readImage(String imagePath) throws IOException, ConnectException {
        if ( DEBUG )
            System.out.println("ReadImage: Opening a socket");

        try (Socket s = new Socket("127.0.0.1", DAEMON_PORT)) {
            DataInputStream in = new DataInputStream(s.getInputStream());
            OutputStream out = s.getOutputStream();

            // Send REQ
            if ( DEBUG )
                System.out.println("ReadImage: writing 2 to the socket");
            out.write(2);

            // Wait for ACK
            if ( DEBUG )
                System.out.println("ReadImage: waiting for ack");
            if (in.readByte() == 1) {

                if (DEBUG)
                    System.out.println("ReadImage: Attempting to read image from: " + imagePath );

                BufferedImage img = FastImageIO.read(new File(imagePath));

                // Send DONE
                out.write(1);

                if (DEBUG)
                    System.out.println("ReadImage: Returned image!");
                return img;
            }
            // Exception if incorrect reply
            else {
                throw new IOException("ReadImage: Received incorrect response from image server");
            }
        }
    }



}
