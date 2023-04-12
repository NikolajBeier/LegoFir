package org.example;

//import nu.pattern.OpenCV;
import org.example.mapping.Dijkstras_Algorithm;
import org.example.mapping.Map;
import org.example.mapping.TennisBall;
import org.example.robot.Legofir;
import org.example.ui.ConnectToRobot;
import org.example.ui.Visualization;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Legofir dude=null;
        JFrame jFrame = new JFrame();
        jFrame.setSize(300, 175);
        JButton visualization = new JButton("Visualization");
        JButton connect = new JButton("Connect");
        JButton camera = new JButton("Show Camera");
        jFrame.setLayout(new BorderLayout());
        visualization.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startVisualization(dude);
                jFrame.dispose();
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startConnectingToRobot(dude);
                jFrame.dispose();
            }
        });
        camera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    // Overriding existing run() method
                    @Override public void run()
                    {
                        final CameraAnalyze camera = new CameraAnalyze(dude);

                        // Start camera in thread
                        new Thread(new Runnable() {
                            @Override public void run()
                            {

                            }
                        }).start();
                    }
                });
                jFrame.dispose();
            }
        });
        JLabel header = new JLabel("GolfBot UI", SwingConstants.CENTER);
        header.setPreferredSize(new Dimension(300, 75));
        jFrame.add(header, BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 2));
        buttons.add(connect);
        buttons.add(visualization);
        buttons.add(camera);
        jFrame.add(buttons, BorderLayout.CENTER);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        jFrame.setVisible(true);
    }

        public static void startConnectingToRobot(Legofir dude) {
            ConnectToRobot connectToRobot = new ConnectToRobot(dude);
        }
        public static void startVisualization (Legofir dude) {
            Visualization visualization = new Visualization();
        }
        public static void startCameraAnalyze(Legofir dude){
            CameraAnalyze cameraAnalyze = new CameraAnalyze(dude);
        }
    }