package org.example.robot.behaviour;

import org.example.mapping.Obstacle;
import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

import java.awt.geom.Line2D;

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

        Line2D line = new Line2D.Double(robotPosition.getX(),robotPosition.getY(),point.x,point.y);

        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x,obstacle.getLeftPoint().y,obstacle.getRightPoint().x,obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x,obstacle.getTopPoint().y,obstacle.getBottomPoint().x,obstacle.getBottomPoint().y);

        if(line.intersectsLine(obstacleHorizontal) || line.intersectsLine(obstacleVertical)){
            return true;
        }
        return false;
    }

    public void moveAroundObstacle(Point nextBallPoint) {
        Point nextPoint;
        try {
            nextPoint = findIntermediatePoint(nextBallPoint);
        } catch (NullPointerException e){
            return;
        }
        moveToIntermediatePoint(nextPoint);
    }

    private void moveToIntermediatePoint(Point nextPoint) {
        while(!myBehavior.isSuppressed()) {
            navigation.turnsTowardsWayPoint(nextPoint);
            navigation.driveTowardsWaypoint(nextPoint);
        }
    }

    private Point findIntermediatePoint(Point nextBallPoint) {
        Obstacle obstacle = dude.getMap().getObstacle();
        Point robotPoint = new Point(dude.getMap().getRobotPosition().getX(),dude.getMap().getRobotPosition().getY());
        Line2D obstacleHorizontal = new Line2D.Double(obstacle.getLeftPoint().x,obstacle.getLeftPoint().y,obstacle.getRightPoint().x,obstacle.getRightPoint().y);
        Line2D obstacleVertical = new Line2D.Double(obstacle.getTopPoint().x,obstacle.getTopPoint().y,obstacle.getBottomPoint().x,obstacle.getBottomPoint().y);

        Point obstacleMiddlePoint = intersection(obstacleHorizontal,obstacleVertical);

        Point topWayPoint = new Point(obstacleMiddlePoint.x, obstacleMiddlePoint.y+200);
        Point topLeftPoint = new Point(obstacleMiddlePoint.x-200, obstacleMiddlePoint.y+200);
        Point bottomWayPoint = new Point(obstacleMiddlePoint.x, obstacleMiddlePoint.y-200);
        Point bottomLeftPoint = new Point(obstacleMiddlePoint.x-200, obstacleMiddlePoint.y-200);
        Point leftWayPoint = new Point(obstacleMiddlePoint.x-200, obstacleMiddlePoint.y);
        Point topRightPoint = new Point(obstacleMiddlePoint.x+200, obstacleMiddlePoint.y+200);
        Point rightWayPoint = new Point(obstacleMiddlePoint.x+200, obstacleMiddlePoint.y);
        Point bottomRightPoint = new Point(obstacleMiddlePoint.x+200, obstacleMiddlePoint.y-200);

        if(robotPoint.x >= obstacleMiddlePoint.x && robotPoint.y >= obstacleMiddlePoint.y) {
            // Robot moving from top right
            if(nextBallPoint.x>= obstacleMiddlePoint.x && nextBallPoint.y>=obstacleMiddlePoint.y) {
                // ball is also on top right
                return topRightPoint;
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
                return bottomRightPoint;
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
                return topLeftPoint;
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