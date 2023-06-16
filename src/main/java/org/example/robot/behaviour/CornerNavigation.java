package org.example.robot.behaviour;

import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;

public class CornerNavigation {
    Legofir dude;
    MyBehavior myBehavior;
    Navigation navigation;

    Point wayPoint;

    public CornerNavigation(Legofir dude, MyBehavior myBehavior, Navigation navigation) {
        this.dude=dude;
        this.myBehavior= myBehavior;
        this.navigation = navigation;
    }

    public void pickUpBallInCorner(TennisBall nextBall, DriveTowardsBall.Position position) {
        wayPoint = new Point();
        int wayPointMargin = 125;
        switch (position){
            case TOPLEFT -> wayPoint = new Point(nextBall.getX() + wayPointMargin, nextBall.getY() - wayPointMargin);
            case TOPRIGHT -> wayPoint = new Point(nextBall.getX() - wayPointMargin, nextBall.getY() - wayPointMargin);
            case BOTTOMLEFT -> wayPoint = new Point(nextBall.getX() + wayPointMargin , nextBall.getY() + wayPointMargin);
            case BOTTOMRIGHT -> wayPoint = new Point(nextBall.getX() - wayPointMargin, nextBall.getY() + wayPointMargin);
        }
            if(isOnWayPoint()){
                Point nextBallPoint = new Point(nextBall.getX(), nextBall.getY());
                driveIntoCorner(nextBallPoint);
                turnTowards(nextBallPoint, 0.01);
                collectBallInCorner();
            } else {
                navigation.driveTowardsWaypoint(wayPoint);
            }
    }

    private void collectBallInCorner() {
        dude.collectBall();
        dude.stopHarvester();
        dude.moveBackward();
        long timeBefore = System.currentTimeMillis();
        while(System.currentTimeMillis() - timeBefore < 2500) {
        }
        dude.stopWheels();
    }

    private boolean isOnWayPoint() {
        double errorMargin = 18;

        double distance = Math.sqrt(Math.pow(dude.getMap().getRobotPosition().getX() - wayPoint.x, 2) +
                Math.pow(dude.getMap().getRobotPosition().getY() - wayPoint.y, 2));
        return distance <= errorMargin;
    }

    private void driveIntoCorner(Point nextBall){
        double distanceToPoint = Double.MAX_VALUE;

        while(distanceToPoint > 16) {
            distanceToPoint = distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY()), nextBall);
            if(distanceToPoint < 40){
                turnTowards(nextBall, 0.04);
                dude.moveForward(15);
                continue;
            }



            turnTowards(nextBall, 0.08);
            if(distanceToPoint < 50){
                dude.moveForward(25);
            } else if (distanceToPoint<100) {
                dude.moveForward(50);
            } else {
                dude.moveForward(100);
            }
        }
        dude.stopWheels();
    }
    public void turnTowards(Point nextPoint, double turnMargin) {

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        double nextPointX = nextPoint.x;
        double nextPointY = nextPoint.y;

        double currentAngle = dude.getAngle();
        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
        Point Pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor..x.
        double angleToNextPoint = Geometry.degreesOfVectorInRadians(Pointvector.x, Pointvector.y);

        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint,turnMargin)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {
                turnLeftTowardsPoint(currentAngle, angleToNextPoint);
            } else {
                turnRightTowardsPoint(currentAngle, angleToNextPoint);
            }
        }
    }
    private boolean pointIsLeftOfRobotHeading( double currentAngle, double angleToNextPoint) {
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
    private boolean isApproximatelySameAngle(double robotAngle,double targetAngle, double marginDegrees){
        return ((Math.abs(robotAngle-targetAngle) < marginDegrees) || (robotAngle>Math.PI-marginDegrees/2 && targetAngle<-Math.PI+marginDegrees/2) || (robotAngle<-Math.PI+marginDegrees/2 && targetAngle>Math.PI-marginDegrees/2));
    }
    private void turnRightTowardsPoint(double currentAngle, double angleToNextPoint) {
        currentAngle = dude.getAngle();
        if (!pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {

            dude.turnRight(100);
            while(!pointIsLeftOfRobotHeading(currentAngle,angleToNextPoint) && currentAngle!= angleToNextPoint){
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.2)){
                    dude.setWheelSpeed(15);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(35);
                }
            }
            dude.stopWheels();
        }
    }
    private void turnLeftTowardsPoint(double currentAngle, double angleToNextPoint) {
        currentAngle = dude.getAngle();
        if (pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {
            // Turn left towards ball
            dude.turnLeft(100);
            while (pointIsLeftOfRobotHeading(currentAngle,angleToNextPoint) && currentAngle!= angleToNextPoint) {
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.2)){
                    dude.setWheelSpeed(15);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(35);
                }
            }
            dude.stopWheels();
        }
    }
}
