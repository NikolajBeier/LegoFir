package org.example;

import com.github.sarxos.webcam.Webcam;
//import nu.pattern.OpenCV;
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

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        JFrame jFrame = new JFrame();
        jFrame.setSize(300, 175);
        JButton visualization = new JButton("Visualization");
        JButton connect = new JButton("Connect");
        JButton camera = new JButton("Show Camera");
        jFrame.setLayout(new BorderLayout());
        visualization.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startVisualization();
                jFrame.dispose();
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startConnectingToRobot();
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
                        final Camera camera = new Camera();

                        // Start camera in thread
                        new Thread(new Runnable() {
                            @Override public void run()
                            {
                                camera.startCamera();
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

        public static void startConnectingToRobot() {
            ConnectToRobot connectToRobot = new ConnectToRobot();
        }
        public static void startVisualization () {
            Visualization visualization = new Visualization();
        }
        public static void startCameraAnalyze(){
            CameraAnalyze cameraAnalyze = new CameraAnalyze();
        }
    }