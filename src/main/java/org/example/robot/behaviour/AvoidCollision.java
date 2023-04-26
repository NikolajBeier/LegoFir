package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import org.example.robot.Legofir;

public class AvoidCollision implements MyBehavior {

    String BehaviorName = "Avoid Collision";
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
        dude.setCurrentBehaviourName(BehaviorName);
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
        else return false;
        // TODO: LOGIK
    }
}
