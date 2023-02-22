package org.example;

import lejos.hardware.Audio;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;

public class WASDController {
    public void EV3Controller() throws Exception {
        JFrame jFrame = new JFrame();
        jFrame.setSize(750, 500);
        RemoteEV3 ev3 = new RemoteEV3("172.20.10.8");
        Audio sound = ev3.getAudio();
        sound.systemSound(0);
        RMIRegulatedMotor a = ev3.createRegulatedMotor("A", 'L');
        RMIRegulatedMotor b =ev3.createRegulatedMotor("D", 'L');
        RMIRegulatedMotor harvester =ev3.createRegulatedMotor("B", 'M');
        harvester.setAcceleration(1000);
        a.setAcceleration(1000);
        b.setAcceleration(1000);
        harvester.setSpeed(1080);
        a.setSpeed(1080);
        b.setSpeed(1080);
        JLabel jLabel = new JLabel("Hola");
        jFrame.setLayout(new GridLayout(5,2));


        JSlider jSpeed = new JSlider(JSlider.HORIZONTAL, 0,7200, 1080);
        JSlider jAcc = new JSlider(JSlider.HORIZONTAL, 0,7200, 1080);
        jSpeed.setMinorTickSpacing(10);
        jAcc.setMinorTickSpacing(10);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(3600, new JLabel("Speed"));

        Hashtable<Integer, JLabel> labels2 = new Hashtable<>();
        labels2.put(3600, new JLabel("Acceleration"));

        jSpeed.setLabelTable(labels);
        jAcc.setLabelTable(labels2);

        jSpeed.setPaintLabels(true);
        jAcc.setPaintLabels(true);
        jSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try{
                    harvester.setSpeed(jSpeed.getValue());
                    a.setSpeed(jSpeed.getValue());
                    b.setSpeed(jSpeed.getValue());
                }catch(Exception j){
                    j.printStackTrace();
                }
            }
        });
        jAcc.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try{
                    harvester.setAcceleration(jAcc.getValue());
                    a.setAcceleration(jAcc.getValue());
                    b.setAcceleration(jAcc.getValue());
                }catch(Exception j){
                    j.printStackTrace();
                }
            }
        });
        JButton controlRobot = new JButton("Control");
        controlRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.toFront();
                jFrame.requestFocus();
                jLabel.setText("Now controlling the robot");
            }
        });

        JButton jButton = new JButton("Exit");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    a.close();
                    b.close();
                    harvester.close();
                }catch(Exception j){
                    j.printStackTrace();
                }
                System.exit(0);
            }
        });

        jFrame.add(jLabel);
        jFrame.add(jSpeed);
        jFrame.add(jAcc);
        jFrame.add(controlRobot);
        jButton.setPreferredSize(new Dimension(100,50));
        jFrame.add(jButton);
        jFrame.addKeyListener(new KeyAdapter() {
            boolean isUpPressed = false;
            boolean isDownPressed = false;
            boolean isLeftPressed = false;
            boolean isRightPressed = false;
            boolean singleMovement = false;
            boolean multiMovement = false;
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> isUpPressed = true;
                    case KeyEvent.VK_DOWN -> isDownPressed = true;
                    case KeyEvent.VK_LEFT -> isLeftPressed = true;
                    case KeyEvent.VK_RIGHT -> isRightPressed = true;
                }
                try {
                    if(!multiMovement) {
                        if (isUpPressed && isRightPressed) {
                            jLabel.setText("Fremad + højre");
                            a.backward();
                            b.setSpeed(a.getSpeed() / 2);
                            a.forward();
                        } else if (isUpPressed && isLeftPressed) {
                            jLabel.setText("Fremad + venstre");
                            b.backward();
                            a.setSpeed(a.getSpeed() / 2);
                            a.forward();
                        }
                        multiMovement = true;
                    } else if(!singleMovement) {
                        a.setSpeed(jSpeed.getValue());
                        b.setSpeed(jSpeed.getValue());
                        if (isUpPressed) {
                            jLabel.setText("Fremad");
                            a.forward();
                            b.forward();
                        } else if (isRightPressed) {
                            jLabel.setText("Højre");
                            a.forward();
                            b.backward();
                        } else if (isLeftPressed) {
                            jLabel.setText("Venstre");
                            a.forward();
                            b.backward();
                        } else if (isDownPressed) {
                            jLabel.setText("Bagud");
                            a.forward();
                            b.forward();
                        }
                        singleMovement = true;
                    }
                }catch(Exception j) {
                    j.printStackTrace();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        if (isLeftPressed && isRightPressed) {
                            multiMovement = false;
                        }
                        singleMovement = false;
                        isUpPressed = false;
                    }
                    case KeyEvent.VK_DOWN -> {
                        singleMovement = false;
                        isDownPressed = false;
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (isUpPressed) {
                            multiMovement = false;
                        }
                        singleMovement = false;
                        isLeftPressed = false;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (isUpPressed) {
                            multiMovement = false;
                        }
                        singleMovement = false;
                        isRightPressed = false;
                    }
                }
            }});


        jFrame.setVisible(true);

    }
}
