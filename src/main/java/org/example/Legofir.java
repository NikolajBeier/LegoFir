package org.example;

import lejos.remote.ev3.RMIRegulatedMotor;

import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

public class Legofir {

    RMIRegulatedMotor left;
    RMIRegulatedMotor right;
    RMIRegulatedMotor harvester;
    int defaultSpeedHarvester;
    int defaultSpeedWheel;
    int defaultAccelerationHarvester;
    int defaultAccelerationWheel;

    public Legofir(RMIRegulatedMotor left, RMIRegulatedMotor right, RMIRegulatedMotor harvester, int defaultSpeedHarvester, int defaultSpeedWheel, int defaultAccelerationHarvester, int defaultAccelerationWheel) {
        this.left = left;
        this.right = right;
        this.harvester = harvester;
        this.defaultSpeedHarvester = defaultSpeedHarvester;
        this.defaultAccelerationHarvester = defaultAccelerationHarvester;
        this.defaultSpeedWheel = defaultSpeedWheel;
        this.defaultAccelerationWheel = defaultAccelerationWheel;
    }

    public void moveForward() throws RemoteException{
            left.backward();
            right.backward();
    }

    public void turnLeft() throws RemoteException{
        right.backward();
        left.forward();
        try {
            sleep(1110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWheels();
    }

    public void turnRight() throws RemoteException{
        left.backward();
        right.forward();
        try {
            sleep(1110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWheels();
    }

    public void beginHarvester() throws RemoteException{
       harvester.forward();
    }

    public void stopWheels() throws RemoteException{
        left.stop(true);
        right.stop(true);
    }

    public void stopHarvester() throws RemoteException{
        harvester.stop(true);
    }

    public void openCheeks() throws RemoteException{

    }

    public void closeCheeks() throws RemoteException{

    }

    public void closePorts() throws RemoteException{
        harvester.close();
        left.close();
        right.close();
        System.out.println("lukket begge motorer");
    }

}
