package org.example;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.RangeFinder;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

import java.rmi.RemoteException;

public class DetectCollision implements Behavior {

    Legofir dude;
    Boolean suppressed = false;
    final int AVOID_DISTANCE = 15;
    final SampleProvider sampleProvider;

    public DetectCollision(Legofir dude) {
        this.dude = dude;
        sampleProvider= dude.ultrasonicSensor.getDistanceMode();
    }


    @Override
    public void action() {
        suppressed = false;
        if(!suppressed){
            try {
                dude.stopWheels();
                dude.turnRight();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    };

    @Override
    public boolean takeControl() {
        float [] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        int distanceValue = (int)sample[0];
        return (distanceValue <= AVOID_DISTANCE);
    };
}
