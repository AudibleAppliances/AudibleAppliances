package uk.ac.cam.groupprojects.bravo.config;

import org.junit.Test;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ConfigException;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Tests methods in the ConfigData class
 *
 * @author Oliver Hope
 */
public class ConfigDataTest {

    /**
     * Tests if can parse a sample config file successfully. Fails with exception if not
     *
     * @throws IOException If can't read the file
     * @throws ConfigException If JSON is malformed (or incorrect parsing strategy)
     */
    @Test
    public void testParse() throws IOException, ConfigException {
        String configPath = URLDecoder.decode(getClass().getResource("/testConfig.json").getFile(), "UTF-8");
        ConfigData cd = new ConfigData(configPath);
    }
}
