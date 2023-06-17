package robotTest;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import org.example.robot.model.Legofir;
import org.example.ui.ConnectToRobot;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class HarvesterTest {
    static String ip = "172.20.10.9";



    @Test
    public void connectingToRobotTest(){
        Legofir dude=new Legofir();
        ConnectToRobot connectToRobot = new ConnectToRobot(dude);
        connectToRobot.connectToRobot(ip);
        // Wait for 0.5 seconds
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteEV3 ev3 = connectToRobot.getEv3();

        ev3.setDefault();

        // Create the motor objects
        RMIRegulatedMotor right = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor left =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        RMIRegulatedMotor balldropper =ev3.createRegulatedMotor("C", 'M');
        System.out.println("motors connected harvester test");


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



        dude.beginHarvester();
        try {
            sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dude.stopHarvester();

        System.out.println("finished harvester test");

        dude.stopAll();



    }
}
