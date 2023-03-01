package org.example.robot;

import lejos.hardware.Audio;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.behaviour.DetectCollision;
import org.example.robot.behaviour.DriveForward;

public class Launcher implements Program {
    RemoteEV3 ev3;
    Legofir dude;


    public Launcher(RemoteEV3 ev3){
        this.ev3 = ev3;
    }
    public void launchRobot() {
        ev3.setDefault();
        Audio sound = ev3.getAudio();
        sound.setVolume(15);
        //imperial march
        for (int i = 0; i < 3; i++) {
            sound.playTone(440,500);
        }
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,500);
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,1000);
     /*   int i;
        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(500, 100);
        }

        for (i = 0; i < 4; ++i) {
            sound.playTone(300, 200);
        }

        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(100, 100);
        }

        for (i = 0; i < 4; ++i) {
            sound.playTone(300, 200);
        }

        for (i = 0; i < 3; ++i) {
            sound.playTone(2500, 100);
            sound.playTone(400, 100);
        }*/

        System.out.println("Når vi her?");

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');

        // Create the sensor objects
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S1"));

        // Robot object
        dude = new Legofir(left,right,harvester,720,720,1000,1000, ultrasonicSensor);


        Behavior[] bArray = new Behavior[]{new DriveForward(dude),new DetectCollision(dude)};
        Arbitrator arby = new Arbitrator(bArray);
        arby.go();
        System.out.println("startet begge motorer");
    }

    @Override
    public void launch() {
        launchRobot();
    }
    @Override
    public void disconnect() {
        dude.stopAll();
    }
}
