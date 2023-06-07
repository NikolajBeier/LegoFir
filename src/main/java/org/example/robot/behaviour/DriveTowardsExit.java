package org.example.robot.behaviour;


import org.example.robot.model.Legofir;

public class DriveTowardsExit implements MyBehavior {
    String BehaviorName = "DriveTowardsExit";
    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    double currentAngle;
    double angleToExit;
    double distanceToExit;
    Navigation navigation;


    public DriveTowardsExit(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude);
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
            System.out.println("vi når hertil DriveTowards exit");
            navigation.turnCheeksTowardsGoal(dude.getMap().getDepositPoint().getCenterLeft());

            dude.moveBackward();
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
    public Boolean checkIfRobotIsOnPoint(){
        return (dude.getMap().getRobotPosition().getX() + 25) > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y &&
                dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty();
    }
}
