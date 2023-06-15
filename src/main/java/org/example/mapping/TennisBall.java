package org.example.mapping;

import org.example.robot.behaviour.DriveTowardsBall;

public class TennisBall {


    int x;

    int y;

    boolean isCloseToWall;
    DriveTowardsBall.Direction closetsWall;

    boolean isInCorner;

    public TennisBall(int x, int y, DriveTowardsBall.Direction closetsWall, boolean isInCorner, boolean isCloseTowall) {
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

    public DriveTowardsBall.Direction getClosetsWall() {
        return closetsWall;
    }


    public DriveTowardsBall.Direction getClosetswall(){
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


