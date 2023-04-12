package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import org.example.robot.Legofir;

public class AvoidCollision implements MyBehavior {

    Legofir dude;
    Boolean suppressed = false;
    final float AVOID_DISTANCE = 0.25f;
    Boolean stopCondition = false;

    public AvoidCollision(Legofir dude) {
        this.dude = dude;
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed= true;
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