package org.example.robot.behaviour;

import org.example.mapping.Obstacle;
import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import java.awt.geom.Line2D;

import static org.example.Main.logger;
import static org.example.utility.Geometry.distanceBetweenPoints;
import static org.example.utility.Geometry.intersection;

public class ObstacleNavigation {
    Legofir dude;
    MyBehavior myBehavior;
    Navigation navigation;
    public ObstacleNavigation(Legofir dude,MyBehavior myBehavior){
        this.dude=dude;
        this.myBehavior=myBehavior;
        navigation = new Navigation(dude,myBehavior);
    }

    public boolean pathToNextPointCollidesWithObstacle(Point point){

        Obstacle obstacle = dude.getMap().getObstacle();
        RobotPosition robotPosition = dude.getMap().getRobotPosition();

        Line2D middleLine = new Line2D.Double(robotPosition.getX(),robotPosition.getY(),point.x,point.y);
        Line2D rightLine = new Line2D.Double(robotPosition.rightSideX,robotPosition.rightSideY,point.x,point.y);
        Line2D leftLine = new Line2D.Double(robotPosition.leftSideX,robotPosition.leftSideY,point.x,point.y);

        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x-35,obstacle.getLeftPoint().y,obstacle.getRightPoint().x+35,obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x,obstacle.getTopPoint().y+35,obstacle.getBottomPoint().x,obstacle.getBottomPoint().y-35);

        if(middleLine.intersectsLine(obstacleHorizontal) || middleLine.intersectsLine(obstacleVertical) || rightLine.intersectsLine(obstacleHorizontal) || rightLine.intersectsLine(obstacleVertical) || leftLine.intersectsLine(obstacleHorizontal) || leftLine.intersectsLine(obstacleVertical)){
            return true;
        }
        return false;
    }

    public void moveAroundObstacle(Point nextBallPoint) {
        Point nextPoint;
        try {
            nextPoint = findIntermediatePoint(nextBallPoint);
            System.out.println("IntermediatePoint to avoid collision: x=" + nextPoint.x +" y="+nextPoint.y);
        } catch (NullPointerException e){
            return;
        }
        moveToIntermediatePoint(nextPoint);
    }

    private void moveToIntermediatePoint(Point nextPoint) {
        while(!isOnTopOf(nextPoint)) {
            navigation.turnsTowardsWayPoint(nextPoint);
            navigation.driveTowardsWaypoint(nextPoint);
        }
        dude.stopWheels();
    }

    private boolean isOnTopOf(Point nextPoint) {
        double errorMargin = 25;


        double distance = Math.sqrt(Math.pow(dude.getMap().getRobotPosition().getX() - nextPoint.x, 2) +
                Math.pow(dude.getMap().getRobotPosition().getY() - nextPoint.y, 2));
        return distance <= errorMargin;
    }

    private Point findIntermediatePoint(Point nextBallPoint) {
        Obstacle obstacle = dude.getMap().getObstacle();
        Point robotPoint = new Point(dude.getMap().getRobotPosition().getX(),dude.getMap().getRobotPosition().getY());
        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x,obstacle.getLeftPoint().y,obstacle.getRightPoint().x,obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x,obstacle.getTopPoint().y,obstacle.getBottomPoint().x,obstacle.getBottomPoint().y);

        Point obstacleMiddlePoint = intersection(obstacleHorizontal,obstacleVertical);

        Point topWayPoint = new Point(obstacleMiddlePoint.x, obstacleMiddlePoint.y+200);
        Point topLeftPoint = new Point(obstacleMiddlePoint.x-250, obstacleMiddlePoint.y+200);
        Point bottomWayPoint = new Point(obstacleMiddlePoint.x, obstacleMiddlePoint.y-200);
        Point bottomLeftPoint = new Point(obstacleMiddlePoint.x-250, obstacleMiddlePoint.y-200);
        Point leftWayPoint = new Point(obstacleMiddlePoint.x-250, obstacleMiddlePoint.y);
        Point topRightPoint = new Point(obstacleMiddlePoint.x+250, obstacleMiddlePoint.y+200);
        Point rightWayPoint = new Point(obstacleMiddlePoint.x+250, obstacleMiddlePoint.y);
        Point bottomRightPoint = new Point(obstacleMiddlePoint.x+250, obstacleMiddlePoint.y-200);

        if(robotPoint.x >= obstacleMiddlePoint.x && robotPoint.y >= obstacleMiddlePoint.y) {
            // Robot moving from top right
            if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is also on top right
                return topWayPoint;
            } else if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y<obstacleMiddlePoint.y) {
                // ball is on bottom right
                return rightWayPoint;
            } else if(nextBallPoint.x< obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is on top left
                return topWayPoint;
            } else {
                // ball is on bottom left
                return bottomRightPoint;
            }
    } else if(robotPoint.x >= obstacleMiddlePoint.x){
        // Robot moving from bottom right
            if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is also on top right
                return rightWayPoint;
            } else if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y<obstacleMiddlePoint.y) {
                // ball is on bottom right
                return rightWayPoint;
            } else if(nextBallPoint.x< obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is on top left
                return topRightPoint;
            } else {
                // ball is on bottom left
                return bottomWayPoint;
            }
    } else if(robotPoint.y >= obstacleMiddlePoint.y){
        // Robot moving from top left
            if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is also on top right
                return topWayPoint;
            } else if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y<obstacleMiddlePoint.y) {
                // ball is on bottom right
                return bottomLeftPoint;
            } else if(nextBallPoint.x< obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is on top left
                return topWayPoint;
            } else {
                // ball is on bottom left
                return leftWayPoint;
            }
    } else {
            // Robot moving from bottom left
            if (nextBallPoint.x >= obstacleMiddlePoint.x && nextBallPoint.y >= obstacleMiddlePoint.y) {
                // ball is also on top right
                return bottomRightPoint;
            } else if (nextBallPoint.x >= obstacleMiddlePoint.x && nextBallPoint.y < obstacleMiddlePoint.y) {
                // ball is on bottom right
                return bottomWayPoint;
            } else if (nextBallPoint.x < obstacleMiddlePoint.x && nextBallPoint.y >= obstacleMiddlePoint.y) {
                // ball is on top left
                return leftWayPoint;
            } else {
                // ball is on bottom left
                return bottomLeftPoint;
            }
        }
    }
}
