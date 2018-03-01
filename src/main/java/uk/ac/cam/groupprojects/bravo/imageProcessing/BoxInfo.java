package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.geom.Point2D;

/**
 * Holds the information about a box in a photo of the exercise bike screen
 *
 * @author Oliver Hope
 */
public class BoxInfo {

    private ScreenBox mType;
    private Point2D.Double mCorner;
    private double mWidth;
    private double mHeight;

    public BoxInfo(ScreenBox type, Point2D.Double corner, double width, double height) {
        mType = type;
        mCorner = corner;
        mWidth = width;
        mHeight = height;
        new Point2D.Double(1.0,2.0);
    }

    public double getWidth() {
        return mWidth;
    }

    public double getHeight() {
        return mHeight;
    }

    public Point2D.Double getCorner() {
        return mCorner;
    }

    public ScreenBox getType() {
        return mType;
    }
}
