package org.example.ui;

import nu.pattern.OpenCV;
import org.example.ui.Calibration.CameraCalibration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraCalibrationToolUI {
    JFrame mainFrame = new JFrame();
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    JLabel cameraScreen = new JLabel();
    CameraCalibration cameraCalibration;

    JButton detectBoard = new JButton("Detect Board");
    JButton takeSnapshot = new JButton("Take Snapshot");
    JButton calibrateCamera = new JButton("Calibrate Camera");
    JButton saveCalibration = new JButton("Save Calibration");
    JButton finishCalibration = new JButton("Finish");

    int camWidth = 1280;
    int camHeight = 960;

    public CameraCalibrationToolUI(){
        OpenCV.loadLocally();
        cameraCalibration = new CameraCalibration();
        initializeUI();
    }
    private void initializeUI(){
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(width, height));


        mainFrame.add(cameraScreen, BorderLayout.CENTER);

        takeSnapshot.setEnabled(false);
        calibrateCamera.setEnabled(false);
        saveCalibration.setEnabled(false);


        JPanel buttonPanel = new JPanel();


        detectBoard.addActionListener(e ->  {
            detectBoard.setEnabled(false);
            takeSnapshot.setEnabled(true);
            cameraCalibration.setDetect(true);
        });

        takeSnapshot.addActionListener(e ->  {
            cameraCalibration.TakeSnapShot();
            if(cameraCalibration.isSufficient()){
                takeSnapshot.setEnabled(false);
                calibrateCamera.setEnabled(true);
                cameraCalibration.setDetect(false);
            }
        });

        calibrateCamera.addActionListener(e ->  {
            calibrateCamera.setEnabled(false);
            saveCalibration.setEnabled(true);
            cameraCalibration.calibrateCam();
        });

        saveCalibration.addActionListener(e ->  {
            cameraCalibration.saveCalibration();
            buttonPanel.removeAll();
            buttonPanel.add(finishCalibration);
            mainFrame.repaint();
        });
        finishCalibration.addActionListener(e -> {
            cameraCalibration.closeCalibration();
            mainFrame.dispose();
        });

        buttonPanel.add(detectBoard);
        buttonPanel.add(takeSnapshot);
        buttonPanel.add(calibrateCamera);
        buttonPanel.add(saveCalibration);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cameraCalibration.runCalibration(cameraScreen);
                    }
                }).start();
            }
        });
    }
}
