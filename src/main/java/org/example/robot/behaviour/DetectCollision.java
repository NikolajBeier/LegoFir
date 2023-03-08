package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.Legofir;

import java.rmi.RemoteException;

public class DetectCollision implements MyBehavior {

    Legofir dude;
    Boolean suppressed = false;
    final float AVOID_DISTANCE = 0.25f;
    Boolean stopCondition = false;

    public DetectCollision(Legofir dude) {
        this.dude = dude;
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
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
        if(stopCondition){
            System.out.println("DetectCollision.takeControl() = " + false);
            return false;
        }
        try {
            if(!dude.ultrasonicSensor.isEnabled()){
                System.out.println("DetectCollision.takeControl() = " + false);
                return false;
            }
            SampleProvider sampleProvider = dude.ultrasonicSensor.getDistanceMode();
            float[] sample = new float[sampleProvider.sampleSize()];
            sampleProvider.fetchSample(sample, 0);
            float distanceValue = sample[0];
            System.out.println("DetectCollision.takeControl() = " + (distanceValue <= AVOID_DISTANCE));
            return (distanceValue <= AVOID_DISTANCE);
        } catch (NullPointerException e) {
            System.out.println("DetectCollision.takeControl() = " + false);
            return false;
        }
    }
}
