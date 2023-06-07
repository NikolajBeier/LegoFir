package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class DepositBalls implements MyBehavior {

    // TODO Remove class when it has been scavenged for useful parts
    String BehaviorName = "DepositBalls";
    Legofir dude;
    boolean suppressed = false;

    Navigation navigation;

    public DepositBalls(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude);
    }

    //Todo when robot reaches point it takes command and turns ass towards exit and starts backing up
    @Override
    public boolean takeControl() {
// makes a square around the waypoint so that when the robot enters the square and there are no more balls on the field DepositBalls take control


            return false;
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        Point point = dude.getMap().getDepositPoint().getCenterRight();
        //Todo create real point
        point.x = point.x + 50;
        while (!suppressed) {
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
