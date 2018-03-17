package uk.ac.cam.groupprojects.bravo.imageProcessing;

import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;
import uk.ac.cam.groupprojects.bravo.util.FastImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;

/**
 * Reads image, communicating with daemon process for locking so as not to corrupt writes.
 *
 * @author Oliver Hope
 */
public class ReadImage {

    private Socket socket;

    /**
     * Create an image reader, opening a socket to use over all reads
     *
     * @throws IOException Opening the socket fails
     */
    public ReadImage() throws IOException {

        if (ApplicationConstants.DEBUG) System.out.println("Opening initial socket");

        socket = new Socket("127.0.0.1", ApplicationConstants.DAEMON_PORT);
    }

    /**
     * Reads image from file, checking with daemon if ok to read.
     *
     * @return Image read
     * @throws IOException If there is a problem reading or communicating with daemon
     */
    public BufferedImage read(String imagePath) throws IOException {

        DataInputStream in = new DataInputStream(socket.getInputStream());
        OutputStream out = socket.getOutputStream();

        // Send REQ (And if socket closed try again)
        try {
            out.write(2);
        } catch (IOException e) {

            if (ApplicationConstants.DEBUG) System.out.println("Socket closed, opening new socket");

            socket = new Socket("127.0.0.1", ApplicationConstants.DAEMON_PORT);
            out.write(2);
        }

        // Wait for ACK
        if (in.readByte() == 1) {

            BufferedImage img = ImageIO.read(new File(imagePath));

            // Send DONE
            out.write(1);

            return img;
        }
        // Exception if incorrect reply
        else {
            throw new IOException("ReadImage: Received incorrect response from image server");
        }
    }
}
