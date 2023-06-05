package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "DepositBalls";
    Legofir dude;
    boolean suppressed = false;

    Navigation navigation = new Navigation();

    public DepositBalls(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public boolean takeControl() {
        return false;
        //Todo when robot reaches point it takes command and turns ass towards exit and starts backing up
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        while(!suppressed){
            //Todo get point
            navigation.checkDirection();
        }
    }

    @Override
    public void suppress() {

    }

    @Override
    public void setStopCondition(Boolean stopCondition) {

    }
}
