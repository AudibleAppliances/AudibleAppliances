package uk.ac.cam.groupprojects.bravo.imageProcessing;

import java.awt.geom.Point2D;

/**
 * Holds the information about a box in a photo of the exercise bike screen
 *
 * @author Oliver Hope
 */
public class BoxInfo {
    private Point2D.Double mCorner;
    private double mWidth;
    private double mHeight;

    public BoxInfo(Point2D.Double corner, double width, double height) {
        mCorner = corner;
        mWidth = width;
        mHeight = height;
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
}
