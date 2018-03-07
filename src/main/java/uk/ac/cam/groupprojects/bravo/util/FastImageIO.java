package uk.ac.cam.groupprojects.bravo.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 * Created by david on 03/03/2018.
 */
public class FastImageIO {

    public static void write(BufferedImage im, String format, File outputFile ) throws IOException {
        FileChannel fileChannel = new RandomAccessFile(outputFile, "rw").getChannel();
        DataBufferByte bufferByte = (DataBufferByte) im.getRaster().getDataBuffer();
        ByteBuffer buffer = MappedByteBuffer.wrap( bufferByte.getData() );
        buffer.flip();
        fileChannel.write( buffer );
        fileChannel.close();
    }


    public static BufferedImage read(File inputFile) throws IOException {
        FileChannel fileChannel = new RandomAccessFile(inputFile, "r").getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        byte[] imageBuffer = new byte[(int) inputFile.length()];
        buffer.get( imageBuffer );
        fileChannel.close();
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

    private static byte[] encodeJPEG( BufferedImage data ){
        ByteArrayOutputStream jpegStream = new ByteArrayOutputStream();

        return null;
    }

}