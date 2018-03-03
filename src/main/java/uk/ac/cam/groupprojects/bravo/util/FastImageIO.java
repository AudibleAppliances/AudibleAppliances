package uk.ac.cam.groupprojects.bravo.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

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
        BufferedInputStream imageInputStream = new BufferedInputStream( new FileInputStream( inputFile ) );
        ImageIO.read(imageInputStream);
        return ImageIO.read( inputFile );
    }

}