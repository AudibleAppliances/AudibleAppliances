package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.IntelligentCropping;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.util.FastImageIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

/**
 * Created by david on 02/03/2018.
 */
public class SplitImage {

    public static void main( String args[] ) throws Exception{
        String imagePath = "src/test/resources/input.jpg";

        long startTime = System.currentTimeMillis();
        BufferedImage img = ImageIO.read(new File( imagePath ));
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println( "Normal Time " + elapsedTime );

        startTime = System.currentTimeMillis();
        BufferedImage img1 = FastImageIO.read(new File( imagePath ));
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println( "Elasped Time " + elapsedTime );

        BufferedImage output = IntelligentCropping.intelligentCrop( img );

        startTime = System.currentTimeMillis();
        ImageIO.write( output, "jpg", new File( "output/output.jpg" ) );
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println( "Normal Time " + elapsedTime );

        startTime = System.currentTimeMillis();
        FastImageIO.write( output, "jpg", new File( "output/output1.jpg" ) );
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println( "Elasped Time " + elapsedTime );

        /*

        ConfigData cd = new ConfigData(configPath);
        ImageSegments segments = new ImageSegments(cd);

        //for ( ScreenBox box: ScreenBox.values() ){
            ScreenBox box = ScreenBox.LCD6;
            BufferedImage output = segments.getImageBox( box, img );

        //}

        */

    }

}
