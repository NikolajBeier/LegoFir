package org.example.mapping;

import org.example.utility.Geometry;
import org.opencv.core.Point;
import java.lang.Math;

public class RobotPosition {

    int x;
    int y;



    public int rightSideX;
    public int rightSideY;
    public int leftSideX;
    public int leftSideY;
    int frontSideX;
    int frontSideY;
    int backSideX;
    int backSideY;


    Point heading = new Point(0,0);

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

    public int getFrontSideX() {return frontSideX;}

    public void setFrontSideX(int frontSideX) {this.frontSideX = frontSideX;}

    public int getFrontSideY() {return frontSideY;}

    public void setFrontSideY(int frontSideY) {this.frontSideY = frontSideY;}

    public int getBackSideX() {return backSideX;}

    public void setBackSideX(int backSideX) {this.backSideX = backSideX;}

    public int getBackSideY() {return backSideY;}

    public void setBackSideY(int backSideY) {this.backSideY = backSideY;}
}
