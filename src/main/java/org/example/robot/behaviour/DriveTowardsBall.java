package org.example.robot.behaviour;


import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;


public class DriveTowardsBall implements MyBehavior {
    String BehaviorName = "DriveTowardsBall";

    enum Condition {
            CORNER,
            WALL,
            OBSTACLE,
            DEFAULT
    }
    enum Position {
        TOPLEFT,
        TOPRIGHT,
        BOTTOMLEFT,
        BOTTOMRIGHT
    }

    Position cornerPosition = null;


    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    Navigation navigation;
    CornerNavigation cornerNavigation;
    ObstacleNavigation obstacleNavigation;

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
        navigation= new Navigation(dude,this);
        obstacleNavigation = new ObstacleNavigation(dude,this);
        cornerNavigation = new CornerNavigation(dude, this, navigation);
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
            navigation.driveTowardsBall(nextBall);
            switch(ballConditions(nextBall)){
                case CORNER:
                    cornerNavigation.driveTowardsCorner(nextBall, cornerPosition);
                    break;
                case WALL:
                    //navigation.driveTowardsWall();
                    break;
                case OBSTACLE:
                    //navigation.driveTowardsObstacle();
                    break;
                default:
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

    private Condition ballConditions(TennisBall nextBall){
        if(ballInCorner(nextBall)){
            return Condition.CORNER;
        }

        return Condition.DEFAULT;
    }

    private boolean ballInCorner(TennisBall nextBall){
        if(nextBall.getX() < dude.getMap().getEdge().getTopLeft().x + 50 && nextBall.getY() < dude.getMap().getEdge().getTopLeft().y - 50){
            //top left
            cornerPosition = Position.TOPLEFT;
            return true;
        } else if(nextBall.getX() > dude.getMap().getEdge().getTopRight().x - 50 && nextBall.getY() < dude.getMap().getEdge().getTopLeft().y - 50){
            //top right
            cornerPosition = Position.TOPRIGHT;
            return true;
        } else if(nextBall.getX() < dude.getMap().getEdge().getTopLeft().x + 50 && nextBall.getY() > dude.getMap().getEdge().getTopLeft().y + 50){
            //bottom left
            cornerPosition = Position.BOTTOMLEFT;
            return true;
        } else if(nextBall.getX() > dude.getMap().getEdge().getTopRight().x - 50 && nextBall.getY() > dude.getMap().getEdge().getTopLeft().y + 50){
            //bottom right
            cornerPosition = Position.BOTTOMRIGHT;
            return true;
        }

        return false;
    }


}
