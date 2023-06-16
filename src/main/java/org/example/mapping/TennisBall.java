package org.example.mapping;

public class TennisBall {


    int x;

    int y;

    boolean isCloseToWall;

    boolean isInCorner;

    public TennisBall(int x, int y, boolean isInCorner, boolean isCloseTowall) {
        this.x = x;
        this.y = y;
        this.isInCorner = isInCorner;
        this.isCloseToWall = isCloseTowall;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public boolean isInCorner() {
        return this.isInCorner;
    }


    public boolean isCloseToWall(){
        return this.isCloseToWall;
    }
    public void setCloseToWall(boolean closeTowall){
        this.isCloseToWall = closeTowall;
    }
    public void setInCorner(boolean inCorner) {
        isInCorner = inCorner;
    }
}


