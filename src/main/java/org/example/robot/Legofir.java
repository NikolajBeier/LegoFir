package org.example.robot;

import lejos.remote.ev3.RMIRegulatedMotor;
import org.example.mapping.Map;
import org.opencv.core.Rect;

import java.rmi.RemoteException;
import java.util.List;

import static java.lang.Thread.sleep;

public class Legofir {

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
    }

    public void setDefaultSpeedWheel(int defaultSpeedWheel) {
        this.defaultSpeedWheel = defaultSpeedWheel;
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

    public void moveForward(){
        try {
            left.backward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnLeft(){
        try {
            left.forward();
            right.backward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void turnRight(){
        try{
            left.backward();
            right.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void beginHarvester(){
        try {
            System.out.println(harvester.getSpeed());
            harvester.forward();
        } catch (RemoteException e) {
            stopAll();
        }
    }

    public void stopWheels(){
        try {
            left.stop(true);
            right.stop(true);
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
            balldropper.rotate(180);
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
        int numOfOldBalls = map.getBalls().size();

        for (Rect ball : balls) {
            map.addBallCord((int)(ball.x+ball.width*0.5), (int)(ball.y+ ball.height*0.5));
        }
        for (int i = 0; i < numOfOldBalls; i++) {
            map.getBalls().removeFirst();

        }

    }

    public int getAngle() {
        return map.getRobotPosition().getHeadingInDegrees();
    }
}
