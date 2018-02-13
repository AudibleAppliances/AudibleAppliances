package uk.ac.cam.groupprojects.bravo.imageProcessing;

import org.junit.Test;

import java.net.URLDecoder;

public class ImageSegmentsTest {

    @Test
    public void loadValidConfigTest() throws Exception {
        String configPath = URLDecoder.decode(getClass().getResource("/testConfig.json").getFile(), "UTF-8");
        ImageSegments segs = new ImageSegments(configPath);
    }

    @Test
    public void cropImageTest() {
        //TODO: Implement when have correct photos and config to use
    }
}
