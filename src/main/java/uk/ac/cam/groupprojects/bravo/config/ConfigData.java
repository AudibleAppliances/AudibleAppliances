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
                BoxInfo newBox = new BoxInfo(new Point2D.Double(cornerX, cornerY), boxWidth, boxHeight);
                mBoxes.put(type, newBox);
            }

            // Calculate the PROGRAM box from LCD_TEXT_10 and LCD_TEXT_11
            BoxInfo text_10 = mBoxes.get(ScreenBox.LCD_TEXT_10);
            BoxInfo text_11 = mBoxes.get(ScreenBox.LCD_TEXT_11);

            double x = Math.min(text_10.getCorner().getX(), text_11.getCorner().getX());
            double y = Math.min(text_10.getCorner().getY(), text_11.getCorner().getY());
            double w = text_10.getWidth() + text_11.getWidth();
            double h = Math.max(text_10.getHeight(), text_11.getHeight());

            BoxInfo program = new BoxInfo(new Point2D.Double(x, y), w, h);
            mBoxes.put(ScreenBox.PROGRAM, program);


            // Parse voice
            JsonPrimitive voice = config.getAsJsonPrimitive("voice");
            mVoice = voice.getAsString();

            // Parse spoken_fields
            JsonObject spoken_fields = config.getAsJsonObject("spoken_fields");

            for (BikeField type : BikeField.values()) {
                //Load is the exception
                if (type != BikeField.LOAD) {
                    String typeName = type.name().toLowerCase();
                    JsonPrimitive field = spoken_fields.getAsJsonPrimitive(typeName);
                    mSpokenFields.put(type, field.getAsBoolean());
                } else {
                    mSpokenFields.put(BikeField.LOAD, false);
                }
            }

        } catch (FileNotFoundException e) {
            if (ApplicationConstants.DEBUG)
                e.printStackTrace();
            throw new ConfigException("Could not read config file");
        } catch (JsonParseException e2) {
            if (ApplicationConstants.DEBUG)
                e2.printStackTrace();
            throw new ConfigException("Error parsing JSON");
        } catch (NullPointerException e3) {
            if (ApplicationConstants.DEBUG)
                e3.printStackTrace();
            throw new ConfigException("Error parsing JSON");
        }
    }

    /**
     * The voice to use when speaking to the user
     *
     * @return name of voice to use
     */
    public String getVoice() {
        return mVoice;
    }

    /**
     * Gets the information for a box of particular type
     *
     * @param type ScreenBox to get information about
     * @return BoxInfo containing all information of the box
     */
    public BoxInfo getBox(ScreenBox type) {
       return mBoxes.get(type);
    }

    /**
     * Return whether that particular field is intended to be spoken to the user
     *
     * @param type BikeField to check whether spoken
     * @return Whether BikeField 'type' should be spoken to user
     */
    public boolean isSpokenField(BikeField type) {
        return mSpokenFields.get(type);
    }
}
