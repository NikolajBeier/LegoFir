package org.example;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.rmi.RemoteException;

public class DriveForward implements Behavior {

    RMIRegulatedMotor harvester;
    RMIRegulatedMotor a;
    RMIRegulatedMotor b;

    public DriveForward(RMIRegulatedMotor harvester, RMIRegulatedMotor a, RMIRegulatedMotor b) {
        this.harvester = harvester;
        this.a = a;
        this.b = b;
    }


    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        try {
            harvester.forward();
            a.forward();
            b.backward();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void suppress() {

    }
}
