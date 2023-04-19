package org.example.robot.behaviour;


import lejos.robotics.SampleProvider;
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

            if (cameraAngleToNextBall > 0) {
                while (currentAngle <= angleToNextBall) {
                    dude.turnLeft();
                    currentAngle = dude.getAngle();
                    System.out.println("Turning Left. CurrentAngle = " + currentAngle);
                }
            }
            if (cameraAngleToNextBall < 0) {
                while (currentAngle >= angleToNextBall) {
                    dude.turnRight();
                    currentAngle = dude.getAngle();
                    System.out.println("Turning Right. CurrentAngle = " + currentAngle);

                }
            }


            // Heading found, now go forward

            dude.beginHarvester();
            dude.moveForward();

            // Waits to be suppressed or until the robot is close enough to the ball for it to be assumed picked up or pushed away.
            while (!suppressed) {
                if (dude.getMap().getRobotPosition().getX() < nextBallX + 5 && dude.getMap().getRobotPosition().getX() > nextBallX - 5) {
                    if (dude.getMap().getRobotPosition().getY() < nextBallY + 5 && dude.getMap().getRobotPosition().getY() > nextBallY - 5) {
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
}
