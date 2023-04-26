package org.example.robot.behaviour;


import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.Legofir;
import org.opencv.core.Point;


public class DriveTowardsBall implements MyBehavior{
    volatile Boolean suppressed = false;
    Legofir dude;
    Boolean stopCondition = false;
    double currentAngle;
    double angleToNextBall;


    public DriveTowardsBall(Legofir dude){
        this.dude=dude;
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
        suppressed=false;
        while(!suppressed) {

            RobotPosition currentPosition = dude.getMap().getRobotPosition();
            TennisBall nextBall = dude.getMap().getNextBall();

            int nextBallX = nextBall.getX();
            int nextBallY = nextBall.getY();

             currentAngle = dude.getAngle();

            // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
            Point ballVector = new Point(nextBallX-currentPosition.getX(), nextBallY-currentPosition.getY());

            // Vinkel af vektor...

            angleToNextBall= Math.atan2(ballVector.y, ballVector.x);

            System.out.println("before turning: "+currentAngle + " " + angleToNextBall);

            turnLeftTowardsBall();
            turnRightTowardsBall();




            // Heading found, now go forward

            dude.beginHarvester();
            dude.moveForward();

            // Waits to be suppressed or until the robot is close enough to the ball for it to be assumed picked up or pushed away.
            while (!suppressed) {
                //System.out.println("Driving towards ball: Robot Position: x=" + dude.getMap().getRobotPosition().getX() + ", y=" + dude.getMap().getRobotPosition().getY() + ", heading=" + dude.getMap().getRobotPosition().getHeading());
                //System.out.println("Driving towards ball: Next Ball Position: x=" + nextBallX + ", y=" + nextBallY);
                if (dude.getMap().getRobotPosition().getX() < nextBallX + 25 && dude.getMap().getRobotPosition().getX() > nextBallX - 25) {
                    if (dude.getMap().getRobotPosition().getY() < nextBallY + 25 && dude.getMap().getRobotPosition().getY() > nextBallY - 25) {
                        break;
                    }
                }
            }

            dude.stopHarvester();
            dude.stopWheels();
        }
    }




    @Override
    public void suppress(){
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
            System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            // Turn left towards ball
            dude.turnLeft();
            while (ballIsLeftOfRobotHeading() && currentAngle!= angleToNextBall) {
                currentAngle = dude.getAngle();
                //System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
            // Stop turning
            System.out.println("turning left: "+currentAngle + " " + angleToNextBall);
            dude.stopWheels();
        }
    }



    private void turnRightTowardsBall() {
        currentAngle = dude.getAngle();
        if (!ballIsLeftOfRobotHeading()) {
            System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.turnRight();
            while(!ballIsLeftOfRobotHeading() && currentAngle!= angleToNextBall){
                currentAngle = dude.getAngle();
                //System.out.println("Turning Right. CurrentAngle = " + currentAngle);
            }
            System.out.println("turning right: "+currentAngle + " " + angleToNextBall);

            dude.stopWheels();
        }
    }
    private boolean ballIsLeftOfRobotHeading() {
        double oppositeAngleOfRobot;

        // Robot's heading in upper quadrant.
        if(currentAngle>0){
            oppositeAngleOfRobot=currentAngle-Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextBall<currentAngle&&angleToNextBall>oppositeAngleOfRobot){
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
            if(angleToNextBall>currentAngle&&angleToNextBall<oppositeAngleOfRobot){
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
}
