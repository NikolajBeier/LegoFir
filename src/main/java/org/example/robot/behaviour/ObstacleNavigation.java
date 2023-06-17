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
        } catch (NullPointerException e){
            return;
        }
        moveToIntermediatePoint(nextPoint);
    }

    private void moveToIntermediatePoint(Point nextPoint) {
        while(!myBehavior.isSuppressed()) {
            navigation.turnsTowardsWayPoint(nextPoint);
            navigation.driveTowardsWaypoint(nextPoint);
            if(isOnTopOf(nextPoint)){
                dude.stopWheels();
                return;
            }
        }
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

    public void pickUpBallInObstacle(TennisBall nextBall, DriveTowardsBall.Position cornerPosition) {
        Point intermediatePoint = findBallIntermediatePoint(nextBall, cornerPosition);
        moveToIntermediatePoint(intermediatePoint);
        slowlyMoveTowardsBallInCorner(nextBall);
        turnTowards(new Point(nextBall.getX(), nextBall.getY()));
        dude.collectBall();
        dude.moveBackward();
        long timeBefore = System.currentTimeMillis();
        while(System.currentTimeMillis() - timeBefore < 1500) {
        }
        dude.stopWheels();
    }

    private void slowlyMoveTowardsBallInCorner(TennisBall nextBall) {
        Point nextBallPoint = new Point(nextBall.getX(),nextBall.getY());
        double distance = Double.MAX_VALUE;

        while(distance > 8) {
           turnTowards(nextBallPoint);
           if(distance>100){
               dude.moveForward(100);
           } else if(distance>50){
               dude.moveForward(50);
           } else {
               dude.moveForward(15);
           }
           distance=distanceBetweenPoints(nextBallPoint, new Point(dude.getMap().getRobotPosition().getFrontSideX(),dude.getMap().getRobotPosition().getFrontSideY()));
        }
        dude.stopWheels();
    }

    private Point findBallIntermediatePoint(TennisBall nextBall, DriveTowardsBall.Position cornerPosition) {
        Obstacle obstacle = dude.getMap().getObstacle();

        switch (cornerPosition) {
            case TOPLEFT:
                return new Point(obstacle.getLeftPoint().x-100,obstacle.getTopPoint().y+100);
            case TOPRIGHT:
                return new Point(obstacle.getRightPoint().x+100,obstacle.getTopPoint().y+100);
            case BOTTOMLEFT:
                return new Point(obstacle.getLeftPoint().x-100,obstacle.getBottomPoint().y-100);
            case BOTTOMRIGHT:
                return new Point(obstacle.getRightPoint().x+100,obstacle.getBottomPoint().y-100);
            default:
                return new Point(obstacle.getRightPoint().x+100,obstacle.getBottomPoint().y-100);
        }
    }

    public void turnTowards(Point nextPoint) {

        RobotPosition currentPosition = dude.getMap().getRobotPosition();

        double nextPointX = nextPoint.x;
        double nextPointY = nextPoint.y;

        double currentAngle = dude.getAngle();
        // vektor fra currentPosition(x,y) til (nextBallX,nextBallY)
        Point Pointvector = new Point(nextPointX - currentPosition.getX(), nextPointY - currentPosition.getY());
        // Vinkel af vektor..x.
        double angleToNextPoint = Geometry.degreesOfVectorInRadians(Pointvector.x, Pointvector.y);

        if (!isApproximatelySameAngle(currentAngle,angleToNextPoint,0.1)) {
            //turn towards ball
            if (pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {
                turnLeftTowardsPoint(currentAngle, angleToNextPoint);
            } else {
                turnRightTowardsPoint(currentAngle, angleToNextPoint);
            }
        }
    }
    private boolean pointIsLeftOfRobotHeading( double currentAngle, double angleToNextPoint) {
        double oppositeAngleOfRobot;

        // Robot's heading in upper quadrant.
        if(currentAngle>0){
            oppositeAngleOfRobot=currentAngle-Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextPoint<currentAngle&&angleToNextPoint>=oppositeAngleOfRobot){
                return false;
            }
            else{
                return true;
            }
        }
        // Robot's heading in lower quadrant.
        else if(currentAngle<0){
            oppositeAngleOfRobot=currentAngle+Math.PI;
            // If ball is on the right side of the robot, return false.
            if(angleToNextPoint<currentAngle||angleToNextPoint>=oppositeAngleOfRobot){
                return false;
            }
            else{
                return true;
            }
        }
        else {
            if(angleToNextPoint>0){
                return true;
            } else return false;
        }
    }
    private boolean isApproximatelySameAngle(double robotAngle,double targetAngle, double marginDegrees){
        return ((Math.abs(robotAngle-targetAngle) < marginDegrees) || (robotAngle>Math.PI-marginDegrees/2 && targetAngle<-Math.PI+marginDegrees/2) || (robotAngle<-Math.PI+marginDegrees/2 && targetAngle>Math.PI-marginDegrees/2));
    }
    private void turnRightTowardsPoint(double currentAngle, double angleToNextPoint) {
        currentAngle = dude.getAngle();
        if (!pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {

            dude.turnRight(100);
            while(!pointIsLeftOfRobotHeading(currentAngle,angleToNextPoint) && currentAngle!= angleToNextPoint){
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.2)){
                    dude.setWheelSpeed(20);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(50);
                }
            }
            dude.stopWheels();
        }
    }
    private void turnLeftTowardsPoint(double currentAngle, double angleToNextPoint) {
        currentAngle = dude.getAngle();
        if (pointIsLeftOfRobotHeading(currentAngle, angleToNextPoint)) {
            // Turn left towards ball
            dude.turnLeft(100);
            while (pointIsLeftOfRobotHeading(currentAngle,angleToNextPoint) && currentAngle!= angleToNextPoint) {
                currentAngle = dude.getAngle();
                if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.08)){
                    dude.setWheelSpeed(5);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.2)){
                    dude.setWheelSpeed(20);
                } else if(isApproximatelySameAngle(currentAngle,angleToNextPoint,0.5)){
                    dude.setWheelSpeed(50);
                }
            }
            dude.stopWheels();
        }
    }
}
