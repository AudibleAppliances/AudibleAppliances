package uk.ac.cam.groupprojects.bravo.main;

import uk.ac.cam.groupprojects.bravo.config.ConfigData;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ImageSegments;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

/**
 * Created by david on 02/03/2018.
 */
public class SplitImage {

    public static void main( String args[] ) throws Exception{
        String configPath = "src/test/resources/testConfig.json";
        String imagePath = "src/test/resources/test.jpg";
        File file = new File("src/test/resources");
        System.out.println( file.length() );
        BufferedImage img = ImageIO.read(new File( imagePath ));
        ConfigData cd = new ConfigData(configPath);
        ImageSegments segments = new ImageSegments(cd);

        //for ( ScreenBox box: ScreenBox.values() ){
            ScreenBox box = ScreenBox.LCD1;
            BufferedImage output = segments.getImageBox( box, img );
            ImageIO.write( output, "jpg", new File( "output/" + box.toString().toLowerCase() + ".jpg" ) );
        //}

    }

}
