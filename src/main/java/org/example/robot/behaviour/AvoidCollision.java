package org.example.robot.behaviour;

import org.example.robot.model.Legofir;

public class AvoidCollision implements MyBehavior {

    String BehaviorName = "Avoid Collision";
    Legofir dude;
    boolean suppressed = false;
    final double AVOID_DISTANCE = 150;
    boolean stopCondition = false;
    CollisionNavigation collisionNavigation;

    public AvoidCollision(Legofir dude) {
        this.dude = dude;
        collisionNavigation = new CollisionNavigation(dude,this);
    }

    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
        suppressed= true;
    }

    @Override
    public boolean isSuppressed() {
        return false;
    }

    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        dude.stopWheels();
        dude.stopHarvester();
        collisionNavigation.startAvoidingCollision();
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
                collision = collisionNavigation.isCollidingOnTheFront();
                break;
            case TURNING_RIGHT:
                collision = collisionNavigation.isCollidingOnTheRight();
                break;
            case TURNING_LEFT:
                collision = collisionNavigation.isCollidingOnTheLeft();
                break;
            case MOVING_BACKWARD:
                collision = collisionNavigation.isCollidingOnTheBack();
                break;
        }
        return collision;
    }
}
