package org.example.mapping;

import org.opencv.core.Point;
import java.lang.Math;

public class RobotPosition {

    int x;
    int y;
    Point heading;

    public RobotPosition() {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getHeading() {
        return heading;
    }

    public int getHeadingInDegrees() {
        return (int) Math.atan(heading.y/heading.x);
    }
}
