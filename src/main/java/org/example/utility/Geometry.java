package org.example.utility;

import org.opencv.core.Point;

import java.awt.geom.Line2D;

public class Geometry {

    public static double distanceBetweenPoints(Point a, Point b){
        double x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y;
        double distance = Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
        return distance;
    }

    /**
     * Returns the angle of a vector in radians. Range -pi to pi
     * @param x
     * @param y
     * @return
     */
    public static double degreesOfVectorInRadians(double x, double y){
        return Math.atan2(y,x);
    }
    public static Point intersection(Line2D a, Line2D b){
        // Two points on each line
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();

        // Denominator is the difference in slope
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // If they are the same return null.
        if (d == 0) {
            return null;
        }

        // Intersection point
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point(xi, yi);
    }
}
