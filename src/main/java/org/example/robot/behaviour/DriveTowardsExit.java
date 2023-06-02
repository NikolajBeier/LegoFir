package org.example.robot.behaviour;


import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;

import static org.example.utility.Geometry.distanceBetweenPoints;

public class DriveTowardsExit implements MyBehavior{
    String BehaviorName = "DriveTowardsExit";
    boolean suppressed = false;
    Legofir dude;
    boolean stopCondition = false;
    double currentAngle;
    double angleToExit;
    double distanceToExit;
    Navigation navigation = new Navigation();

    public DriveTowardsExit(Legofir dude){
        this.dude=dude;
    }


    @Override
    public boolean takeControl() {
        if (dude.getMap().getOrangeBalls().isEmpty()&&dude.getMap().getBalls().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        suppressed=false;
        dude.setCurrentBehaviourName(BehaviorName);
        while (!suppressed) {

            RobotPosition currentPosition = dude.getMap().getRobotPosition();
            Point nextExit = dude.getMap().getDepositPoint().getCenterLeft();

            currentAngle = dude.getAngle();
            distanceToExit = distanceBetweenPoints(new Point(currentPosition.getX(), currentPosition.getY()), nextExit);

            Point exitVector = new Point(nextExit.x-currentPosition.getX(), nextExit.y-currentPosition.getY());

            angleToExit = Geometry.degreesOfVectorInRadians(exitVector.x, exitVector.y);

            if (!isOppositeAngle()){
                turnLeft();
            } else {
                turnRight();
            }
        }
        dude.moveBackward();
    }


    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition= stopCondition;
        suppressed = true;

    }
    private boolean exitIsToTheLeft(){
        double oppositeAngle;

        if (currentAngle>0){
            oppositeAngle = currentAngle-Math.PI;
            if (angleToExit<currentAngle&& angleToExit>=oppositeAngle){
                return true;
            } else {
                return false;
            }
        } else if (currentAngle<0){
            oppositeAngle = currentAngle+Math.PI;
            if (angleToExit<currentAngle || angleToExit>=oppositeAngle){
                return false;
            } else {
                return true;
            }
        } else{
            if (angleToExit>0){
                return true;
            } else {
                return false;

            }
        }
    }
    private boolean isOppositeAngle(){
        return ((Math.abs(currentAngle-angleToExit))>Math.PI/2) || (Math.abs(currentAngle-angleToExit)<-Math.PI/2);
    }
    private void turnLeft(){
        currentAngle = dude.getAngle();
        dude.turnLeft();
        while (exitIsToTheLeft()&& currentAngle != angleToExit && !suppressed){
            currentAngle = dude.getAngle();
        }
        dude.stopWheels();

    }
    private void turnRight(){
        currentAngle = dude.getAngle();
        dude.turnRight();
        while (!exitIsToTheLeft()&& currentAngle != angleToExit && !suppressed){
            currentAngle = dude.getAngle();
        }
    }

}
