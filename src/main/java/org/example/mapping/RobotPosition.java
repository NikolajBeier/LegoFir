package org.example.mapping;

import org.opencv.core.Point;

public class RobotPosition {

    int x;
    int y;
    Point heading;

    public RobotPosition(int x, int y, Point heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
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
}
