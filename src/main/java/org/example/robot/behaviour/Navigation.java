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
    public Navigation(Legofir dude){
        this.dude=dude;
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
        //System.out.println("isApproximatelySameAngle: " + isApproximatelySameAngle(currentAngle,angleToNextPoint));
        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading()) {
                turnLeftTowardsPoint();
            } else {
                turnRightTowardsPoint();
            }
        }
    }

    public void turnCheeksTowardsGoal(Point goal){
        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        int nextPointX = (int) goal.x;
        int nextPointY = (int) goal.y;

        currentAngle = dude.getAngle();
        Point pointVector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        angleToNextPoint = Geometry.degreesOfVectorInRadians(pointVector.x, pointVector.y);
        double oppositeAngleToNextPoint=0;
        if (angleToNextPoint>0){
            oppositeAngleToNextPoint=angleToNextPoint-Math.PI;
        }else {
            oppositeAngleToNextPoint= angleToNextPoint+Math.PI;
        }
        angleToNextPoint=oppositeAngleToNextPoint;

        System.out.println("isApproximatelySameAngle: " + isApproximatelySameAngle(currentAngle,oppositeAngleToNextPoint));
        if (!isApproximatelySameAngle(currentAngle,oppositeAngleToNextPoint)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading()) {
                turnLeftTowardsPoint();

            } else {
                turnRightTowardsPoint();
            }
        }
        else {
            long timeBefore = System.currentTimeMillis();
            while(System.currentTimeMillis() - timeBefore < 10000){
                dude.openCheeks();

            }
            dude.stopBallDropper();
        }
    }
    private void turnLeftTowardsPoint() {

        currentAngle = dude.getAngle();
        if (pointIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning left - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            // Turn left towards ball
            dude.turnLeft();
            while (pointIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint) {
                currentAngle = dude.getAngle();
                //System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
            dude.stopWheels();
            // Stop turning
            logger.info("time: "+System.currentTimeMillis()+". Turning left ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
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
    private void turnRightTowardsPoint() {
        currentAngle = dude.getAngle();
        if (!pointIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning right - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.turnRight();
            while(!pointIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint){
                currentAngle = dude.getAngle();
                //System.out.println("Turning Right. CurrentAngle = " + currentAngle);
            }
            dude.stopWheels();

            logger.info("time: "+System.currentTimeMillis()+". Turning right ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

        }
    }
    private boolean isApproximatelySameAngle(double robotAngle,double targetAngle){
        return ((Math.abs(robotAngle-targetAngle) < 0.2) || (robotAngle>3 && targetAngle<-3) || (robotAngle<-3 && targetAngle>3));
    }

    public double getDistanceToPoint() {
        return distanceToPoint;
    }

    public void driveTowardsBall(TennisBall nextBall,boolean suppressed) {
        turnTowards(nextBall);
        dude.moveForward();

        if (closeToBall(nextBall)) {
            pickUpBall(suppressed);
        }
    }
    public void driveTowardsWaypoint(Point point) {
        turnsTowardsWayPoint(point);
        dude.moveForward();
    }

    private void pickUpBall(boolean suppressed) {
        long timeBefore = System.currentTimeMillis();
        dude.beginHarvester();
        while (!suppressed) {
            if (System.currentTimeMillis() - timeBefore > 2000) {
                break;
            }
        }
        dude.stopHarvester();
    }

    private boolean closeToBall(TennisBall nextBall) {
        distanceToPoint=distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(),dude.getMap().getRobotPosition().getFrontSideY()),new Point(nextBall.getX(),nextBall.getY()));
        System.out.println("distance to ball: "+distanceToPoint);
        return distanceToPoint < 50;
    }

    private boolean isMovingForward() {
        return dude.getState() == RobotState.MOVING_FORWARD;
    }
}

