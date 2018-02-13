package uk.ac.cam.groupprojects.bravo.imageProcessing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ImageSegments {

    private String mConfigPath;
    private HashMap<BoxType, BoxInfo> mBoxes = new HashMap<>();

    public ImageSegments(String config) throws ConfigException {
        mConfigPath = config;
        readConfig();
    }

    private void readConfig() throws ConfigException {

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(mConfigPath)));
            JsonParser parser = new JsonParser();

            JsonObject config = parser.parse(reader).getAsJsonObject();
            JsonObject boxes = config.getAsJsonObject("boxes");

            // Parse individual boxes
            for (BoxType type : BoxType.values()) {
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

        } catch (FileNotFoundException e) {
            throw new ConfigException("Could not read config file");
        }
    }

    public BufferedImage getImageBox(BoxType type, BufferedImage image) {
        BoxInfo box = mBoxes.get(type);

        // Calculate coordinates
        int x = (int) Math.round(box.getCorner().x * image.getWidth());
        int y = (int) Math.round(box.getCorner().y * image.getHeight());
        int w = (int) Math.round(box.getWidth() * image.getWidth());
        int h = (int) Math.round(box.getHeight() * image.getHeight());

        // Crop image and return
        return image.getSubimage(x, y, w, h);
    }
}
