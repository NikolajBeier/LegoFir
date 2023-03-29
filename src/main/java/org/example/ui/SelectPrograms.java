package org.example.ui;

import lejos.remote.ev3.RemoteEV3;
import org.example.robot.programs.Launcher;
import org.example.robot.programs.Program;
import org.example.robot.programs.WASDController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SelectPrograms {
    Program chosenProgram;
    public SelectPrograms(RemoteEV3 ev3){
        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 750);
        jFrame.setLayout(new BorderLayout());
        ArrayList<Program> programs = new ArrayList<>();
        JLabel jLabel = new JLabel("Select a program to run!");
        jLabel.setSize(1000,75);
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

            int currentI = 0;

            JButton jButton = new JButton(str);
            jButton.addActionListener(e -> {
                chosenProgram=programs.get(currentI);
                Thread launchThread = new Thread(() -> chosenProgram.launch());
                launchThread.start();
                jButton.setEnabled(false);


                // Disconnect button
                JButton disconnectButton = new JButton("disconnect");
                disconnectButton.addActionListener(e1 -> {
                    disconnectButton.setEnabled(false);
                    jButton.setEnabled(true);
                    jFrame.revalidate();
                    jFrame.repaint();
                    Thread disconnectThread = new Thread(() -> chosenProgram.disconnect());
                    disconnectThread.start();
                });

                buttons.add(disconnectButton);
                jFrame.revalidate();
                jFrame.repaint();


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
