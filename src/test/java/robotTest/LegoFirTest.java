package robotTest;
import lejos.remote.ev3.RemoteEV3;
import org.example.robot.Launcher;
import org.example.robot.Legofir;
import org.example.ui.ConnectToRobot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;


// These tests demand that the robot is connected to the same wifi as the computer where the test is being run (not DTUSecure)
// When running the tests, the ip of the robot should be entered in the console
// Tests should be run in conjunction with observing the robot, since the turning tests actually only test if the robot is moving and
// not if it is turning in the right direction
public class LegoFirTest {
    static String ip = "172.20.10.9";



    @Test
    public void connectingToRobotTest(){
        ConnectToRobot connectToRobot = new ConnectToRobot();
        connectToRobot.connectToRobot(ip);
        // Wait for 0.5 seconds
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteEV3 ev3 = connectToRobot.getEv3();
        assertEquals("DF6", ev3.getName());
    }
    @Test
    public void driveForwardTest() {

        // Connect to robot
        ConnectToRobot connectToRobot = new ConnectToRobot();
        connectToRobot.connectToRobot(ip);
        // Wait for 0.5 seconds
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteEV3 ev3 = connectToRobot.getEv3();

        // Create launcher and robot object
        Launcher launcher = new Launcher(ev3);
        launcher.setupRobot();
        Legofir robot = launcher.getDude();

        // Drive forward
        robot.moveForward();

        // Wait for 1 seconds
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(robot.isMoving());


        // Stop robot and close ports
        robot.stopAll();
    }
    @Test
    public void turnRightTest() {
        // Connect to robot
        ConnectToRobot connectToRobot = new ConnectToRobot();
        connectToRobot.connectToRobot(ip);
        // Wait for 0.5 seconds
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteEV3 ev3 = connectToRobot.getEv3();

        // Create launcher and robot object
        Launcher launcher = new Launcher(ev3);
        launcher.setupRobot();
        Legofir robot = launcher.getDude();

        // Drive right
        robot.turnRight();

        // Wait for 0.5 seconds
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(robot.isMoving());


        // Stop robot and close ports
        robot.stopAll();
    }
}
