package org.example.robot.behaviour;


import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.Legofir;
import org.opencv.core.Point;


public class DriveTowardsBall implements MyBehavior{
    volatile Boolean suppressed = false;
    Legofir dude;
    Boolean stopCondition = false;


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

            int currentAngle = dude.getAngle();

            // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
            Point ballVector = new Point(nextBallX - currentPosition.getX(), nextBallY - currentPosition.getY());

            // Vinkel af vektor...

            int cameraAngleToNextBall = (int) Math.atan(ballVector.y / ballVector.x);

            int angleToNextBall = currentAngle + cameraAngleToNextBall;

            turnLeftTowardsBall(currentAngle,cameraAngleToNextBall,angleToNextBall);
            turnRightTowardsBall(currentAngle,cameraAngleToNextBall,angleToNextBall);




            // Heading found, now go forward

            dude.beginHarvester();
            dude.moveForward();

            // Waits to be suppressed or until the robot is close enough to the ball for it to be assumed picked up or pushed away.
            while (!suppressed) {
                System.out.println("Driving towards ball: Robot Position: x=" + dude.getMap().getRobotPosition().getX() + ", y=" + dude.getMap().getRobotPosition().getY() + ", heading=" + dude.getMap().getRobotPosition().getHeading());
                System.out.println("Driving towards ball: Next Ball Position: x=" + nextBallX + ", y=" + nextBallY);
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

    private void turnLeftTowardsBall(int currentAngle, int cameraAngleToNextBall, int angleToNextBall) {
        if (cameraAngleToNextBall > 0) {
            // Turn left towards ball
            dude.turnLeft();
            while (currentAngle <= angleToNextBall) {
                currentAngle = dude.getAngle();
                System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
            // Stop turning
            dude.stopWheels();
        }
    }
    private void turnRightTowardsBall(int currentAngle, int cameraAngleToNextBall, int angleToNextBall) {
        if (cameraAngleToNextBall < 0) {
            dude.turnRight();
            while (currentAngle >= angleToNextBall) {
                currentAngle = dude.getAngle();
                System.out.println("Turning Right. CurrentAngle = " + currentAngle);
            }
            dude.stopWheels();
        }
    }
}
