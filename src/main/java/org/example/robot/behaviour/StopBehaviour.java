package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;

public class StopBehaviour implements Behavior {
    Boolean condition;

    public StopBehaviour(Boolean condition) {
        this.condition=condition;
    }

    @Override
    public void suppress() {
    }

    @Override
    public boolean takeControl() {
        return condition;
    }

    // This behavior does nothing
    @Override
    public void action() {
    }

}
