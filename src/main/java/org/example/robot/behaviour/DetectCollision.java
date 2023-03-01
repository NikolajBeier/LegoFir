package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.Legofir;

import java.rmi.RemoteException;

public class DetectCollision implements Behavior {

    Legofir dude;
    Boolean suppressed = false;
    final float AVOID_DISTANCE = 0.25f;

    public DetectCollision(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public void action() {
        suppressed = false;
        if(!suppressed){
            dude.stopWheels();
            dude.turnRight();
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public boolean takeControl() {
        SampleProvider sampleProvider = dude.ultrasonicSensor.getDistanceMode();
        float [] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        float distanceValue = sample[0];
        return (distanceValue <= AVOID_DISTANCE);
    }
}
