package org.example.mapping;

public class TennisBall {

    int x;

    int y;

    double closetsWall;

    boolean isInCorner;

    public TennisBall(int x, int y, double closetsWall, boolean isInCorner) {
        this.x = x;
        this.y = y;
        this.closetsWall=closetsWall;
        this.isInCorner = isInCorner;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getClosetsWall() {
        return closetsWall;
    }

    public boolean isInCorner() {
        return isInCorner;
    }

    public void setClosetsWall(double closetsWall) {
        this.closetsWall = closetsWall;
    }
}


