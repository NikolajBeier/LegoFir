package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "DepositBalls";
    Legofir dude;
    boolean suppressed = false;

    public DepositBalls(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public boolean takeControl() {
        return false;
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
    }

    @Override
    public void suppress() {

    }

    @Override
    public void setStopCondition(Boolean stopCondition) {

    }
}
