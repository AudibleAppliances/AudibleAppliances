package uk.ac.cam.groupprojects.bravo.imageProcessing;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Paths;

public class imageCroppingTest {

    @Test
    public void testCrop() throws IOException {
        String imgPath = URLDecoder.decode(getClass().getResource("/lcd1.jpg").getFile(), "UTF-8");
        BufferedImage img = ImageIO.read(Paths.get(imgPath).toFile());
        IntelligentCropping.moreIntelligentCrop(img);

        //String writePath = "/Users/User/Desktop/lcd2-intellicrop.jpg";
        //ImageIO.write(img, "jpg", Paths.get(writePath).toFile());
    }
}
