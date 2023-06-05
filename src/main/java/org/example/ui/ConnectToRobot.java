package org.example.ui;

import lejos.remote.ev3.RemoteEV3;
import org.example.robot.model.Legofir;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectToRobot {
    RemoteEV3 ev3;
    JFrame jFrame = new JFrame();
    Thread connecting;
    String messageString = "Message Terminal";
    JButton jButton;
    JButton stopConnecting;
    JTextArea jTextArea;
    Legofir dude;
    public ConnectToRobot(Legofir dude){
        this.dude=dude;
        jFrame.setSize(1000, 750);
        jFrame.setLayout(new GridLayout(5,2));
        JTextField jTextField = new JTextField("172.20.10.9");
        jTextArea = new JTextArea(messageString);
        jButton = new JButton("Connect");
        stopConnecting = new JButton("Stop connecting");
        stopConnecting.setEnabled(false);
        stopConnecting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connecting.interrupt();
                stopConnecting.setEnabled(false);
                jButton.setEnabled(true);
                jFrame.revalidate();
                jFrame.repaint();
            }
        });
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToRobot(jTextField.getText());
                stopConnecting.setEnabled(true);
                jButton.setEnabled(false);
                jFrame.revalidate();
                jFrame.repaint();
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(stopConnecting);
        buttons.add(jButton);
        buttons.setLayout(new GridLayout(0,2));

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(connecting!=null && connecting.isAlive()){
                    connecting.interrupt();
                }
                System.exit(0);
            }
        });


        jFrame.add(jTextArea);
        jFrame.add(buttons);
        jFrame.add(jTextField);
        jFrame.setVisible(true);
    }
    public void connectToRobot(String ip){
        connecting = new Thread(new Runnable() {
               // String returnValue;
                @Override
                public void run() {
                    try{
                        ev3 = new RemoteEV3(ip);
                        new SelectPrograms(ev3,dude);
                        jFrame.dispose();
                    } catch(Exception e){
                        e.printStackTrace();
                        jTextArea.setText(e.getMessage());
                        jButton.setEnabled(true);
                        stopConnecting.setEnabled(false);
                        jFrame.revalidate();
                        jFrame.repaint();
                    }
                }
            });
        connecting.start();
    }
    public RemoteEV3 getEv3() {
        return ev3;
    }
}
