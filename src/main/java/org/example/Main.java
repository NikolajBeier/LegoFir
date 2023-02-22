package org.example;

import lejos.hardware.Audio;
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

            RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
            RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
            RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');

            Legofir dude = new Legofir(left,right,harvester,720,720,1000,1000);

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

        Behavior b1 = new DriveForward(dude);
        // Behavior b2 = new DetectCollision(dude);
        Behavior [] bArray = {b1};
        Arbitrator arby = new Arbitrator(bArray);
        arby.go();

        System.out.println("startet begge motorer");


    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
}