package org.example.mapping;

public class TennisBall {


    int x;

    int y;

    Map.direction closetsWall;

    boolean isInCorner;

    public TennisBall(int x, int y, Map.direction closetsWall, boolean isInCorner) {
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

    public Map.direction getClosetsWall() {
        return closetsWall;
    }

    public boolean isInCorner() {
        return isInCorner;
    }

    public void setClosetsWall(Map.direction closetsWall) {
        this.closetsWall = closetsWall;
    }

    public void setInCorner(boolean inCorner) {
        isInCorner = inCorner;
    }
}


