package org.example.robot.behaviour;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.mapping.TennisBall;

import java.awt.*;

public class DriveTowardsCorner implements MyBehavior{
    Legofir dude;
    String BehaviorName = "DriveTowardsCorner";
    boolean suppressed = false;
    boolean stopCondition = false;
    Navigation navigation;
    TennisBall nextBall = dude.getMap().getNextBall();

    public DriveTowardsCorner(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude,this);
    }
    public boolean ballIsInCorner(){
        if (nextBall.isInCorner()){
            return true;
        }
        return false;
    }

    @Override
    public boolean takeControl() {
        if (ballIsInCorner()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        suppressed=false;
        dude.setCurrentBehaviourName(BehaviorName);
        while (!suppressed){
            navigation.turnsTowardsWayPoint(dude.getMap().getWayPoint());
            navigation.driveTowardsWaypoint(dude.getMap().getWayPoint());
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
    }

    @Override
    public boolean isSuppressed() {
        return false;
    }
}
