package org.example.robot.behaviour;


import org.example.mapping.Edge;
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

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    Direction nearestWall = null;
    Position cornerPosition = null;


    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    Navigation navigation;
    WallNavigation wallNav;
    Edge edge;
    CornerNavigation cornerNavigation;
    ObstacleNavigation obstacleNavigation;

    public DriveTowardsBall(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude, this);
        wallNav = new WallNavigation(dude, navigation, this);
        obstacleNavigation = new ObstacleNavigation(dude, this);
        cornerNavigation = new CornerNavigation(dude, this, navigation);
    }


    @Override
    public boolean takeControl() {
        return !stopCondition;
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        while (!suppressed) {
            TennisBall nextBall = dude.getMap().getNextBall();
            Point nextBallPoint = new Point(nextBall.getX(), nextBall.getY());

            // Avoid obstacle if ball is on the other side of it.
            if (obstacleNavigation.pathToNextPointCollidesWithObstacle(nextBallPoint)) {
                obstacleNavigation.moveAroundObstacle(nextBallPoint);
            }

            switch (ballConditions(nextBall)) {
                case CORNER -> cornerNavigation.pickUpBallInCorner(nextBall, cornerPosition);
                case WALL -> wallNav.pickUpBallNextToWall(nearestWall, nextBall);
                case OBSTACLE -> obstacleNavigation.pickUpBallInObstacle(nextBall, cornerPosition);
                default -> navigation.driveTowardsBall(nextBall);
            }
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

    private Condition ballConditions(TennisBall nextBall) {
        if (ballInCorner(nextBall)) {
            return Condition.CORNER;
        } else if (ballInObstacle(nextBall)) {
            return Condition.OBSTACLE;
        } else if (ballNearWall(nextBall)) {
            return Condition.WALL;
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

        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x, obstacle.getLeftPoint().y, obstacle.getRightPoint().x, obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x, obstacle.getTopPoint().y, obstacle.getBottomPoint().x, obstacle.getBottomPoint().y);

        double middleX = intersection(obstacleHorizontal, obstacleVertical).x;
        double middleY = intersection(obstacleHorizontal, obstacleVertical).y;


        double ballX = nextBall.getX();
        double ballY = nextBall.getY();

        if (ballX > leftX - 35 && ballX < rightX + 35 && ballY > bottomY - 35 && ballY < topY + 35) {
            // The ball is within the obstacle
            if (ballX > middleX) {
                // The ball is on the right of the obstacle
                if (ballY < middleY) {
                    // The ball is in the bottom right corner of the obstacle
                    cornerPosition = Position.BOTTOMRIGHT;
                    System.out.println("bottom right");
                } else {
                    // The ball is in the top right corner of the obstacle
                    cornerPosition = Position.TOPRIGHT;
                }
            } else {
                // The ball is on the left of the obstacle
                if (ballY < middleY) {
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

    private Boolean ballNearWall(TennisBall nextBall) {
        Point topLeft = dude.getMap().getEdge().getTopLeft();
        Point topRight = dude.getMap().getEdge().getTopRight();
        Point bottomLeft = dude.getMap().getEdge().getBottomLeft();
        Point bottomRight = dude.getMap().getEdge().getBottomRight();



        double ballX = nextBall.getX();
        double ballY = nextBall.getY();

        //if (distanceToEdge>=distanceToEdge(startingPoint)){
        if (ballY > topLeft.y - 100) {
            nearestWall = Direction.NORTH;
            System.out.println("bolden er mod den nordlige væg");
            return true;

        }
        if (ballY > bottomRight.y + 100) {
            nearestWall = Direction.SOUTH;
            return true;
        }

        if (ballX < bottomLeft.x + 100) {
            nearestWall = Direction.WEST;
            return true;
        }

        if (ballX > topRight.x-100) {
            nearestWall = Direction.EAST;
            return true;
        }


            return false;
}

    private boolean ballInCorner(TennisBall nextBall) {
        if (nextBall.getX() < dude.getMap().getEdge().getTopLeft().x + 100 && nextBall.getY() > dude.getMap().getEdge().getTopLeft().y - 100) {
            //top left
            cornerPosition = Position.TOPLEFT;
            return true;
        } else if (nextBall.getX() > dude.getMap().getEdge().getTopRight().x - 100 && nextBall.getY() > dude.getMap().getEdge().getTopRight().y - 100) {
            //top right
            cornerPosition = Position.TOPRIGHT;
            return true;
        } else if (nextBall.getX() < dude.getMap().getEdge().getBottomLeft().x + 100 && nextBall.getY() < dude.getMap().getEdge().getBottomLeft().y + 100) {
            //bottom left
            cornerPosition = Position.BOTTOMLEFT;
            return true;
        } else if (nextBall.getX() > dude.getMap().getEdge().getBottomRight().x - 100 && nextBall.getY() < dude.getMap().getEdge().getBottomRight().y + 100) {
            //bottom right
            cornerPosition = Position.BOTTOMRIGHT;
            return true;
        }

        return false;
    }
}
