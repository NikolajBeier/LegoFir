package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "DepositBalls";
    Legofir dude;
    boolean suppressed = false;

    Navigation navigation = new Navigation(dude);

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
        Point point = dude.getMap().getDepositPoint().getCenterRight();
        //Todo create real point
        point.x = point.x+50;
        while(!suppressed){
            navigation.checkDirection(point);
            dude.moveBackward();
        }
    }

    @Override
    public void suppress() {

    }

    @Override
    public void setStopCondition(Boolean stopCondition) {

    }
}
