package org.example.robot.behaviour;

import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

import static org.example.utility.Geometry.distanceBetweenPoints;

public class CornerNavigation {
    Legofir dude;
    MyBehavior myBehavior;
    Navigation navigation;

    public CornerNavigation(Legofir dude, MyBehavior myBehavior, Navigation navigation) {
        this.dude=dude;
        this.myBehavior= myBehavior;
        this.navigation = navigation;
    }

    public void driveTowardsCorner(TennisBall nextBall, DriveTowardsBall.Position position) {
        Point wayPoint = new Point();
        switch (position){
            case TOPLEFT -> wayPoint = new Point(nextBall.getX() + 100, nextBall.getY() - 100);
            case TOPRIGHT -> wayPoint = new Point(nextBall.getX() - 100, nextBall.getY() - 100);
            case BOTTOMLEFT -> wayPoint = new Point(nextBall.getX() + 100 , nextBall.getY() + 100);
            case BOTTOMRIGHT -> wayPoint = new Point(nextBall.getX() - 100, nextBall.getY() + 100);
        }
        navigation.driveTowardsWaypoint(wayPoint);
        navigation.turnsTowardsWayPoint(wayPoint);
        driveIntoCorner(nextBall);

    }

    private void driveIntoCorner(TennisBall nextBall){
        while(true) {
            navigation.turnTowards(nextBall);
            double distanceToPoint = distanceBetweenPoints(new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY()), new Point(nextBall.getX(), nextBall.getY()));

            if (distanceToPoint < 10) {
                dude.collectBallOnce();
                dude.stopHarvester();
                break;
            }

            if(distanceToPoint < 50){
                dude.moveForward(25);
            } else if (distanceToPoint<100) {
                dude.moveForward(50);
            } else if (distanceToPoint<150){
                dude.moveForward(100);
            } else {
                dude.moveForward(150);
            }
        }
    }
}
