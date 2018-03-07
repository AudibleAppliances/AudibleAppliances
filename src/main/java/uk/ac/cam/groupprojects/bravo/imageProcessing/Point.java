package uk.ac.cam.groupprojects.bravo.imageProcessing;

public class Point {
    public int x;
    public int y;

    private int containingRectWidth;

    public Point(int x, int y, int containingRectWidth) {
        this.x = x;
        this.y = y;
        this.containingRectWidth = containingRectWidth;
    }

    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point)o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return y * containingRectWidth + x;
    }
}