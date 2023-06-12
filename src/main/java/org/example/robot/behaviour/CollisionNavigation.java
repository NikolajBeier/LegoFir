package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class CollisionNavigation {
    Legofir dude;
    private final double FRONTAL_AVOID_DISTANCE = 150;
    private final double TURNING_AVOID_DISTANCE = 60;
    private long startTime;
    MyBehavior myBehavior;
    public CollisionNavigation(Legofir dude,MyBehavior myBehavior){
        this.dude=dude;
        this.myBehavior=myBehavior;
    }


    public void avoidFrontCollision() {
        dude.moveBackward();
        while(isCollidingOnTheFront() && !isCollidingOnTheBack()){}
        dude.stopWheels();
        dude.turnRight();
        doNothingInMS(1500);
        dude.stopWheels();
        dude.moveForward();
        doNothingInMS(1500);
        dude.stopWheels();
    }



    public void avoidLeftCollision() {
        dude.moveBackward();
        while(isCollidingOnTheLeft() && !isCollidingOnTheBack()){}
        dude.stopWheels();
        dude.turnRight();
        startTime = System.currentTimeMillis();
        while(withinTimerinMS(2500,startTime) && !isCollidingOnTheRight()){}
        dude.stopWheels();
        dude.moveForward();
        doNothingInMS(1500);
        dude.stopWheels();
    }



    public void avoidRightCollision() {
        dude.moveBackward();
        while(isCollidingOnTheRight() && !isCollidingOnTheBack()){}
        dude.stopWheels();
        dude.turnLeft();
        startTime = System.currentTimeMillis();
        while(!isCollidingOnTheLeft() && withinTimerinMS(1500,startTime)){}
        dude.stopWheels();
        dude.moveForward();
        doNothingInMS(1500);
        dude.stopWheels();
    }

    public void avoidBackCollision() {
        dude.moveForward();
        while(isCollidingOnTheBack() && !isCollidingOnTheFront()){}
        dude.stopWheels();
        dude.turnLeft();
        doNothingInMS(1500);
        dude.stopWheels();
        dude.moveForward();
        doNothingInMS(1500);
        dude.stopWheels();
    }

    public boolean isCollidingOnTheBack() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point oppositeHeading = new Point(-heading.x, -heading.y);
        Point leftSide = new Point(dude.getMap().getRobotPosition().leftSideX, dude.getMap().getRobotPosition().leftSideY);
        Point rightSide = new Point(dude.getMap().getRobotPosition().rightSideX, dude.getMap().getRobotPosition().rightSideY);
        double distanceLeft = dude.getMap().distanceToEdge(oppositeHeading,leftSide);
        double distanceRight = dude.getMap().distanceToEdge(oppositeHeading,rightSide);
        if(distanceRight < FRONTAL_AVOID_DISTANCE || distanceLeft < FRONTAL_AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    public boolean isCollidingOnTheLeft() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        Point rightHeading = new Point(heading.y, -heading.x);
        Point frontSide = new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY());
        Point backSide = new Point(dude.getMap().getRobotPosition().getBackSideX(), dude.getMap().getRobotPosition().getBackSideY());
        double distanceFront = dude.getMap().distanceToEdge(leftHeading,frontSide);
        double distanceBack = dude.getMap().distanceToEdge(rightHeading,backSide);
        if(distanceBack < TURNING_AVOID_DISTANCE || distanceFront < TURNING_AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    public boolean isCollidingOnTheRight() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        Point rightHeading = new Point(heading.y, -heading.x);
        Point frontSide = new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY());
        Point backSide = new Point(dude.getMap().getRobotPosition().getBackSideX(), dude.getMap().getRobotPosition().getBackSideY());
        double distanceFront = dude.getMap().distanceToEdge(rightHeading,frontSide);
        double distanceBack = dude.getMap().distanceToEdge(leftHeading,backSide);
        if(distanceBack < TURNING_AVOID_DISTANCE || distanceFront < TURNING_AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    public boolean isCollidingOnTheFront() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftSide = new Point(dude.getMap().getRobotPosition().leftSideX, dude.getMap().getRobotPosition().leftSideY);
        Point rightSide = new Point(dude.getMap().getRobotPosition().rightSideX, dude.getMap().getRobotPosition().rightSideY);
        Point middle = new Point(dude.getMap().getRobotPosition().getX(), dude.getMap().getRobotPosition().getY());
        double distanceLeft = dude.getMap().distanceToEdge(heading,leftSide);
        double distanceRight = dude.getMap().distanceToEdge(heading,rightSide);
        double distanceMiddle= dude.getMap().distanceToEdge(heading,middle);
        if(distanceRight < FRONTAL_AVOID_DISTANCE || distanceLeft < FRONTAL_AVOID_DISTANCE || distanceMiddle < FRONTAL_AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    public void startAvoidingCollision() {
        if(isCollidingOnTheFront()){
            avoidFrontCollision();
        } else if(isCollidingOnTheLeft()){
            avoidLeftCollision();
        } else if(isCollidingOnTheRight()){
            avoidRightCollision();
        } else if(isCollidingOnTheBack()){
            avoidBackCollision();
        }
    }
    private void doNothingInMS(int i) {
        startTime = System.currentTimeMillis();
        while(withinTimerinMS(1500,startTime)){}
    }
    private boolean withinTimerinMS(int timer, long startTime) {
        return System.currentTimeMillis()-startTime<timer;
    }
}
