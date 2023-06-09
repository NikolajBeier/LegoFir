package org.example.robot.behaviour;


import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.robot.model.RobotState;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;


public class DriveTowardsBall implements MyBehavior {
    String BehaviorName = "DriveTowardsBall";
    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    Navigation navigation;
    BallDistanceToWall ballDistanceToWall;

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
        navigation= new Navigation(dude);
    }


    @Override
    public boolean takeControl() {
        if(stopCondition){
            System.out.println("DriveTowardsBall.takeControl() = " + false);
            return false;
        }
        System.out.println("DriveTowardsBall.takeControl() = " + true);
        return true;
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);

        while(!suppressed){
            TennisBall nextBall = dude.getMap().getNextBall();
            ballDistanceToWall = new BallDistanceToWall(nextBall);
            if (nextBall.isInCorner()){

            }else if (nextBall.getClosetsWall()<100){

            }else

            navigation.driveTowardsBall(nextBall, suppressed);
        }
        dude.stopWheels();
        dude.stopHarvester();
    }

    @Override
    public void suppress(){
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
    }

}
