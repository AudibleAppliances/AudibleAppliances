package uk.ac.cam.groupprojects.bravo.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 * Created by david on 03/03/2018.
 */
public class FastImageIO {

    private static final String JPGMime = "image/jpeg";

    public static void write(BufferedImage im, File outputFile ) throws IOException {
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

    private static BufferedImage decodeJPEG(byte[] data) throws IOException {
        // Create an InputStream from the compressed data array.
        ByteArrayInputStream jpegStream = new ByteArrayInputStream(data);

        ImageReader JPGReader;
        Iterator<ImageReader> JPGReaderIter =
                ImageIO.getImageReadersByMIMEType(JPGMime);
        if(JPGReaderIter.hasNext()) {
            JPGReader = JPGReaderIter.next();
            JPGReader.setInput(new
                    MemoryCacheImageInputStream(jpegStream));
            return JPGReader.read(0);
        }else {
            throw new IOException("Not a JPEG");
        }
    }

    private static byte[] encodeJPEG( BufferedImage data ){
        ByteArrayOutputStream jpegStream = new ByteArrayOutputStream();

        return null;
    }
}