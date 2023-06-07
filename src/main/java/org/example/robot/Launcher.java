package org.example.robot;

import lejos.hardware.Audio;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.subsumption.Arbitrator;
import org.example.robot.behaviour.*;
import org.example.robot.model.Legofir;
import org.example.robot.music.Themes;

import static java.lang.Thread.sleep;

public class Launcher implements Program {
    RemoteEV3 ev3;
    Legofir dude;
    Arbitrator arby;
    MyBehavior[] bArray;
    Themes themes = new Themes();


    public Launcher(RemoteEV3 ev3, Legofir dude){
        this.ev3 = ev3;
        this.dude=dude;
    }
    public void setupRobot(){

        ev3.setDefault();

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        RMIRegulatedMotor balldropper =ev3.createRegulatedMotor("C", 'M');
        System.out.println("motors connected");


        // Create the sensor objects
        /*
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S1"));
        // EV3 create sensor
        RMISampleProvider ev3GyroSensor = ev3.createSampleProvider("S3", "lejos.hardware.sensor.EV3GyroSensor", "Angle");
        System.out.println("sensors connected");
         */

        // Robot object
        //dude = new Legofir(left,right,harvester, balldropper,1440,720,720,1000,1000, 1000);
        dude.setLeft(left);
        dude.setRight(right);
        dude.setHarvester(harvester);
        dude.setBalldropper(balldropper);
        dude.setDefaultSpeedHarvester(1440);
        dude.setDefaultSpeedBallDropper(5000);
        dude.setDefaultAccelerationHarvester(1000);
        dude.setDefaultAccelerationWheel(50);
        dude.setDefaultAccelerationBallDropper(5000);
        dude.setDefaultSpeedWheel(200);
        dude.setLaunched(true);

        /*
        Audio sound = ev3.getAudio();
        sound.setVolume(15);
        themes.ImperialTheme(ev3);

         */


        System.out.println("Når vi her?");

           }
    public void launchRobot() {

        bArray = new MyBehavior[]{
                new DriveTowardsBall(dude),
                new AvoidCollision(dude),
                new DriveTowardsExit(dude),
                new DropBalls(dude),
                new StopBehaviour()


        };
        arby = new Arbitrator(bArray);

        arby.go();
        System.out.println("arby stoppet");
    }

    private void imperialLaunch(Audio sound) {

    }

    @Override
    public void launch() {
        setupRobot();
        launchRobot();
    }
    @Override
    public void disconnect() {
        // set the stop condition to true, so the arbitrator will stop
        System.out.println("Forsøger at stoppe Arby");
        for(MyBehavior b : bArray){
            b.setStopCondition(true);
        }
        arby.keepRunning=false;
        System.out.println("stop condition sat til true");
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ventet på arby er stoppet");
        // Stop motors and disconnect ports
        System.out.println("forsøger at stoppe motorerer og disconnecte");
        dude.stopAll();
        System.out.println("arby stopped and ports disconnected");
    }
    public Legofir getDude() {
        return dude;
    }
}
