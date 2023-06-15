package org.example.mapping;

public class TennisBall {


    int x;

    int y;

    boolean isCloseToWall;
    Map.Direction closetsWall;

    boolean isInCorner;

    public TennisBall(int x, int y, Map.Direction closetsWall, boolean isInCorner, boolean isCloseTowall) {
        this.x = x;
        this.y = y;
        this.closetsWall=closetsWall;
        this.isInCorner = isInCorner;
        this.isCloseToWall = isCloseTowall;

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
        return this.isInCorner;
    }

    public void setClosetsWall(Map.Direction closetsWall) {
        this.closetsWall = closetsWall;
    }

    public Map.Direction getClosetswall(){
        return this.closetsWall;
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


