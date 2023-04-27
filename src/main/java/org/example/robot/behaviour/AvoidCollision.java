package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class AvoidCollision implements MyBehavior {

    String BehaviorName = "Avoid Collision";
    Legofir dude;
    Boolean suppressed = false;
    final float AVOID_DISTANCE = 50f;
    Boolean stopCondition = false;

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
        Boolean collision = false;
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

    private Boolean checkForBackCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point oppositeHeading = new Point(-heading.x, -heading.y);
        double distance = dude.getMap().distanceToEdge(oppositeHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private Boolean checkForLeftCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point leftHeading = new Point(-heading.y, heading.x);
        double distance = dude.getMap().distanceToEdge(leftHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }

    private Boolean checkForRightCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point rightHeading = new Point(heading.y, -heading.x);
        double distance = dude.getMap().distanceToEdge(rightHeading);
        if(distance < AVOID_DISTANCE){
            return true;
        }

        return false;
    }

    private Boolean checkForFrontCollision() {
        Point heading = dude.getMap().getRobotPosition().getHeading();

        double distance = dude.getMap().distanceToEdge(heading);
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }
}
