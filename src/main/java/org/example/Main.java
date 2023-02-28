package org.example;

import lejos.hardware.Audio;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {
    public static void main(String[] args) {
        try{
        RemoteEV3 ev3 = new RemoteEV3("172.20.10.8");
        ev3.setDefault();
        Audio sound = ev3.getAudio();
        sound.setVolume(15);

        for (int i = 0; i < 3; i++) {
            sound.playTone(2500, 100);
            sound.playTone(500, 100);
        }
        for (int i = 0; i < 4; i++) {
            sound.playTone(300, 200);
        }
        for (int i = 0; i < 3; i++) {
            sound.playTone(2500, 100);
            sound.playTone(100, 100);
        }
        for (int i = 0; i < 4; i++) {
            sound.playTone(300, 200);
        }
        for (int i = 0; i < 3; i++) {
            sound.playTone(2500, 100);
            sound.playTone(400, 100);
        }

        sound.playTone(300, 4000);

        //sound.systemSound(100, 4000);
        System.out.println("Når vi her?");

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');

        // Create the sensor objects
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S1"));

        // Robot object
        Legofir dude = new Legofir(left,right,harvester,720,720,1000,1000, ultrasonicSensor);

        /*
        harvester.setAcceleration(1000);
        a.setAcceleration(1000);
        b.setAcceleration(1000);
        System.out.println("set acc");
        harvester.setSpeed(720);
        a.setSpeed(720);
        b.setSpeed(720);
        System.out.println("set speed");
             */

        // behaviour
        Behavior [] bArray = {
                new DriveForward(dude),
                new DetectCollision(dude)
        };

        // Arbitrator
        Arbitrator arby = new Arbitrator(bArray);

        // start behaviour
        arby.go();



    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
}