package org.example;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import static java.lang.Thread.sleep;

import java.rmi.RemoteException;

public class DriveForward implements Behavior {

    Boolean suppressed = false;
    Legofir legofir;

    public DriveForward(Legofir legofir) {
        this.legofir = legofir;
    }

    /* Overridden Methods */

    @Override
    public void suppress() {
        suppressed = true;
    };

    // The default behaviour is to drive forward, so should always return true
    @Override
    public boolean takeControl() {
        return true;
    };

    @Override
    public void action() {
        suppressed = false;
        while(!suppressed){
            try {
                legofir.moveForward();
                sleep(1000);
                legofir.stopWheels();
                sleep(1000);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
