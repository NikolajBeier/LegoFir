package org.example.mapping;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Map {

    int x;
    int y;
    int size;
    Edge edge = new Edge();

    public RobotPosition getRobotPosition() {
        return robotPosition;
    }

    public List<TennisBall> getBalls() {
        return balls;
    }

    RobotPosition robotPosition = new RobotPosition();
    List<TennisBall> balls = new ArrayList<>();

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = x*y;
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
        /*
        System.out.println("closest distance: "+closestDistance);
        System.out.println("robot x: "+getRobotPosition().x+" robot y: "+getRobotPosition().y);
        System.out.println("ball x: "+closestBall.x+" ball y: "+closestBall.y);

         */


        return closestBall;
    }

    public void setEdge(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, int height, int width) {
        edge.setAll(topLeft, topRight, bottomLeft, bottomRight, height, width);
    }

    public void setBalls(List<TennisBall> newList) {
        balls = newList;
    }
}
