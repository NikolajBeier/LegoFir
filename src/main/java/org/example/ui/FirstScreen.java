package org.example.ui;

import org.example.robot.model.Legofir;
import org.example.ui.Calibration.CalibrationTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FirstScreen {
    static JFrame jFrame = new JFrame();
    JButton camera = new JButton("Begin");
    JButton configuration = new JButton("Configuration");
    JButton calibrationTool = new JButton("Calibration");
    JButton cameraCalibration = new JButton("Camera Calibration");
    JButton backToMenu = new JButton("Back To Menu");
    JLabel header = new JLabel("Jank'o Fett", SwingConstants.CENTER);
    JPanel buttons = new JPanel();

    Legofir dude;
    public FirstScreen(Legofir dude){
        this.dude=dude;
        initializeUI();
        showUI();
    }

    private void initializeUI() {
        jFrame.setSize(300, 175);
        jFrame.setLayout(new BorderLayout());
        camera.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                final CameraAnalyze camera = new CameraAnalyze(dude);
            });
            jFrame.dispose();
        });
        calibrationTool.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                final CalibrationTool calibrationTool = new CalibrationTool();
            });
        });
        cameraCalibration.addActionListener(e-> {
            EventQueue.invokeLater(()->{
                final CameraCalibrationToolUI cameraCalibrationToolUI = new CameraCalibrationToolUI();
            });
        });
        backToMenu.addActionListener(e -> {
            //Remove previous buttons
            jFrame.remove(buttons);
            buttons.removeAll();

            //Add new buttons
            buttons.add(camera);
            buttons.add(configuration);

            //Update UI
            jFrame.add(buttons);
            jFrame.revalidate();
            jFrame.repaint();
        });

        configuration.addActionListener(e-> {
            //Remove previous buttons
            jFrame.remove(buttons);
            buttons.removeAll();

            //Add new buttons
            buttons.add(cameraCalibration);
            buttons.add(calibrationTool);
            buttons.add(backToMenu);

            //Update UI
            jFrame.add(buttons);
            jFrame.revalidate();
            jFrame.repaint();
        });

        header.setPreferredSize(new Dimension(300, 75));
        buttons.setLayout(new GridLayout(0, 2));
        buttons.add(camera);
        buttons.add(configuration);
    }
    private void showUI() {
        jFrame.add(header, BorderLayout.NORTH);
        jFrame.add(buttons, BorderLayout.CENTER);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        jFrame.setVisible(true);
    }

}
