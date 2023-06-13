/*package org.example.robot.behaviour;
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
            if (!checkIfRobotIsOnPoint()) {
                navigation.turnsTowardsWayPoint(dude.getMap().getWayPoint());
                navigation.driveTowardsWaypoint(dude.getMap().getWayPoint());
            }else {
                nextBall = dude.getMap().getNextBall();
                navigation.driveTowardsBall(nextBall);
            }


        }
        long timeBefore = System.currentTimeMillis();
        while(System.currentTimeMillis() - timeBefore < 500){
            dude.moveBackward();
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

    public Boolean checkIfRobotIsOnPoint(){
        double errorMargin = 8;

        if(dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty()){
            return false;
        }

        double distance = Math.sqrt(Math.pow(dude.getMap().getRobotPosition().getX() - dude.getMap().getWayPoint().x, 2) +
                Math.pow(dude.getMap().getRobotPosition().getY() - dude.getMap().getWayPoint().y, 2));
        return distance <= errorMargin;
    }
}*/
