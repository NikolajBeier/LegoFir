package org.example.ui;

import lejos.remote.ev3.RemoteEV3;
import org.example.robot.Launcher;
import org.example.robot.Program;
import org.example.robot.WASDController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SelectPrograms {
    public SelectPrograms(RemoteEV3 ev3){
        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 750);
        jFrame.setLayout(new BorderLayout());
        ArrayList<Program> programs = new ArrayList<>();
        JLabel jLabel = new JLabel("Select a program to run!");
        jLabel.setSize(100,75);
        jFrame.add(jLabel, BorderLayout.CENTER, SwingConstants.CENTER);

        //Programs hardcoded
        Launcher launcher = new Launcher(ev3);
        programs.add(launcher);
        WASDController wasdController = new WASDController(ev3);
        programs.add(wasdController);

        JPanel buttons = new JPanel();
        for(int i = 0; i < programs.size(); i++){
            String str = programs.get(i).toString();
            str = str.substring(18);
            str = str.split("@")[0];
            JButton jButton = new JButton(str);
            int currentI = i;
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    programs.get(currentI).Launch();
                }
            });
            buttons.add(jButton);
        }

        buttons.setLayout(new GridLayout(0,2));
        jFrame.add(buttons, BorderLayout.SOUTH);
        jFrame.revalidate();
        jFrame.repaint();
        jFrame.setVisible(true);
    }
}
