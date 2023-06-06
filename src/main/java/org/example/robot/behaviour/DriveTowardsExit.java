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
    //Navigation navigation;

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
if(!isOppositeAngle()){
                if (exitIsToTheLeft()) {
                    turnLeft();
                } else {
                    turnRight();
                }}
            while(distanceToExit>50){
                dude.moveBackward();
            }

            if (distanceToExit<50){
                dude.stopWheels();
                dude.openCheeks();
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis()-startTime<10000){
                    dude.openCheeks();
                }
                setStopCondition(true);
                dude.stopAll();
            }

        }

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
                return false;
            } else {
                return true;
            }
        } else if (currentAngle<0){
            oppositeAngle = currentAngle+Math.PI;
            if (angleToExit<currentAngle || angleToExit>=oppositeAngle){
                return true;
            } else {
                return false;
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
        return ((Math.abs(currentAngle-angleToExit) < 0.2) || (currentAngle>3 && angleToExit<-3) || (currentAngle<-3 && angleToExit>3));
    }

    private void turnLeft(){
        currentAngle = dude.getAngle();
        if (exitIsToTheLeft()) {
            dude.turnLeft();
            while (!suppressed && isOppositeAngle()) {
                currentAngle = dude.getAngle();
                System.out.println("når vi her?");
            }
            dude.stopWheels();
        }
    }
    private void turnRight(){
        currentAngle = dude.getAngle();
        if (!exitIsToTheLeft()) {
            dude.turnRight();
            while (!suppressed && isOppositeAngle()) {
                currentAngle = dude.getAngle();
                System.out.println("nårviher");
            }
            dude.stopWheels();
        }
        dude.stopWheels();
    }

}
