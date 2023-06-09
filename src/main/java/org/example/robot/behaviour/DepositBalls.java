package org.example.robot.behaviour;


import org.example.robot.model.Legofir;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "DriveTowardsExit";
    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    double currentAngle;
    double angleToExit;
    double distanceToExit;
    Navigation navigation;


    public DepositBalls(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude,this);
    }


    @Override
    public boolean takeControl() {
        return checkIfRobotIsOnPoint();
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        while (!suppressed) {
            navigation.turnCheeksTowardsGoal(dude.getMap().getDepositPoint().getCenterLeft(),suppressed);
        }

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
    public boolean isSuppressed() {
        return false;
    }

    public Boolean checkIfRobotIsOnPoint(){
        return (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y &&
                dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty());
    }
}
