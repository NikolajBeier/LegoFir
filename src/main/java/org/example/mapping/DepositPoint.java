package org.example.mapping;

import org.opencv.core.Point;

public class DepositPoint {
    Point centerLeft=new Point(0,0);
    Point centerRight=new Point(0,0);
    Point leftExitTopRight=new Point(0,0);
    Point leftExitBottomLeft=new Point(0,0);
    Point rightExitTopRight=new Point(0,0);
    Point rightExitBottomLeft=new Point(0,0);
    Edge edge;

    public DepositPoint(Edge edge) {
        this.edge = edge;
    }

    public void calcExit() {
        double xright = 0;
        double yright = 0;
        double xleft = 0;
        double yleft = 0;

        Point edgepoint = this.edge.getTopRight();
        Point edgepoint2 = this.edge.getBottomRight();

        xright = (edgepoint.x+edgepoint2.x)/2;
        yright = (edgepoint.y+edgepoint2.y)/2;

        edgepoint = this.edge.getTopLeft();
        edgepoint2 = this.edge.getBottomLeft();
        xleft = (edgepoint.x+edgepoint2.x)/2+20;
        yleft = (edgepoint2.y+edgepoint.y)/2;
        this.centerRight = new Point(xright, yright);
        this.centerLeft = new Point(xleft, yleft);


    }

    public void setCoords() {
        Point temp = this.centerRight;
        this.rightExitBottomLeft = new Point(temp.x - 5, temp.y - 5);
        this.rightExitTopRight = new Point(temp.x + 5, temp.y + 5);
        temp = this.centerLeft;
        this.leftExitTopRight = new Point(temp.x+5,temp.y+5);
        this.leftExitBottomLeft = new Point(temp.x-5,temp.y-5);

    }

    public Point getLeftExitBottomLeft() {
        return leftExitBottomLeft;
    }

    public Point getRightExitBottomLeft() {
        return rightExitBottomLeft;
    }

    public Point getLeftExitTopRight() {
        return leftExitTopRight;
    }

    public Point getRightExitTopRight() {
        return rightExitTopRight;
    }

    public Point getCenterRight() {
        return centerRight;
    }
    public Point getCenterLeft() {
        return centerLeft;
    }
}

