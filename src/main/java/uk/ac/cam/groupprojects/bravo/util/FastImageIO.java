package uk.ac.cam.groupprojects.bravo.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by david on 03/03/2018.
 */
public class FastImageIO {

    public static void write(RenderedImage im, String format, File outputFile ) throws IOException {
        BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        ImageIO.write(im, format, imageOutputStream);
        imageOutputStream.close();

    }

    public static BufferedImage read(File inputFile ) throws IOException {
        FileChannel fileChannel = new RandomAccessFile(inputFile, "r").getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        byte[] imageBuffer = new byte[(int) inputFile.length()];
        buffer.get( imageBuffer );
        return decodeJPEG( imageBuffer );
    }

    private static BufferedImage decodeJPEG(byte[] data) {
        // Create an InputStream from the compressed data array.
        ByteArrayInputStream jpegStream = new ByteArrayInputStream(data);

        // Create a decoder.
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(jpegStream);

        // Decode the compressed data into a Raster.
        BufferedImage image;
        try {
            image = decoder.decodeAsBufferedImage();
        } catch (IOException ioe) {
            throw new RuntimeException("TIFFImage13");
        }

        // Translate the decoded Raster to the specified location and return.
        return image;
    }

}