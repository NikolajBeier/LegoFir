
package org.example.ui;

import nu.pattern.OpenCV;
import org.example.camera.*;
import org.example.mapping.RobotPosition;
import org.example.robot.model.Legofir;
import org.example.robot.model.RobotState;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.opencv.imgproc.Imgproc.arrowedLine;


public class CameraAnalyze {
    JFrame jFrame = new JFrame();
    JLabel cameraScreen = new JLabel();
    JLabel robotState = new JLabel();
    JLabel ballAmount = new JLabel();
    JLabel robotBehaviour = new JLabel();
    JLabel robotPosition = new JLabel();
    JPanel information = new JPanel(new GridLayout(0,1));
    JPanel buttons = new JPanel(new GridLayout(0, 2));
    Button robotDetectionButton = new Button("Robot Detection");
    Button ballDetectionButton = new Button("Ball Detection");
    Button edgeDetectionButton = new Button("Edge Detection");
    Button connectToRobot= new Button("Connect Robot");
    Legofir dude;
    FrameAnalyzer frameAnalyzer;
    int screenWidth= Toolkit.getDefaultToolkit().getScreenSize().width;
    int screenHeight= Toolkit.getDefaultToolkit().getScreenSize().height;
    int camWidth = 1280;
    int camHeight = 960;




    public CameraAnalyze(Legofir dude) {
        this.dude=dude;
        OpenCV.loadLocally();
        intializeUI();
        frameAnalyzer = new FrameAnalyzer(dude,this);
    }

    private void intializeUI() {
        jFrame.setLayout(new BorderLayout());

        jFrame.add(cameraScreen, BorderLayout.CENTER);
        ballDetectionButton.addActionListener(e -> frameAnalyzer.setBallDetection(!frameAnalyzer.isBallDetectionOn()));
        robotDetectionButton.addActionListener(e->frameAnalyzer.setRobotDetection(!frameAnalyzer.isRobotDetectionOn()));
        edgeDetectionButton.addActionListener(e -> frameAnalyzer.setEdgeDetection(!frameAnalyzer.isEdgeDetectionOn()));
        connectToRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> new Thread(() -> new ConnectToRobot(dude)).start());
            }
        });
        buttons.add(robotDetectionButton);
        buttons.add(ballDetectionButton);
        buttons.add(edgeDetectionButton);
        buttons.add(connectToRobot);
        information.add(robotBehaviour);
        information.add(robotState);
        information.add(ballAmount);
        information.add(robotPosition);
        jFrame.add(buttons, BorderLayout.SOUTH);
        jFrame.add(information,BorderLayout.EAST);
        jFrame.add(cameraScreen);
        jFrame.setSize(new Dimension(camWidth + 180, camHeight + 65));
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
    public void updateUI(ImageIcon imageIcon){
        String currentBehaviour = dude.getCurrentBehaviourName();
        int currentBallAmount = dude.getMap().getBalls().size();
        int currentOrangeBallAmount=dude.getMap().getOrangeBalls().size();
        RobotState currentState = dude.getState();
        RobotPosition currentPostion = dude.getMap().getRobotPosition();
        robotBehaviour.setText("Current Behaviour: " + currentBehaviour);
        robotState.setText("Current Robot State: " + currentState.name());
        ballAmount.setText("Amount of balls left: "+(currentBallAmount+currentOrangeBallAmount));
        robotPosition.setText("Current Position: x = " + currentPostion.getX() + ", y = "+ currentPostion.getY());
        cameraScreen.setIcon(imageIcon);
    }

}

