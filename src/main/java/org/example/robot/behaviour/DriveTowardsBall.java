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
    ObstacleNavigation obstacleNavigation;

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
        navigation= new Navigation(dude,this);
        obstacleNavigation = new ObstacleNavigation(dude,this);
    }


    @Override
    public boolean takeControl() {
        if(stopCondition){
            return false;
        }
        return true;
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        while(!suppressed){
            TennisBall nextBall = dude.getMap().getNextBall();
            Point nextBallPoint = new Point(nextBall.getX(),nextBall.getY());
            if(obstacleNavigation.pathToNextPointCollidesWithObstacle(nextBallPoint)){
                obstacleNavigation.moveAroundObstacle(nextBallPoint);
            }

            if (nextBall.isInCorner()){
                navigation.turnsTowardsWayPoint(dude.getMap().getWayPoint());
                navigation.driveTowardsWaypoint(dude.getMap().getWayPoint());
                navigation.driveTowardsBall(nextBall);
                long timeBefore= System.currentTimeMillis();
                while(System.currentTimeMillis() - timeBefore < 500){
                    dude.moveBackward();
                }
            }else {
                navigation.driveTowardsBall(nextBall);
            }
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
    public boolean isSuppressed() {
        return suppressed;
    }


}
