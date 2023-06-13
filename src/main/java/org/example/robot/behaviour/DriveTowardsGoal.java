package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class DriveTowardsGoal implements MyBehavior{
    Legofir dude;
    String BehaviorName = "DriveTowardsGoal";
    boolean suppressed = false;
    boolean stopCondition = false;
    Navigation navigation;
    ObstacleNavigation obstacleNavigation;

    public DriveTowardsGoal(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude,this);
        obstacleNavigation = new ObstacleNavigation(dude,this);
    }

    @Override
    public boolean takeControl() {
        if((dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty()) || timerExpired()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        suppressed=false;
        dude.setCurrentBehaviourName(BehaviorName);
        if(obstacleNavigation.pathToNextPointCollidesWithObstacle(dude.getMap().getWayPoint())){
            obstacleNavigation.moveAroundObstacle(dude.getMap().getWayPoint());
        }
        while (!suppressed && takeControl()){
           navigation.driveTowardsWaypoint(dude.getMap().getWayPoint());
        }
    }

    @Override
    public void suppress() { suppressed =true; }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
        }

    @Override
    public boolean isSuppressed() {
        return false;
    }


    private boolean timerExpired() {
        return System.currentTimeMillis()-dude.startTime>420000;
    }
}

