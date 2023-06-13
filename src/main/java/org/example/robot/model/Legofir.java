package org.example.robot.model;

import lejos.remote.ev3.RMIRegulatedMotor;
import org.example.mapping.Map;
import org.example.mapping.TennisBall;
import org.opencv.core.Rect;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Legofir {
    RobotState state = RobotState.IDLE;

    // Mapping

    Map map = new Map(180, 120);

    // Sensors

    // Motors
    RMIRegulatedMotor left;
    RMIRegulatedMotor right;
    RMIRegulatedMotor harvester;
    RMIRegulatedMotor balldropper;

    // Motor default values
    int defaultSpeedHarvester;
    int defaultSpeedWheel;
    int defaultSpeedBallDropper;
    int defaultAccelerationHarvester;
    int defaultAccelerationWheel;
    int defaultAccelerationBallDropper;

    boolean launched;

    String currentBehaviourName = "None";

    public long startTime;

    public Legofir() {
    }

    public void setLeft(RMIRegulatedMotor left) {
        this.left = left;
    }

    public void setRight(RMIRegulatedMotor right) {
        this.right = right;
    }

    public void setHarvester(RMIRegulatedMotor harvester) {
        this.harvester = harvester;
    }

    public void setBalldropper(RMIRegulatedMotor balldropper) {
        this.balldropper = balldropper;
    }

    public void setDefaultSpeedHarvester(int defaultSpeedHarvester) {
        this.defaultSpeedHarvester = defaultSpeedHarvester;
        try {
            harvester.setSpeed(defaultSpeedHarvester);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultSpeedWheel(int defaultSpeedWheel) {
        this.defaultSpeedWheel = defaultSpeedWheel;
        try {
            left.setSpeed(defaultSpeedWheel);
            right.setSpeed(defaultSpeedWheel);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultSpeedBallDropper(int defaultSpeedBallDropper) {
        this.defaultSpeedBallDropper = defaultSpeedBallDropper;
    }

    public void setDefaultAccelerationHarvester(int defaultAccelerationHarvester) {
        this.defaultAccelerationHarvester = defaultAccelerationHarvester;
    }

    public void setDefaultAccelerationWheel(int defaultAccelerationWheel) {
        this.defaultAccelerationWheel = defaultAccelerationWheel;
    }

    public void setDefaultAccelerationBallDropper(int defaultAccelerationBallDropper) {
        this.defaultAccelerationBallDropper = defaultAccelerationBallDropper;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public boolean isLaunched() {
        return launched;
    }

    public String getCurrentBehaviourName() {
        return currentBehaviourName;
    }

    public void setCurrentBehaviourName(String currentBehaviourName) {
        this.currentBehaviourName = currentBehaviourName;
    }

    public void moveForward(int speed){
        state=RobotState.MOVING_FORWARD;
        try {
            left.setSpeed(speed);
            right.setSpeed(speed);
            left.forward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void moveForward(){
        state=RobotState.MOVING_FORWARD;
        try {
            left.forward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void moveBackward(){
        state=RobotState.MOVING_BACKWARD;
        try {
            left.backward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void moveBackward(int speed){
        state=RobotState.MOVING_BACKWARD;
        try {
            left.setSpeed(speed);
            right.setSpeed(speed);
            left.forward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnLeft(){
        state=RobotState.TURNING_LEFT;
        try {
            left.setSpeed(50);
            right.setSpeed(50);
            left.backward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void turnLeft(int speed){
        state=RobotState.TURNING_LEFT;
        try {
            left.setSpeed(speed);
            right.setSpeed(speed);
            left.backward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnRight(int speed){
state=RobotState.TURNING_RIGHT;
        try{
            left.setSpeed(speed);
            right.setSpeed(speed);
            left.forward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void turnRight(){
        state=RobotState.TURNING_RIGHT;
        try{
            left.setSpeed(50);
            right.setSpeed(50);
            left.forward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void beginHarvester(){
        try {
            System.out.println(harvester.getSpeed());
            harvester.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void stopWheels(){
        //state=RobotState.IDLE;
        try {
            left.stop(true);
            right.stop(false);
            left.setSpeed(defaultSpeedWheel);
            right.setSpeed(defaultSpeedWheel);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void stopHarvester(){
        try {
            harvester.stop(true);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void stopBallDropper(){
        try {
            balldropper.stop(true);
        } catch (RemoteException e) {
            closePorts();
        }
    }

    public void openCheeks(){
        try{
            balldropper.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void closeCheeks(){
        try{
            balldropper.rotate(-180);
        } catch (RemoteException e) {
            stopAll();
        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopBallDropper();
    }




    public void closePorts(){
        try {
            harvester.close();
            left.close();
            right.close();
            balldropper.close();

            System.out.println("lukket alle motorer og sensorer");
        } catch (RemoteException e) {
            System.out.println("Kunne ikke lukke motorer");
        }
    }
    public boolean isMoving(){
        try {
            return left.isMoving() && right.isMoving();
        } catch (RemoteException e) {
            stopAll();
        }
        return false;
    }


    public void stopAll() {
        stopWheels();
        stopHarvester();
        stopBallDropper();
        closePorts();
    }

    public Map getMap() {
        return map;
    }

    public void addBalls(List<Rect> balls) {
        // replace old balls with new ones
        List<TennisBall> newList = new ArrayList<>();
        for (Rect ball : balls) {
            newList.add(new TennisBall((int)(ball.x+ball.width*0.5), (int)(-ball.y- ball.height*0.5), null, false, false));
        }
        map.setBalls(newList);
    }
    public void addOrangeBalls(List<Rect> balls) {
        // replace old balls with new ones
        List<TennisBall> newOrangeList = new ArrayList<>();
        for (Rect ball : balls) {
            newOrangeList.add(new TennisBall((int)(ball.x+ball.width*0.5), (int)(-ball.y- ball.height*0.5), null,false, false));
        }
        map.setOrangeBalls(newOrangeList);
    }



    public double getAngle() {
        return map.getRobotPosition().getHeadingInRadians();
    }

    public RobotState getState() {
        return state;
    }

    public void setWheelSpeed(int i) {
        try {
            left.setSpeed(i);
            right.setSpeed(i);
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void collectBall() {
        try {
            harvester.rotate(40);
            harvester.rotate(-1400);
            harvester.resetTachoCount();
        } catch (RemoteException e) {
            stopAll();
        }
    }
    public void collectBallOnce(){
        try {
            harvester.rotate(-700);
            harvester.resetTachoCount();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }
}
