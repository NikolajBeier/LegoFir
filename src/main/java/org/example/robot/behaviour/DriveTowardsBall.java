package org.example.robot.behaviour;


import lejos.robotics.SampleProvider;
import org.example.robot.Legofir;


public class DriveTowardsBall implements MyBehavior{
    Boolean suppressed = false;
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
        int currentAngle= dude.GetAngle();

        int cameraAngleToNextBall=45; // TODO: get angle from camera

        int angleToNextBall=currentAngle+cameraAngleToNextBall;

        if(cameraAngleToNextBall>0){
            while(currentAngle<=angleToNextBall){
                dude.turnLeft();
                currentAngle=dude.GetAngle();
                System.out.println("Turning Left. CurrentAngle = " + currentAngle);
            }
        }
        if(cameraAngleToNextBall<0){
            while(currentAngle>=angleToNextBall){
                dude.turnRight();
                currentAngle=dude.GetAngle();
                System.out.println("Turning Right. CurrentAngle = " + currentAngle);

            }
        }
        // køre frem så længe vi må.
        while (!suppressed){
            dude.beginHarvester();
            dude.moveForward();
        }
        dude.stopHarvester();
        dude.stopWheels();
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
