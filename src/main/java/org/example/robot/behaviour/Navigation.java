package org.example.robot.behaviour;

import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;


public class Navigation {
    double currentAngle;
    Legofir dude;
     double  distanceToPoint;

    double angleToNextPoint;

    public void checkDirection( TennisBall nextPoint, Legofir dude) {
        this.dude=dude;

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        System.out.println("we get here");
        int nextPointX = nextPoint.getX();
        int nextPointY = nextPoint.getY();

        currentAngle = dude.getAngle();
        distanceToPoint = distanceBetweenPoints(new Point(currentPosition.getX(), currentPosition.getY()), new Point(nextPointX, nextPointY));

        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
        Point Pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor...
        angleToNextPoint = Geometry.degreesOfVectorInRadians(Pointvector.x, Pointvector.y);
/*
            System.out.println("NextBallX: " + nextBallX + ", NextBallY: " + nextBallY);
            System.out.println("CurrentPositionX: " + currentPosition.getX() + ", CurrentPositionY: " + currentPosition.getY());
            System.out.println("RobotToBallVectorX " + ballVector.x + ", RobotToBallVectorY" + ballVector.y);
            System.out.println("Current angle: " + currentAngle);
            System.out.println("Angle to next ball: " + angleToNextBall);
 */


        //if current angle is not close to angle to next ball
        System.out.println("isApproximatelySameAngle: " + isApproximatelySameAngle());
        if (!isApproximatelySameAngle()) {
            //turn towards ball
            if (ballIsLeftOfRobotHeading()) {
                turnLeftTowardsBall();
            } else {
                turnRightTowardsBall();
            }
        }
    }

    private void turnLeftTowardsBall() {
        currentAngle = dude.getAngle();
        if (ballIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning left - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            // Turn left towards ball
            dude.turnLeft();
            while (ballIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint) {
                currentAngle = dude.getAngle();
                //System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
            // Stop turning
            logger.info("time: "+System.currentTimeMillis()+". Turning left ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            dude.stopWheels();
        }
    }
    private boolean ballIsLeftOfRobotHeading() {
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
    private void turnRightTowardsBall() {
        currentAngle = dude.getAngle();
        if (!ballIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning right - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.turnRight();
            while(!ballIsLeftOfRobotHeading() && currentAngle!= angleToNextPoint){
                currentAngle = dude.getAngle();
                //System.out.println("Turning Right. CurrentAngle = " + currentAngle);
            }
            logger.info("time: "+System.currentTimeMillis()+". Turning right ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextPoint);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.stopWheels();
        }
    }
    private boolean isApproximatelySameAngle(){
        return ((Math.abs(currentAngle-angleToNextPoint) < 0.3) || (currentAngle>3 && angleToNextPoint<-3) || (currentAngle<-3 && angleToNextPoint>3));
    }

    public double getDistanceToPoint() {
        return distanceToPoint;
    }
}

