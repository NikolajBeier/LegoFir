package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

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
        if(!suppressed){
            dude.stopWheels();
            dude.turnRight();
        }
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
        return false;
    }

    private Boolean checkForLeftCollision() {
        return false;
    }

    private Boolean checkForRightCollision() {
        return false;
    }

    private Boolean checkForFrontCollision() {
        double distance = dude.getMap().frontDistanceToEdge();
        if(distance < AVOID_DISTANCE){
            return true;
        }
        return false;
    }
}
