package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

import static java.lang.Thread.sleep;

public class DriveForward implements MyBehavior {

    String BehaviorName = "DriveForward";
    boolean suppressed = false;
    Legofir legofir;
    boolean stopCondition = false;

    public DriveForward(Legofir legofir){
        this.legofir = legofir;
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed= true;
    }

    @Override
    public boolean isSuppressed() {
        return false;
    }

    @Override
    public void suppress(){
        suppressed = true;
    }

    // The default behaviour is to drive forward, so should always return true
    @Override
    public boolean takeControl() {
        if(stopCondition){
            return false;
        }
        return true;
    }

    @Override
    public void action() {
        suppressed = false;
        legofir.setCurrentBehaviourName(BehaviorName);
        while(!suppressed) {
            legofir.beginHarvester();
            legofir.moveForward();
        }
        legofir.stopHarvester();
        legofir.stopWheels();
    }
}
