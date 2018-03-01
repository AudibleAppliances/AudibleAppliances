package uk.ac.cam.groupprojects.bravo.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import uk.ac.cam.groupprojects.bravo.imageProcessing.BoxInfo;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.ApplicationConstants;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Reads in and stores the details from a config file
 *
 * @author Oliver Hope
 */
public class ConfigData {

    private final String configPath;
    private HashMap<ScreenBox, BoxInfo> mBoxes = new HashMap<>();
    private HashMap<BikeField, Boolean> mSpokenFields = new HashMap<>();
    private String mVoice;

    /**
     * Loads the data in from config file to object
     *
     * @param configPath
     */
    public ConfigData(String configPath) throws ConfigException {
        this.configPath = configPath;
        parseConfig();
    }

    /**
     * Read in a config file to object fields
     *
     * @throws ConfigException if config file non-existent or malformed
     */
    private void parseConfig() throws ConfigException {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(configPath)));
            JsonParser parser = new JsonParser();

            JsonObject config = parser.parse(reader).getAsJsonObject();
            JsonObject boxes = config.getAsJsonObject("boxes");

            // Parse individual boxes
            for (ScreenBox type : ScreenBox.values()) {
                String typeName = type.name().toLowerCase();
                JsonObject box = boxes.getAsJsonObject(typeName);

                // Get box info
                double boxWidth = box.get("width").getAsDouble();
                double boxHeight = box.get("height").getAsDouble();
                JsonArray corner = box.getAsJsonArray("corner");
                double cornerX = corner.get(0).getAsDouble();
                double cornerY = corner.get(1).getAsDouble();

                // Create box info and place in data structure.
                BoxInfo newBox = new BoxInfo(type, new Point2D.Double(cornerX, cornerY), boxWidth, boxHeight);
                mBoxes.put(type, newBox);
            }

            // Parse voice
            JsonPrimitive voice = config.getAsJsonPrimitive("voice");
            mVoice = voice.getAsString();

            // Parse spoken_fileds
            JsonObject spoken_fields = config.getAsJsonObject("spoken_fields");

            for (BikeField type : BikeField.values()) {
                String typeName = type.name().toLowerCase();
                JsonPrimitive field = spoken_fields.getAsJsonPrimitive(typeName);
                mSpokenFields.put(type, field.getAsBoolean());
            }

        } catch(FileNotFoundException e){
            if ( ApplicationConstants.DEBUG )
                e.printStackTrace();
            throw new ConfigException("Could not read config file");
        } catch(JsonParseException e2){
            if ( ApplicationConstants.DEBUG )
                e2.printStackTrace();
            throw new ConfigException("Error parsing JSON");
        }
    }

    public String getVoice() {
        return mVoice;
    }

    public BoxInfo getBox(ScreenBox type) {
       return mBoxes.get(type);
    }

    public boolean isSpokenField(BikeField type) {
        return mSpokenFields.get(type);
    }
}
