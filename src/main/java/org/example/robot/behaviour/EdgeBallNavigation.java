package org.example.robot.behaviour;

import org.example.mapping.Obstacle;
import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.utility.Geometry.distanceBetweenPoints;

public class EdgeBallNavigation {
    Legofir dude;
    MyBehavior myBehavior;
    Navigation navigation;
    public EdgeBallNavigation(Legofir dude,MyBehavior myBehavior,Navigation navigation){
        this.dude=dude;
        this.myBehavior=myBehavior;
        this.navigation=navigation;
    }

    public void pickUpBallInObstacle(TennisBall nextBall, DriveTowardsBall.Position cornerPosition, DriveTowardsBall.Direction wallDirection, DriveTowardsBall.Condition condition) {
        Point intermediatePoint = findBallIntermediatePoint(nextBall, cornerPosition,condition,wallDirection);
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

    private Point findBallIntermediatePoint(TennisBall nextBall, DriveTowardsBall.Position cornerPosition, DriveTowardsBall.Condition condition, DriveTowardsBall.Direction wallDirection) {
        Obstacle obstacle = dude.getMap().getObstacle();
        switch(condition) {
            case CORNER:
                return findBallIntermediatePointInCorner(nextBall, cornerPosition);
            case WALL:
                return findBallIntermediatePointOnWall(nextBall, wallDirection);
            case OBSTACLE:
                return findBallIntermediatePointInObstacle(nextBall, obstacle,cornerPosition);
            default:
                return findBallIntermediatePointInCorner(nextBall, cornerPosition);
        }


    }

    private Point findBallIntermediatePointOnWall(TennisBall nextBall, DriveTowardsBall.Direction wallDirection) {
        switch (wallDirection) {
            case NORTH -> {
                return new Point(nextBall.getX(), nextBall.getY() - 100);
            }
            case SOUTH -> {
                return new Point(nextBall.getX(), nextBall.getY() + 100);
            }
            case EAST -> {
                return new Point(nextBall.getX() - 100, nextBall.getY());
            }
            case WEST -> {
                return new Point(nextBall.getX() + 100, nextBall.getY());
            }
            default -> {
                return new Point(nextBall.getX(), nextBall.getY() - 100);
            }
        }
    }

    private Point findBallIntermediatePointInCorner(TennisBall nextBall, DriveTowardsBall.Position cornerPosition) {
        int wayPointMargin = 125;
        switch (cornerPosition){
            case TOPLEFT -> {return new Point(nextBall.getX() + wayPointMargin, nextBall.getY() - wayPointMargin);}
            case TOPRIGHT -> {return new Point(nextBall.getX() - wayPointMargin, nextBall.getY() - wayPointMargin);}
            case BOTTOMLEFT -> {return new Point(nextBall.getX() + wayPointMargin , nextBall.getY() + wayPointMargin);}
            case BOTTOMRIGHT -> {return new Point(nextBall.getX() - wayPointMargin, nextBall.getY() + wayPointMargin);}
            default -> {return new Point(nextBall.getX() + wayPointMargin, nextBall.getY() - wayPointMargin);}
        }
    }

    private Point findBallIntermediatePointInObstacle(TennisBall nextBall, Obstacle obstacle, DriveTowardsBall.Position cornerPosition) {
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
    private void slowlyMoveTowardsBallInCorner(TennisBall nextBall) {
        Point nextBallPoint = new Point(nextBall.getX(),nextBall.getY());
        double distance = Double.MAX_VALUE;

        while(distance > 10) {
            turnTowards(nextBallPoint);
            if(distance>100){
                dude.moveForward(100);
            } else if(distance>50){
                dude.moveForward(75);
            } else {
                dude.moveForward(50);
            }
            distance=distanceBetweenPoints(nextBallPoint, new Point(dude.getMap().getRobotPosition().getFrontSideX(),dude.getMap().getRobotPosition().getFrontSideY()));
        }
        dude.stopWheels();
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
