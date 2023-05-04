package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;

public class StopBehaviour implements MyBehavior {
    boolean stopCondition = false;
    boolean suppressed = false;

    public StopBehaviour() {
        this.suppress();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed = true;
    }

    @Override
    public boolean takeControl() {
        if(!stopCondition){
            System.out.println("StopBehaviour.takeControl() = " + false);
            return false;
        }
        System.out.println("StopBehaviour.takeControl() = " + true);
        return true;
    }

    // This behavior does nothing
    @Override
    public void action() {
    }

}
