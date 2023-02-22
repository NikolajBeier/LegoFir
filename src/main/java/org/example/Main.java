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
        sound.setVolume(10);
        sound.playTone(220, 1500);
        sound.systemSound(0);
        System.out.println("Når vi her?");
        RMIRegulatedMotor a = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor b =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        harvester.setAcceleration(1000);
        a.setAcceleration(1000);
        b.setAcceleration(1000);
        System.out.println("set acc");
        harvester.setSpeed(720);
        a.setSpeed(720);
        b.setSpeed(720);
        System.out.println("set speed");

        // behaviour

        Behavior b1 = new DriveForward(harvester, a, b);
        Behavior [] bArray = {b1};
        Arbitrator arby = new Arbitrator(bArray);
        arby.go();

        System.out.println("startet begge motorer");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        harvester.stop(true);
        b.stop(true);
        a.stop(true);

        System.out.println("stoppet begge motorer");
        harvester.close();
        a.close();
        b.close();
        System.out.println("lukket begge motorer");

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
}