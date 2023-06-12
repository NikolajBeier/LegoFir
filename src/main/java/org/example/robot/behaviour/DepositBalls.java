package org.example.robot.behaviour;


import org.example.robot.model.Legofir;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "DepositBalls";
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
        double errorMargin = 10;

        if(!dude.getMap().getOrangeBalls().isEmpty() && !dude.getMap().getBalls().isEmpty()){
            return false;
        }

        double distance = Math.sqrt(Math.pow(dude.getMap().getRobotPosition().getX() - dude.getMap().getWayPoint().x, 2) +
                Math.pow(dude.getMap().getRobotPosition().getY() - dude.getMap().getWayPoint().y, 2));
        return distance <= errorMargin;

        /*

        if(!(dude.getMap().getRobotPosition().getX() > dude.getMap().getWayPoint().x - 10)){
            isOnPoint = false;
        }
        if(!(dude.getMap().getRobotPosition().getX() < dude.getMap().getWayPoint().x + 10)){
            isOnPoint = false;
        }
        if(!(dude.getMap().getRobotPosition().getY() > dude.getMap().getWayPoint().y - 10)){
            isOnPoint = false;
        }
        if(!(dude.getMap().getRobotPosition().getY() < dude.getMap().getWayPoint().y + 10)){
            isOnPoint = false;
        }

        */
    }
}
