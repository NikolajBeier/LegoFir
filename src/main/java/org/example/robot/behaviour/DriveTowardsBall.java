package org.example.robot.behaviour;


import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import org.example.robot.Legofir;

import java.util.logging.Logger;


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
        while (!suppressed){
            System.out.println(dude.GetAngle());

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
