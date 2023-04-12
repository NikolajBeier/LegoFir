package org.example.mapping;

import org.opencv.core.Point;

import java.util.LinkedList;

public class Map {

    int x;
    int y;
    int size;

    public RobotPosition getRobotPosition() {
        return robotPosition;
    }

    public LinkedList<TennisBall> getBalls() {
        return balls;
    }

    RobotPosition robotPosition;
    LinkedList<TennisBall> balls;

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = x*y;
    }

    public void addBallObject(TennisBall tennisball){
        balls.add(tennisball);
    }

    public void addBallCord(int x, int y){
        TennisBall tennisball = new TennisBall(x,y);
        balls.add(tennisball);
    }

    public void setRobotPosition(int x, int y, Point heading){
        robotPosition.x = x;
        robotPosition.y = y;
        robotPosition.heading = heading;
    }

    public void removeAllBalls() {
        balls.clear();
    }
}
