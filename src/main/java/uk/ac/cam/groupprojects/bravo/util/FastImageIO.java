package uk.ac.cam.groupprojects.bravo.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.bytedeco.javacpp.opencv_core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

/**
 * Created by david on 03/03/2018.
 */
public class FastImageIO {

    public static void write(RenderedImage im, String format, File outputFile ) throws IOException {
        BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        ImageIO.write(im, format, imageOutputStream);
        imageOutputStream.close();

    }

    public static BufferedImage oldRead(File inputFile ) throws IOException {
        BufferedInputStream imageInputStream = new BufferedInputStream( new FileInputStream( inputFile ) );
        ImageIO.read(imageInputStream);
        return ImageIO.read( inputFile );
    }

    public static BufferedImage read(File inputFile ) throws IOException {
        Raster raster = readRaster( inputFile );
        BufferedImage dest = new BufferedImage(raster.getWidth(), raster.getHeight(), 5);
        dest.setData( raster );
        return dest;
    }

    public static Raster readRaster(File inputFile) throws IOException{
        FileChannel fileChannel = new RandomAccessFile(inputFile, "r").getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        byte[] imageBuffer = new byte[(int) inputFile.length()];
        buffer.get( imageBuffer );
        return decodeJPEG( imageBuffer );
    }

    private static Raster decodeJPEG(byte[] data) {
        // Create an InputStream from the compressed data array.
        ByteArrayInputStream jpegStream = new ByteArrayInputStream(data);

        // Create a decoder.
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(jpegStream);

        // Decode the compressed data into a Raster.
        Raster jpegRaster;
        try {
            jpegRaster = decoder.decodeAsBufferedImage().getWritableTile(0, 0);
        } catch (IOException ioe) {
            throw new RuntimeException("TIFFImage13");
        }

        // Translate the decoded Raster to the specified location and return.
        return jpegRaster.createTranslatedChild(0,0);
    }

}