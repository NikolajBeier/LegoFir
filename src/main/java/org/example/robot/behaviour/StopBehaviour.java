package org.example.robot.behaviour;

import lejos.robotics.subsumption.Behavior;
import org.example.robot.model.Legofir;

public class StopBehaviour implements MyBehavior {
    Legofir dude;
    Boolean stopCondition = false;
    Boolean suppressed = false;
    String BehaviorName = "StopBehaviour";

    public StopBehaviour() {
        this.suppress();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed = true;
    }

    @Override
    public boolean takeControl() {
        if(!stopCondition){
            return false;
        }
        return true;
    }

    // This behavior does nothing other than setting the behaviour in the UI
    @Override
    public void action() {
        dude.setCurrentBehaviourName(BehaviorName);
    }

}
