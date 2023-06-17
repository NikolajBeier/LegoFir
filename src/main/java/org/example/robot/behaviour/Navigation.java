package org.example.robot.behaviour;

import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.robot.model.RobotState;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;


public class Navigation {
    double currentAngle=0;
    Legofir dude;
    double distanceToPoint=0;
    double angleToNextPoint=0;
    MyBehavior myBehavior;
    public Navigation(Legofir dude, MyBehavior myBehavior) {
        this.dude=dude;
        this.myBehavior= myBehavior;
    }

    public void turnTowards(TennisBall nextPoint) {

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        int nextPointX = nextPoint.getX();
        int nextPointY = nextPoint.getY();

        currentAngle = dude.getAngle();
        distanceToPoint = distanceBetweenPoints(new Point(currentPosition.getFrontSideX(), currentPosition.getFrontSideY()), new Point(nextPointX, nextPointY));

        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
        Point Pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor..x.
        angleToNextPoint = Geometry.degreesOfVectorInRadians(Pointvector.x, Pointvector.y);

        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading()) {
                turnLeftTowardsPoint();
            } else {
                turnRightTowardsPoint();
            }
        }
    }


    public void turnsTowardsWayPoint(Point nextPoint) {

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        int nextPointX = (int) nextPoint.x;
        int nextPointY = (int) nextPoint.y;

        currentAngle = dude.getAngle();
        Point pointVector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());


        distanceToPoint = distanceBetweenPoints(new Point(currentPosition.getFrontSideX(), currentPosition.getFrontSideY()), new Point(nextPointX, nextPointY));

        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
       // Point pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor...
        angleToNextPoint = Geometry.degreesOfVectorInRadians(pointVector.x, pointVector.y);


        //if current angle is not close to angle to next ball
        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading()) {
                turnLeftTowardsPoint();
            } else {
                turnRightTowardsPoint();
            }
        }
    }

    public void turnCheeksTowardsGoal(Point goal, boolean suppressed){

        /*
         int nextPointX = (int) goal.x;
        int nextPointY = (int) goal.y;
        Point pointVector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        angleToNextPoint = Geometry.degreesOfVectorInRadians(pointVector.x, pointVector.y);
        double oppositeAngleToNextPoint=0;
        if (angleToNextPoint>0){
            oppositeAngleToNextPoint=angleToNextPoint-Math.PI;
        }else {
            oppositeAngleToNextPoint= angleToNextPoint+Math.PI;
        }
        angleToNextPoint=0;
         */

        if(angleTowardsGoal(goal)) {
            long timeBefore = System.currentTimeMillis();
            while(System.currentTimeMillis() - timeBefore < 1000){
                dude.moveBackward();
            }
            dude.stopWheels();
            angleTowardsGoal(goal);
            timeBefore = System.currentTimeMillis();
            while(System.currentTimeMillis() - timeBefore < 40000){
                dude.openCheeks();
            }
            dude.stopBallDropper();
        }
    }
    private boolean angleTowardsGoal(Point goal){
        currentAngle = dude.getAngle();
        Point currentPosition = new Point(dude.getMap().getRobotPosition().getX(), dude.getMap().getRobotPosition().getY());
        double targetAngle = getAngleBetweenTwoPoints(currentPosition, goal);
        if(targetAngle<0){
            angleToNextPoint=targetAngle+Math.PI;
        } else if(targetAngle>0){
            angleToNextPoint=targetAngle-Math.PI;
        }

        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint, 0.05)) {

            //turn towards ball
            if (pointIsLeftOfRobotHeading()) {
                turnLeftTowardsPoint();
            } else {
                turnRightTowardsPoint();
            }
            return false;
        }
        return true;
    }
    private void turnLeftTowardsPoint() {

        currentAngle = dude.getAngle();
        if (pointIsLeftOfRobotHeading()) {
            // Turn left towards ball
            dude.turnLeft(100);
            while (pointIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint && (!myBehavior.isSuppressed() || (myBehavior.isSuppressed() && dude.getMap().getBalls().size()==0))) {
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint)){
                    dude.setWheelSpeed(35);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(50);
                }
            }
            dude.stopWheels();
            // Stop turning
        }
    }
    private boolean pointIsLeftOfRobotHeading( ) {
        double oppositeAngleOfRobot;

        // Robot's heading in upper quadrant.
        if(currentAngle>0){
            oppositeAngleOfRobot=currentAngle-Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextPoint<currentAngle&&angleToNextPoint>=oppositeAngleOfRobot){
                return false;
            }
            else{
                return true;
            }
        }
        // Robot's heading in lower quadrant.
        else if(currentAngle<0){
            oppositeAngleOfRobot=currentAngle+Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextPoint<currentAngle||angleToNextPoint>=oppositeAngleOfRobot){
                return false;
            }
            else{
                return true;
            }
        }
        else {
            if(angleToNextPoint>0){
                return true;
            } else return false;
        }
    }
    private void turnRightTowardsPoint(){
        currentAngle = dude.getAngle();
        if (!pointIsLeftOfRobotHeading()) {

            dude.turnRight(100);
            while(!pointIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint && (!myBehavior.isSuppressed() || (myBehavior.isSuppressed() && dude.getMap().getBalls().size()==0))){
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint)){
                    dude.setWheelSpeed(35);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(50);
                }
            }
            dude.stopWheels();


        }
    }
    private boolean isApproximatelySameAngle(double robotAngle,double targetAngle, double marginDegrees){
        return ((Math.abs(robotAngle-targetAngle) < marginDegrees) || (robotAngle>Math.PI-marginDegrees/2 && targetAngle<-Math.PI+marginDegrees/2) || (robotAngle<-Math.PI+marginDegrees/2 && targetAngle>Math.PI-marginDegrees/2));
    }
    private boolean isApproximatelySameAngle(double robotAngle,double targetAngle){
        return ((Math.abs(robotAngle-targetAngle) < 0.15) || (robotAngle>3.05 && targetAngle<-3.05) || (robotAngle<-3.05 && targetAngle>3.05));
    }

    public double getAngleBetweenTwoPoints(Point point, Point target) {
        return Math.atan2(-point.y - (-target.y),target.x - point.x);

    }

    public double getDistanceToPoint() {
        return distanceToPoint;
    }

    public void driveTowardsBall(TennisBall nextBall) {
        while(!myBehavior.isSuppressed() || dude.getMap().getBalls().size()==0) {
            turnTowards(nextBall);
            distanceToPoint = distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY()), new Point(nextBall.getX(), nextBall.getY()));

            if (distanceToPoint<75) {
                dude.moveForward(100);
            } else if (distanceToPoint<150){
                dude.moveForward(300);
            } else {
                dude.moveForward(400);
            }

            if (closeToBall(nextBall)) {
                pickUpBall();
                break;
            }
        }
    }
    public void driveTowardsWaypoint(Point point) {
        turnsTowardsWayPoint(point);
        distanceToPoint = distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY()), point);
        if (distanceToPoint<75) {
            dude.moveForward(100);
        } else if (distanceToPoint<150){
            dude.moveForward(250);
        } else {
            dude.moveForward(500);
        }
    }
    public void driveTowardsWaypoint(Point point,int speed) {
        turnsTowardsWayPoint(point);
        distanceToPoint = distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY()), point);
        dude.moveForward(speed);
    }
    private void pickUpBall() {
        dude.collectBall();
    }
    private boolean closeToBall(TennisBall nextBall) {
        distanceToPoint=distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(),
                dude.getMap().getRobotPosition().getFrontSideY()),new Point(nextBall.getX(),nextBall.getY()));
        return distanceToPoint < 17;
    }
    private boolean isMovingForward() {
        return dude.getState() == RobotState.MOVING_FORWARD;
    }
}

