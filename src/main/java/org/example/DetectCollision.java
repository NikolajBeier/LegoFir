package org.example;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

public class DetectCollision implements Behavior {
    public DetectCollision(RMIRegulatedMotor harvester, RMIRegulatedMotor a, RMIRegulatedMotor b) {
    }

    @Override
    public boolean takeControl() {
        return false;
    }

    @Override
    public void action() {

    }

    @Override
    public void suppress() {

    }
}
