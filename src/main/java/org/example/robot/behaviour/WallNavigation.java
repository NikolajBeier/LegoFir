package org.example.robot.behaviour;

import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;

public class WallNavigation {
    Legofir dude;
    Navigation nav;
    Point nextBall = new Point(0,0);
    public WallNavigation(Legofir dude, Navigation nav) {
        this.dude = dude;
        this.nav = nav;
    }

    //needs an enum of Heading
    public void pickUpBallNextToWall(Point waypoint, TennisBall nextBall){
        nav.driveTowardsWaypoint(waypoint);
        slowlyMoveTowardsBallInCorner(nextBall);
        turnTowards(new Point(nextBall.getX(), nextBall.getY()));
        dude.collectBall();
        dude.moveBackward();
        long timeBefore = System.currentTimeMillis();
        while(System.currentTimeMillis() - timeBefore < 1500) {
        }
        dude.stopWheels();

        /*
        while (nav.myBehavior.isSuppressed()) {
            while(!checkIfRobotIsOnPoint()){
                nav.turnsTowardsWayPoint(waypoint);
            }
            nav.driveTowardsWaypoint(waypoint);
           // needs slowspeed
        }

         */

    }
    private void slowlyMoveTowardsBallInCorner(TennisBall nextBall) {
        Point nextBallPoint = new Point(nextBall.getX(),nextBall.getY());
        double distance = Double.MAX_VALUE;

        while(distance > 17) {
            turnTowards(nextBallPoint);
            if(distance>50){
                dude.moveForward(50);
            } else {
                dude.moveForward(15);
            }
            distance=distanceBetweenPoints(nextBallPoint, new Point(dude.getMap().getRobotPosition().getFrontSideX(),dude.getMap().getRobotPosition().getFrontSideY()));
        }
        dude.stopWheels();
    }
    public Boolean checkIfRobotIsOnPoint(){
        return (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y);
    }



    public void turnTowards(Point nextPoint) {

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        double nextPointX = nextPoint.x;
        double nextPointY = nextPoint.y;

        double currentAngle = dude.getAngle();
        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
        Point Pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor..x.
        double angleToNextPoint = Geometry.degreesOfVectorInRadians(Pointvector.x, Pointvector.y);

        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint,0.1)) {
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
            logger.info("time: "+System.currentTimeMillis()+". Turning right - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);

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
            logger.info("time: "+System.currentTimeMillis()+". Turning left - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
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
