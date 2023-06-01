package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.example.robot.model.RobotState;
import org.opencv.core.Point;

import static org.example.Main.logger;

public class AvoidCollision implements MyBehavior {

    String BehaviorName = "Avoid Collision";
    Legofir dude;
    boolean suppressed = false;
    final double AVOID_DISTANCE = 150;
    boolean stopCondition = false;

    public AvoidCollision(Legofir dude) {
        this.dude = dude;
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed= true;
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        logger.info("Avoiding Collision now");
        dude.stopWheels();
        dude.stopHarvester();
        long startTime = System.currentTimeMillis();

        // Evasive maneuver
        if(checkForFrontCollision()){
            System.out.println("avoiding front collision");
            dude.moveBackward();
            while(System.currentTimeMillis()-startTime > 1000){}
            dude.stopWheels();
            dude.turnRight();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
            dude.moveForward();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
        }
        else if(checkForLeftCollision()){
            System.out.println("avoiding left collision");
            dude.moveBackward();
            while(System.currentTimeMillis()-startTime > 1000){}
            dude.stopWheels();
            dude.turnRight();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
            dude.moveForward();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
        }
        else if(checkForRightCollision()){
            System.out.println("avoiding right collision");
            dude.moveBackward();
            while(System.currentTimeMillis()-startTime > 1000){}
            dude.stopWheels();
            dude.turnLeft();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
            dude.moveForward();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
        }
        else if(checkForBackCollision()){
            System.out.println("avoiding back collision");
            dude.moveForward();
            while(System.currentTimeMillis()-startTime > 1000){}
            dude.stopWheels();
            dude.turnRight();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
            dude.moveBackward();
            while(System.currentTimeMillis()-startTime > 2000){}
            dude.stopWheels();
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public boolean takeControl() {
        boolean collision = false;
        if(stopCondition){
            return false;
        }
        switch(dude.getState()){
            case IDLE:
                break;
            case MOVING_FORWARD:
                collision = checkForFrontCollision();
                break;
            case TURNING_RIGHT:
                collision = checkForRightCollision();
                break;
            case TURNING_LEFT:
                collision = checkForLeftCollision();
                break;
            case MOVING_BACKWARD:
                collision = checkForBackCollision();
                break;
        }

        System.out.println("AvoidCollision.takeControl() = " + collision);
        return collision;
    }

    private boolean checkForBackCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point oppositeHeading = new Point(-heading.x, -heading.y);
        double distance = dude.getMap().distanceToEdge(oppositeHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private boolean checkForLeftCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        double distance = dude.getMap().distanceToEdge(leftHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private boolean checkForRightCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point rightHeading = new Point(heading.y, -heading.x);
        double distance = dude.getMap().distanceToEdge(rightHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }

        return false;
    }

    private boolean checkForFrontCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();

        double distance = dude.getMap().distanceToEdge(heading);
        System.out.println("Distance to edge: " + distance);
        System.out.println("Avoid distance: "+ AVOID_DISTANCE);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }
}
