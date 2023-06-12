package org.example.mapping;

public class TennisBall {


    int x;

    int y;
    double distanceToClosestWall;
    Map.Direction closetsWall;

    boolean isInCorner;

    public TennisBall(int x, int y, Map.Direction closetsWall, boolean isInCorner) {
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

    public Map.Direction getClosetsWall() {
        return closetsWall;
    }

    public boolean isInCorner() {
        return isInCorner;
    }

    public void setClosetsWall(Map.Direction closetsWall) {
        this.closetsWall = closetsWall;
    }
    public void setDistanceToClosestWall(double distanceToClosestWall) {
        this.distanceToClosestWall = distanceToClosestWall;
    }
    public double getDistanceToClosestWall() {
        return distanceToClosestWall;
    }
    public void setInCorner(boolean inCorner) {
        isInCorner = inCorner;
    }
}


