package org.example.robot.behaviour;


import org.example.robot.model.Legofir;

public class DepositBalls implements MyBehavior {
    String BehaviorName = "depositBalls";
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
        return checkIfRobotIsOnPoint() && (noMoreBalls() || timerExpired());
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

    public boolean checkIfRobotIsOnPoint(){
        double errorMargin = 8;



        double distance = Math.sqrt(Math.pow(dude.getMap().getRobotPosition().getX() - dude.getMap().getWayPoint().x, 2) +
                Math.pow(dude.getMap().getRobotPosition().getY() - dude.getMap().getWayPoint().y, 2));
        return distance <= errorMargin;
    }
    public boolean noMoreBalls(){
        return dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty();
    }
    private boolean timerExpired() {
        return System.currentTimeMillis()-dude.startTime>360000;
    }
}
