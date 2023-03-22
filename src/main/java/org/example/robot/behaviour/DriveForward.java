package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;
import org.example.robot.Legofir;

import static java.lang.Thread.sleep;

import java.rmi.RemoteException;

public class DriveForward implements MyBehavior {

    Boolean suppressed = false;
    Legofir legofir;
    Boolean stopCondition = false;

    public DriveForward(Legofir legofir){
        this.legofir = legofir;
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    @Override
    public void suppress(){
        suppressed = true;
    }

    // The default behaviour is to drive forward, so should always return true
    @Override
    public boolean takeControl() {
        if(stopCondition){
            System.out.println("DriveForward.takeControl() = " + false);
            return false;
        }
        System.out.println("DriveForward.takeControl() = " + true);
        return true;
    }

    @Override
    public void action() {
        suppressed = false;
        while(!suppressed) {
            legofir.beginHarvester();
            legofir.moveForward();
            System.out.println(legofir);
        }
        legofir.stopHarvester();
        legofir.stopWheels();
    }
}
