package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import static java.lang.Thread.sleep;

public class DropBalls implements MyBehavior {

    Legofir dude;
    String BehaviorName = "DropBalls";
    boolean suppressed = false;
    boolean stopCondition = false;
    Navigation navigation;

    int test = 1;

    public DropBalls(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude);
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        if(!suppressed){

            navigation.checkDirection(dude.getMap().getDepositPoint().getCenterRight());
            dude.openCheeks();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dude.closeCheeks();
            dude.closePorts();

        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    @Override
    public boolean takeControl() {
        if (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x&&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y &&
                dude.getMap().getBalls().isEmpty() && dude.getMap().getOrangeBalls().isEmpty()) {
            return true;
        } else
            return false;
    }
    }


