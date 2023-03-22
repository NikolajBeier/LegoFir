package org.example;

import org.example.mapping.Dijkstras_Algorithm;
import org.example.ui.ConnectToRobot;
import org.example.ui.Visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        Dijkstras_Algorithm dijkstras_algorithm=new Dijkstras_Algorithm();
        dijkstras_algorithm.toString();
        JFrame jFrame = new JFrame();
        jFrame.setSize(300, 175);
        JButton visualization = new JButton("Visualization");
        JButton connect = new JButton("Connect");
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
        JLabel header = new JLabel("GolfBot UI", SwingConstants.CENTER);
        header.setPreferredSize(new Dimension(300, 75));
        jFrame.add(header, BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 2));
        buttons.add(connect);
        buttons.add(visualization);
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
    }