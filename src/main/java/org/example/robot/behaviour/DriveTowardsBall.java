package org.example.robot.behaviour;


import org.example.mapping.Obstacle;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

import java.awt.geom.Line2D;

import static org.example.utility.Geometry.intersection;


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
            switch(ballConditions(nextBall)){
                case CORNER:
                    cornerNavigation.driveTowardsCorner(nextBall, cornerPosition);
                    break;
                case WALL:
                    //navigation.driveTowardsWall();
                    break;
                case OBSTACLE:
                    obstacleNavigation.pickUpBallInCorner(nextBall, cornerPosition);
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
        } else if (ballInObstacle(nextBall)){
            return Condition.OBSTACLE;
        }
        return Condition.DEFAULT;
    }

    private boolean ballInObstacle(TennisBall nextBall) {
        Obstacle obstacle = dude.getMap().getObstacle();
        double topX = obstacle.getTopPoint().x;
        double topY = obstacle.getTopPoint().y;
        double bottomX = obstacle.getBottomPoint().x;
        double bottomY = obstacle.getBottomPoint().y;
        double rightX = obstacle.getRightPoint().x;
        double rightY = obstacle.getRightPoint().y;
        double leftX = obstacle.getLeftPoint().x;
        double leftY = obstacle.getLeftPoint().y;

        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x,obstacle.getLeftPoint().y,obstacle.getRightPoint().x,obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x,obstacle.getTopPoint().y,obstacle.getBottomPoint().x,obstacle.getBottomPoint().y);

        double middleX = intersection(obstacleHorizontal,obstacleVertical).x;
        double middleY = intersection(obstacleHorizontal,obstacleVertical).y;


        double ballX = nextBall.getX();
        double ballY = nextBall.getY();

        if(ballX>leftX && ballX<rightX && ballY>bottomY && ballY<topY){
            // The ball is within the obstacle
            if(ballX>middleX){
                // The ball is on the right of the obstacle
                if(ballY<middleY){
                    // The ball is in the bottom right corner of the obstacle
                    cornerPosition = Position.BOTTOMRIGHT;
                    System.out.println("bottom right");
                } else {
                    // The ball is in the top right corner of the obstacle
                    cornerPosition = Position.TOPRIGHT;
                }
            } else {
                // The ball is on the left of the obstacle
                if(ballY<middleY){
                    // The ball is in the bottom left corner of the obstacle
                    cornerPosition = Position.BOTTOMLEFT;
                } else {
                    // The ball is in the top left corner of the obstacle
                    cornerPosition = Position.TOPLEFT;
                }
            }
            return true;
        }
        return false;
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
