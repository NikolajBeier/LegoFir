package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;

public class StopBehaviour implements MyBehavior {
    Boolean stopCondition = false;

    public StopBehaviour() {
    }

    @Override
    public void suppress() {
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    @Override
    public boolean takeControl() {
        System.out.println("StopBehaviour.takeControl() = " + stopCondition);
        return stopCondition;
    }

    // This behavior does nothing
    @Override
    public void action() {
    }

}
