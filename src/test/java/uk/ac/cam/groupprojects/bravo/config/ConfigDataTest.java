package uk.ac.cam.groupprojects.bravo.config;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ConfigException;

import java.io.IOException;
import java.net.URLDecoder;

public class ConfigDataTest {

    @Test
    public void testParse() throws IOException, ConfigException {
        String configPath = URLDecoder.decode(getClass().getResource("/testConfig.json").getFile(), "UTF-8");
        ConfigData cd = new ConfigData(configPath);
    }
}
