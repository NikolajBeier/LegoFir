package org.example.mapping;

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

    public double getHeadingInDegrees() {
        //System.out.println("math Atan: "+Math.atan(heading.y/heading.x));
        //System.out.println("math toDegrees: "+Math.toDegrees(Math.atan(heading.y/heading.x)));
        return Math.atan2(heading.y,heading.x);
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
