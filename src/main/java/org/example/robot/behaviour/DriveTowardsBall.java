package org.example.robot.behaviour;


import lejos.robotics.SampleProvider;
import org.example.robot.Legofir;


public class DriveTowardsBall implements MyBehavior{
    Boolean suppressed = false;
    Legofir dude;
    Boolean stopCondition = false;
    SampleProvider sampleProvider=dude.ev3GyroSensor.getAngleAndRateMode();
    float[] angle= new float[sampleProvider.sampleSize()];


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
        while (!suppressed){
            System.out.println(dude.GetAngle());
            System.out.println(dude.ev3GyroSensor.getAngleAndRateMode().sampleSize());
            dude.turnRight();



        }
    }

    @Override
    public void suppress(){
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;

    }
}
