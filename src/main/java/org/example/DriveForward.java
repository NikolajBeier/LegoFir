package org.example;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import static java.lang.Thread.sleep;

import java.rmi.RemoteException;

public class DriveForward implements Behavior {

    Legofir legofir;

    public DriveForward(Legofir legofir) {
        this.legofir = legofir;
    }


    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        try {
            legofir.moveForward();
            legofir.beginHarvester();
            sleep(3000);
            legofir.stopWheels();
            sleep(2000);
            legofir.turnRight();
            sleep(1000);
            legofir.moveForward();
            sleep(2000);
            legofir.stopWheels();
            legofir.stopHarvester();
            legofir.closePorts();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void suppress() {

    }
}
