package org.example.robot.behaviour;


import org.example.mapping.Edge;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;


public class DriveTowardsBall implements MyBehavior {
    String BehaviorName = "DriveTowardsBall";



    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    Navigation navigation;
    WallNavigation wallNav;
    BallDistanceToWall ballDistanceToWall;

    Edge edge;

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
        navigation= new Navigation(dude,this);
        wallNav = new WallNavigation(dude, navigation);
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
            ballDistanceToWall = new BallDistanceToWall();
            nextBall.setClosetsWall(ballDistanceToWall.BallHeadingtoWall(nextBall, dude));

            //if (nextBall.isInCorner()){

            /*}else*/
            if (ballDistanceToWall.isCloseToWall(nextBall, dude)) {
                switch (ballDistanceToWall.BallHeadingtoWall(nextBall,dude)){
                    case NORTH -> dude.getMap().setBallNextToWallWaypoint(nextBall.getX(), nextBall.getY() - 100);
                    case SOUTH -> dude.getMap().setBallNextToWallWaypoint(nextBall.getX(), nextBall.getY() + 100);
                    case EAST -> dude.getMap().setBallNextToWallWaypoint(nextBall.getX() + 100, nextBall.getY());
                    case WEST -> dude.getMap().setBallNextToWallWaypoint(nextBall.getX() - 100, nextBall.getY());
                }


                while (!checkIfRobotIsOnPoint()) {
                    navigation.turnsTowardsWayPoint(dude.getMap().getBallNextToWallWaypoint());
                    navigation.driveTowardsWaypoint(dude.getMap().getWayPoint());
                }
                navigation.driveTowardsBall(nextBall, suppressed);
                dude.moveBackward();
            } else
                navigation.driveTowardsBall(nextBall, suppressed);
        }
        dude.stopWheels();
        dude.stopHarvester();
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
    public boolean isSuppressed() {
        return suppressed;
    }


    public Boolean checkIfRobotIsOnPoint() {
        return (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y);
    }
}
