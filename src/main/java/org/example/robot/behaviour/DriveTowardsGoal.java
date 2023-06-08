package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

public class DriveTowardsGoal implements MyBehavior{
    Legofir dude;
    String BehaviorName = "DriveTowardsGoal";
    boolean suppressed = false;
    boolean stopCondition = false;
    double currentAngle;
    double angleToLine;
    double distanceToLine;
    Navigation navigation;

    public DriveTowardsGoal(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude);
    }

    @Override
    public boolean takeControl() {
        if (dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        suppressed=false;
        dude.setCurrentBehaviourName(BehaviorName);

        while (!suppressed){

           navigation.drivesTowardsWayPoint(dude.getMap().getWayPoint());
           dude.moveForward();
        }



    }

    @Override
    public void suppress() { suppressed =true; }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
        }
}

