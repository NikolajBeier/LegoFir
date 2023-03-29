package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.Legofir;
import org.slf4j.Logger;


public class DriveTowardsBall implements MyBehavior{
    private static int HALF_SECOND = 500;
    //private static final Logger log = org.slf4j.LoggerFactory.getLogger(GyroSensorDemo3.class);
    Boolean suppressed = false;
    Legofir dude;
    Boolean stopCondition = false;

    final SampleProvider  sp = dude.ev3GyroSensor.getAngleMode();
    int value =0;
    int iterationcounter =0;

    /*
    while(true){
        float[] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample,0);
        value=(int) sample[0];
        
        log.in
    }*/

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

    @Override
    public void setStopCondition(Boolean stopCondition) {

    }
}
