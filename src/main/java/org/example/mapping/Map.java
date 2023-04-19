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

    RobotPosition robotPosition = new RobotPosition();
    LinkedList<TennisBall> balls = new LinkedList<>();

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

    public TennisBall getNextBall() {



        // find the tennis ball closest to the robot
        TennisBall closestBall = new TennisBall(0,0);
        double closestDistance = Integer.MAX_VALUE;

        for(TennisBall tennisball : balls) {
            // Distance between two points:
            double distance = Math.sqrt((tennisball.x-getRobotPosition().x)*(tennisball.x-getRobotPosition().x)+(tennisball.y-getRobotPosition().y)*(tennisball.y-getRobotPosition().y));
            if(distance<closestDistance) {
                closestDistance = distance;
                closestBall = tennisball;
            }
        }


        return closestBall;
    }
}
