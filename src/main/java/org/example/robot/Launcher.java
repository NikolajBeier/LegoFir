package org.example.robot;

import lejos.hardware.Audio;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.behaviour.DetectCollision;
import org.example.robot.behaviour.DriveForward;
import org.example.robot.behaviour.DropBalls;

public class Launcher implements Program {
    RemoteEV3 ev3;
    Legofir dude;
    Themes theme = new Themes();


    public Launcher(RemoteEV3 ev3){
        this.ev3 = ev3;
    }
    public void launchRobot() {
        ev3.setDefault();


        theme.TetrisTheme(ev3);


        System.out.println("Når vi her?");

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        RMIRegulatedMotor balldropper =ev3.createRegulatedMotor("C", 'M');

        // Create the sensor objects
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S1"));

        // Robot object
        dude = new Legofir(left,right,harvester, balldropper,720,720,720,1000,1000, 1000, ultrasonicSensor);


        Behavior[] bArray = new Behavior[]{/*new DriveForward(dude),new DetectCollision(dude),*/ new DropBalls(dude)};
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
