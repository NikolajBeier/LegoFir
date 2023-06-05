package org.example.mapping;

import org.opencv.core.Point;

public class Obstacle {
    Point topPoint= new Point(0,0);
    Point bottomPoint= new Point(0,0);
    Point leftPoint= new Point(0,0);
    Point rightPoint= new Point(0,0);

    public Point getTopPoint() {
        return topPoint;
    }

    public void setTopPoint(Point topPoint) {
        this.topPoint = topPoint;
    }

    public Point getBottomPoint() {
        return bottomPoint;
    }

    public void setBottomPoint(Point bottomPoint) {
        this.bottomPoint = bottomPoint;
    }

    public Point getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(Point leftPoint) {
        this.leftPoint = leftPoint;
    }

    public Point getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Point rightPoint) {
        this.rightPoint = rightPoint;
    }

    public Obstacle() {

    }


}
