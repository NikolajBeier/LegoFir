package org.example.robot;

import lejos.hardware.Audio;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.behaviour.DetectCollision;
import org.example.robot.behaviour.DriveForward;
import org.example.robot.behaviour.MyBehavior;
import org.example.robot.behaviour.StopBehaviour;

public class Launcher implements Program {
    RemoteEV3 ev3;
    Legofir dude;
    Arbitrator arby;
    Boolean stopCondition=false;
    MyBehavior[] bArray;


    public Launcher(RemoteEV3 ev3){
        this.ev3 = ev3;
    }
    public void launchRobot() {
        ev3.setDefault();
        Audio sound = ev3.getAudio();
        sound.setVolume(15);
        //imperial march
        imperialLaunch(sound);



        System.out.println("Når vi her?");

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        System.out.println("motors connected");

        // Create the sensor objects
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S1"));
        System.out.println("sensors connected");

        // Robot object
        dude = new Legofir(left,right,harvester,720,720,1000,1000, ultrasonicSensor);


        bArray = new MyBehavior[]{
                new DriveForward(dude),
                new DetectCollision(dude),
        };
        arby = new Arbitrator(bArray, true);
        arby.go();
        System.out.println("arby stoppet");
    }

    private void imperialLaunch(Audio sound) {
        for (int i = 0; i < 3; i++) {
            sound.playTone(440,500);
        }
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,500);
        sound.playTone(349,350);
        sound.playTone(523,150);

        sound.playTone(440,1000);
    }

    @Override
    public void launch() {
        launchRobot();
    }


    //skal startes som sin egen thread
    @Override
    public void disconnect() {
        // set the stop condition to true, so the arbitrator will stop
        System.out.println("Forsøger at stoppe Arby");
        for(MyBehavior b : bArray){
            b.setStopCondition(true);
        }

        //vente
        // Stop motors and disconnect ports
        System.out.println("forsøger at stoppe motorerer og disconnecte");
        dude.stopAll();
        System.out.println("arby stopped and ports disconnected");
    }
}
