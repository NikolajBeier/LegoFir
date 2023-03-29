package org.example.ui;

import lejos.remote.ev3.RemoteEV3;
import org.example.robot.programs.Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RunProgram {
    JFrame mainFrame = new JFrame();
    Thread program;
    public RunProgram(RemoteEV3 ev3, Program chosenProgram){
        program = new Thread(chosenProgram::launch);
        mainFrame.setSize(1000,750);
        mainFrame.setLayout(new BorderLayout());
        String str = chosenProgram.toString();
        str = str.substring(18);
        str = str.split("@")[0];
        mainFrame.setTitle(str);
        JPanel programView = new JPanel();
        JButton disconnect = new JButton("Disconnect");
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0,3));
        buttons.add(disconnect);
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectPrograms(ev3);
                chosenProgram.disconnect();
                mainFrame.dispose();
            }
        });
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chosenProgram.disconnect();
                System.exit(0);
            }
        });


        mainFrame.add(buttons, BorderLayout.SOUTH);
        mainFrame.add(programView, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
}
