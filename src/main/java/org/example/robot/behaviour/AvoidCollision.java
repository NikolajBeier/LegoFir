package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

import static org.example.Main.logger;

public class AvoidCollision implements MyBehavior {

    String BehaviorName = "Avoid Collision";
    Legofir dude;
    boolean suppressed = false;
    final double AVOID_DISTANCE = 5;
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
        dude.turnRight();
        // Wait for avoidCollision behavior to be done
        while(!suppressed){

        }
        dude.stopWheels();
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
        //logger.info("time: "+System.currentTimeMillis()+" Checking for front collision - Distance to edge: " + distance);
        //logger.info("time: "+System.currentTimeMillis()+" Checking for front collision - Avoid distance: "+ AVOID_DISTANCE);
        //System.out.println("Distance to edge: " + distance);
        //System.out.println("Avoid distance: "+ AVOID_DISTANCE);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }
}
