package org.example.robot.behaviour;


import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.robot.model.RobotState;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;


public class DriveTowardsBall implements MyBehavior {
    String BehaviorName = "DriveTowardsBall";
    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    double currentAngle;
    double angleToNextBall;
    Navigation navigation = new Navigation();

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public boolean takeControl() {
        if(stopCondition){
            System.out.println("DriveTowardsBall.takeControl() = " + false);
            return false;
        }
        System.out.println("DriveTowardsBall.takeControl() = " + true);
        return true;
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        TennisBall nextBall = dude.getMap().getNextBall();
        while (!suppressed) {
            System.out.println("DrivTowardBallAction");
            navigation.checkDirection(nextBall, dude);
            dude.moveForward();

            // Waits to be suppressed or until the robot is close enough to the ball for it to be assumed picked up or pushed away.
            if (navigation.distanceToPoint < 150) {
                long timeBefore = System.currentTimeMillis();
                dude.beginHarvester();
                while (!suppressed) {
                /*
                if (dude.getMap().getRobotPosition().getX() < nextBallX + 25 && dude.getMap().getRobotPosition().getX() > nextBallX - 25) {
                    if (dude.getMap().getRobotPosition().getY() < nextBallY + 25 && dude.getMap().getRobotPosition().getY() > nextBallY - 25) {
                        break;
                    }
                }
                 */
                    if (System.currentTimeMillis() - timeBefore > 2000) {
                        break;
                    }
                }

                timeBefore = System.currentTimeMillis();
                while (!suppressed) {
                    if (System.currentTimeMillis() - timeBefore > 1000) {
                        nextBall = dude.getMap().getNextBall();
                        break;
                    }
                }
                dude.stopHarvester();
            }

        }
        dude.stopWheels();
        dude.stopHarvester();
    }

    /*
            RobotPosition currentPosition = dude.getMap().getRobotPosition();



            int nextBallX = nextBall.getX();
            int nextBallY = nextBall.getY();

            currentAngle = dude.getAngle();
            distanceToBall = distanceBetweenPoints(new Point(currentPosition.getX(), currentPosition.getY()), new Point(nextBallX, nextBallY));

            // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
            Point ballVector = new Point(nextBallX-currentPosition.getX(), nextBallY-currentPosition.getY());
            // Vinkel af vektor...
            angleToNextBall= Geometry.degreesOfVectorInRadians(ballVector.x,ballVector.y);
/*
            System.out.println("NextBallX: " + nextBallX + ", NextBallY: " + nextBallY);
            System.out.println("CurrentPositionX: " + currentPosition.getX() + ", CurrentPositionY: " + currentPosition.getY());
            System.out.println("RobotToBallVectorX " + ballVector.x + ", RobotToBallVectorY" + ballVector.y);
            System.out.println("Current angle: " + currentAngle);
            System.out.println("Angle to next ball: " + angleToNextBall);
 */


            //if current angle is not close to angle to next ball
         /*   System.out.println("isApproximatelySameAngle: "+isApproximatelySameAngle());
            if( !isApproximatelySameAngle() ){
                dude.stopWheels();
                //turn towards ball
                if(ballIsLeftOfRobotHeading()){
                    turnLeftTowardsBall();
                } else {
                    turnRightTowardsBall();
                }
            }
            dude.moveForward();



            // Waits to be suppressed or until the robot is close enough to the ball for it to be assumed picked up or pushed away.

            if(distanceToBall<150) {
                long timeBefore= System.currentTimeMillis();
                dude.beginHarvester();
                while (!suppressed) {
                /*
                if (dude.getMap().getRobotPosition().getX() < nextBallX + 25 && dude.getMap().getRobotPosition().getX() > nextBallX - 25) {
                    if (dude.getMap().getRobotPosition().getY() < nextBallY + 25 && dude.getMap().getRobotPosition().getY() > nextBallY - 25) {
                        break;
                    }
                }
                 */
               /*     if (System.currentTimeMillis() - timeBefore > 3000) {
                        break;
                    }
                }
                timeBefore= System.currentTimeMillis();
                while(!suppressed){
                    if (System.currentTimeMillis() - timeBefore > 1000) {
                        nextBall=dude.getMap().getNextBall();
                        break;
                    }
                }
                dude.stopHarvester();
            }
            System.out.println("DriveTowardsBall.action() - suppressed: " + suppressed);
        }
        dude.stopWheels();
    }



*/
    @Override
    public void suppress(){
        System.out.println("DriveTowardsBall.suppress()");
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
    }

    private void turnLeftTowardsBall() {
        currentAngle = dude.getAngle();
        if (ballIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning left - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextBall);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            // Turn left towards ball
            dude.turnLeft();
            while (ballIsLeftOfRobotHeading() && currentAngle!= angleToNextBall && !suppressed) {
                currentAngle = dude.getAngle();
                //System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
            // Stop turning
            logger.info("time: "+System.currentTimeMillis()+". Turning left ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextBall);
            //System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            dude.stopWheels();
        }
    }



    private void turnRightTowardsBall() {
        currentAngle = dude.getAngle();
        if (!ballIsLeftOfRobotHeading()) {
            logger.info("time: "+System.currentTimeMillis()+". Turning right - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextBall);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.turnRight();
            while(!ballIsLeftOfRobotHeading() && currentAngle!= angleToNextBall && !suppressed){
                currentAngle = dude.getAngle();
                //System.out.println("Turning Right. CurrentAngle = " + currentAngle);
            }
            logger.info("time: "+System.currentTimeMillis()+". Turning right ended - Current angle: "+currentAngle + ". Angle to next ball: " + angleToNextBall);
            //System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.stopWheels();
        }
    }
    private boolean ballIsLeftOfRobotHeading() {
        double oppositeAngleOfRobot;

        // Robot's heading in upper quadrant.
        if(currentAngle>0){
            oppositeAngleOfRobot=currentAngle-Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextBall<currentAngle&&angleToNextBall>=oppositeAngleOfRobot){
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
            if(angleToNextBall<currentAngle||angleToNextBall>=oppositeAngleOfRobot){
                return false;
            }
            else{
                return true;
            }
        }
        else {
            if(angleToNextBall>0){
                return true;
            } else return false;
        }
    }
    private boolean isApproximatelySameAngle(){
        return ((Math.abs(currentAngle-angleToNextBall) < 0.3) || (currentAngle>3 && angleToNextBall<-3) || (currentAngle>3 && angleToNextBall>-3));
    }

}
