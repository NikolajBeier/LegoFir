package org.example.mapping;

import org.example.utility.Geometry;
import org.opencv.core.Point;
import java.lang.Math;

public class RobotPosition {

    int x;
    int y;



    int rightSideX;
    int rightSideY;
    int leftSideX;
    int leftSideY;

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

    public double getHeadingInRadians() {
        //System.out.println("heading x: " + heading.x + " heading y: " + heading.y);
        return Geometry.degreesOfVectorInRadians(heading.x,heading.y);
    }
    public void setRightSideX(int rightSideX) {
        this.rightSideX = rightSideX;
    }

    public void setRightSideY(int rightSideY) {
        this.rightSideY = rightSideY;
    }

    public void setLeftSideX(int leftSideX) {
        this.leftSideX = leftSideX;
    }

    public void setLeftSideY(int leftSideY) {
        this.leftSideY = leftSideY;
    }
}
