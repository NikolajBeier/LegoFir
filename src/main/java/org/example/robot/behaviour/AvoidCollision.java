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
            while(checkForFrontCollision() && !checkForBackCollision()){}
            dude.stopWheels();
            dude.turnRight();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
            dude.moveForward();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
        }
        else if(checkForLeftCollision()){
            System.out.println("avoiding left collision");
            dude.moveBackward();
            while(checkForFrontCollision() && !checkForBackCollision()){}
            dude.stopWheels();
            dude.turnRight();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
            dude.moveForward();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
        }
        else if(checkForRightCollision()){
            System.out.println("avoiding right collision");
            dude.moveBackward();
            while(checkForFrontCollision() && !checkForBackCollision()){}
            dude.stopWheels();
            dude.turnLeft();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
            dude.moveForward();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
        }
        else if(checkForBackCollision()){
            System.out.println("avoiding back collision");
            dude.moveForward();
            while(checkForBackCollision() && !checkForFrontCollision()){}
            dude.stopWheels();
            dude.turnLeft();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
            dude.stopWheels();
            dude.moveForward();
            startTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-startTime<1500){}
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
        return collision;
    }

    private boolean checkForBackCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point oppositeHeading = new Point(-heading.x, -heading.y);
        Point leftSide = new Point(dude.getMap().getRobotPosition().leftSideX, dude.getMap().getRobotPosition().leftSideY);
        Point rightSide = new Point(dude.getMap().getRobotPosition().rightSideX, dude.getMap().getRobotPosition().rightSideY);
        double distanceLeft = dude.getMap().distanceToEdge(oppositeHeading,leftSide);
        double distanceRight = dude.getMap().distanceToEdge(oppositeHeading,rightSide);
        if(distanceRight < AVOID_DISTANCE || distanceLeft < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private boolean checkForLeftCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        Point rightHeading = new Point(heading.y, -heading.x);
        Point frontSide = new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY());
        Point backSide = new Point(dude.getMap().getRobotPosition().getBackSideX(), dude.getMap().getRobotPosition().getBackSideY());
        double distanceFront = dude.getMap().distanceToEdge(leftHeading,frontSide);
        double distanceBack = dude.getMap().distanceToEdge(rightHeading,backSide);
        if(distanceBack < AVOID_DISTANCE || distanceFront < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private boolean checkForRightCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        Point rightHeading = new Point(heading.y, -heading.x);
        Point frontSide = new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY());
        Point backSide = new Point(dude.getMap().getRobotPosition().getBackSideX(), dude.getMap().getRobotPosition().getBackSideY());
        double distanceFront = dude.getMap().distanceToEdge(rightHeading,frontSide);
        double distanceBack = dude.getMap().distanceToEdge(leftHeading,backSide);
        if(distanceBack < AVOID_DISTANCE || distanceFront < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private boolean checkForFrontCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftSide = new Point(dude.getMap().getRobotPosition().leftSideX, dude.getMap().getRobotPosition().leftSideY);
        Point rightSide = new Point(dude.getMap().getRobotPosition().rightSideX, dude.getMap().getRobotPosition().rightSideY);
        Point middle = new Point(dude.getMap().getRobotPosition().getX(), dude.getMap().getRobotPosition().getY());
        double distanceLeft = dude.getMap().distanceToEdge(heading,leftSide);
        double distanceRight = dude.getMap().distanceToEdge(heading,rightSide);
        double distanceMiddle= dude.getMap().distanceToEdge(heading,middle);
        if(distanceRight < AVOID_DISTANCE || distanceLeft < AVOID_DISTANCE || distanceMiddle < AVOID_DISTANCE){
            return true;
        }
        return false;
    }
}
