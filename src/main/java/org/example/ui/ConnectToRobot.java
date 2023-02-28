package org.example.ui;

import lejos.remote.ev3.RemoteEV3;
import org.example.robot.Launcher;
import org.example.robot.Program;
import org.example.robot.WASDController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;

public class ConnectToRobot {
    RemoteEV3 ev3;
    JFrame jFrame = new JFrame();
    public ConnectToRobot(){
        jFrame.setSize(1000, 750);
        jFrame.setLayout(new GridLayout(5,2));
        JTextField jTextField = new JTextField("172.20.10.8");
        JTextArea jTextArea = new JTextArea("Message Terminal");
        JButton jButton = new JButton("Connect");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = "success";//connectToRobot(jTextField.getText());
                if(str.equals("success")){
                    new SelectPrograms(ev3);
                    jFrame.dispose();
                } else {
                    jTextArea.setText(str);
                }
            }
        });

        jFrame.add(jTextArea);
        jFrame.add(jButton);
        jFrame.add(jTextField);
        jFrame.setVisible(true);
    }

    public String connectToRobot(String ip){
        try{
            ev3 = new RemoteEV3(ip);
            return "success";
        } catch(Exception e){
            return e.toString();
        }
    }
}
