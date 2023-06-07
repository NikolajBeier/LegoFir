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
        while(!suppressed){

            navigation.drivesTowardsWayPoint(dude.getMap().getDepositPoint().getCenterRight());
            dude.openCheeks();
            dude.beginHarvester();
            try {
                sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dude.closeCheeks();
            dude.stopHarvester();
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
    // makes a square around the waypoint so that when the robot enters the square and there are no more balls on the field DepositBalls take control
    public boolean takeControl() {
        if (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y &&
                dude.getMap().getBalls().isEmpty() && dude.getMap().getOrangeBalls().isEmpty()) {
            return true;
        } else
            return false;
    }
    }