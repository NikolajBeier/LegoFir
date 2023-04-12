package org.example.robot;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.robotics.SampleProvider;
import org.example.mapping.Map;
import org.example.mapping.TennisBall;

import java.rmi.RemoteException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Legofir {

    // Mapping

    Map map = new Map(180, 120);

    // Sensors

    public EV3UltrasonicSensor ultrasonicSensor;
    // Motors
    RMIRegulatedMotor left;
    RMIRegulatedMotor right;
    RMIRegulatedMotor harvester;
    RMIRegulatedMotor balldropper;

    // Motor default values
    int defaultSpeedHarvester;
    int defaultSpeedWheel;
    int defaultSpeedBallDropper;
    int defaultAccelerationHarvester;
    int defaultAccelerationWheel;
    int defaultAccelerationBallDropper;

    RMISampleProvider sampleProvider;




    public Legofir(RMIRegulatedMotor left, RMIRegulatedMotor right, RMIRegulatedMotor harvester, RMIRegulatedMotor balldropper, int defaultSpeedHarvester, int defaultSpeedWheel, int defaultSpeedBallDropper, int defaultAccelerationHarvester, int defaultAccelerationWheel, int defaultAccelerationBallDropper, EV3UltrasonicSensor ultrasonicSensor, RMISampleProvider ev3GyroSensor) {
        this.left = left;
        this.right = right;
        this.harvester = harvester;
        this.balldropper = balldropper;
        this.defaultSpeedHarvester = defaultSpeedHarvester;
        this.defaultAccelerationHarvester = defaultAccelerationHarvester;
        this.defaultSpeedWheel = defaultSpeedWheel;
        this.defaultAccelerationWheel = defaultAccelerationWheel;
        this.defaultSpeedBallDropper = defaultSpeedBallDropper;
        this.defaultAccelerationBallDropper = defaultAccelerationBallDropper;
        this.ultrasonicSensor = ultrasonicSensor;
        this.sampleProvider=ev3GyroSensor;
    }

    public void moveForward(){
        try {
            left.backward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnLeft(){
        try {
            left.forward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnRight(){
        try{
            left.backward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void beginHarvester(){
        try {
            System.out.println(harvester.getSpeed());
            harvester.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void stopWheels(){
        try {
            left.stop(true);
            right.stop(true);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void stopHarvester(){
        try {
            harvester.stop(true);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void stopBallDropper(){
        try {
            balldropper.stop(true);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void openCheeks(){
        try{
            balldropper.rotate(180);
        } catch (RemoteException e) {
            stopAll();
        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopBallDropper();
    }

    public void closeCheeks(){
        try{
            balldropper.rotate(-180);
        } catch (RemoteException e) {
            stopAll();
        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopBallDropper();
    }

    public int GetAngle(){

        try {
            return (int) sampleProvider.fetchSample()[0];
        } catch (RemoteException e) {
            closePorts();
        }
        return 0;
    }


    public void closePorts(){
        try {
            harvester.close();
            left.close();
            right.close();
            balldropper.close();
            ultrasonicSensor.disable();
            while(ultrasonicSensor.isEnabled()) {
                System.out.println("venter på at ultrasonicSensor skal lukke");
            }
            ultrasonicSensor.close();
            System.out.println("lukket alle motorer og sensorer");
        } catch (RemoteException e) {
            System.out.println("Kunne ikke lukke motorer");
        }
    }
    public boolean isMoving(){
        try {
            return left.isMoving() && right.isMoving();
        } catch (RemoteException e) {
            stopAll();
        }
        return false;
    }


    public void stopAll() {
        stopWheels();
        stopHarvester();
        stopBallDropper();
        closePorts();
    }
}
