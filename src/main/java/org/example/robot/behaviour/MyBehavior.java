package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;

public interface MyBehavior extends Behavior {
    boolean takeControl();

    void action();

    void suppress();

    void setStopCondition(Boolean stopCondition);
}
