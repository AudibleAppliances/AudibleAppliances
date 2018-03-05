package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
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
        long loopStartTime = System.currentTimeMillis();
        long elapsedTime, elapsedTime2, loopSubSetTime = System.currentTimeMillis();

        try (Socket s = new Socket("127.0.0.1", DAEMON_PORT)) {
            DataInputStream in = new DataInputStream(s.getInputStream());
            OutputStream out = s.getOutputStream();

            // Send REQ
            out.write(2);
            elapsedTime = System.currentTimeMillis() - loopStartTime;
            elapsedTime2 = System.currentTimeMillis() - loopSubSetTime;
            if (DEBUG)
                System.out.println("ReadImage: Time taken to time to write to socket " + elapsedTime + "ms (" + elapsedTime2 + ")" );
            loopSubSetTime = System.currentTimeMillis();

            //Wait for ACK
            if (in.readByte() == 1) {

                elapsedTime = System.currentTimeMillis() - loopStartTime;
                elapsedTime2 = System.currentTimeMillis() - loopSubSetTime;
                if (DEBUG)
                    System.out.println("ReadImage: Time taken to time to ack to socket " + elapsedTime + "ms (" + elapsedTime2 + ")");
                loopSubSetTime = System.currentTimeMillis();

                BufferedImage img = FastImageIO.read(new File(imagePath));
                elapsedTime = System.currentTimeMillis() - loopStartTime;
                elapsedTime2 = System.currentTimeMillis() - loopSubSetTime;
                if (DEBUG)
                    System.out.println("ReadImage: Time taken to time to read image " + elapsedTime + "ms (" + elapsedTime2 + ")" );
                loopSubSetTime = System.currentTimeMillis();
                // Send DONE


                elapsedTime = System.currentTimeMillis() - loopStartTime;
                elapsedTime2 = System.currentTimeMillis() - loopSubSetTime;
                out.write(1);
                if (DEBUG)
                    System.out.println("ReadImage: Time taken to write " + elapsedTime + "ms (" + elapsedTime2 + ")" );
                return img;
            }
            // Exception if incorrect reply
            else {
                throw new IOException("ReadImage: Received incorrect response from image server");
            }
        }
    }



}
