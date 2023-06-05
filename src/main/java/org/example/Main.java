package org.example;

//import nu.pattern.OpenCV;
import org.example.camera.CameraAnalyze;
import org.example.robot.model.Legofir;
import org.example.mapping.ObjectColor;
import org.example.ui.Calibration.CalibrationTool;
import org.example.ui.ConnectToRobot;
import org.example.ui.Visualization;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.FileReader;


public class Main {
    public static Logger logger = Logger.getLogger("Main");

    static JFrame jFrame = new JFrame();
    public static void main(String[] args) {
        FileHandler fh;
        try {
            fh = new FileHandler("log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.addHandler(fh);
        Legofir dude= new Legofir();
        initializeColors();
        jFrame.setSize(300, 175);
        JButton visualization = new JButton("Visualization");
        JButton connect = new JButton("Connect");
        JButton camera = new JButton("Show Camera");
        jFrame.setLayout(new BorderLayout());
        visualization.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startVisualization(dude);
                jFrame.dispose();
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startConnectingToRobot(dude);
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
                        final CameraAnalyze camera = new CameraAnalyze(dude);

                        // Start camera in thread
                        new Thread(new Runnable() {
                            @Override public void run()
                            {

                            }
                        }).start();
                    }
                });
                jFrame.dispose();
            }
        });
        JButton calibrationTool = new JButton("Calibration Tool");
        calibrationTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start camera in thread
                EventQueue.invokeLater(new Runnable() {
                    // Overriding existing run() method
                    @Override public void run()
                    {
                        final CalibrationTool calibrationTool = new CalibrationTool();
                    }
                });
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
        buttons.add(calibrationTool);
        jFrame.add(buttons, BorderLayout.CENTER);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        jFrame.setVisible(true);
    }

        public static void startConnectingToRobot(Legofir dude) {
            ConnectToRobot connectToRobot = new ConnectToRobot(dude);
        }
        public static void startVisualization (Legofir dude) {
            Visualization visualization = new Visualization();
        }
        public static void startCameraAnalyze(Legofir dude){
            CameraAnalyze cameraAnalyze = new CameraAnalyze(dude);
        }

        private static void initializeColors(){
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/java/org/example/ui/Calibration/colors.json"));
                ObjectColor.setWhiteBall(createColor(jsonObject.get("WhiteBall")));
                ObjectColor.setOrangeBall(createColor(jsonObject.get("OrangeBall")));
                ObjectColor.setEdge(createColor(jsonObject.get("Edge")));
                ObjectColor.setBlueRobot(createColor(jsonObject.get("BlueRobot")));
                ObjectColor.setGreenRobot(createColor(jsonObject.get("GreenRobot")));
            } catch (Exception e){
                EventQueue.invokeLater(new Runnable() {
                    // Overriding existing run() method
                    @Override public void run()
                    {
                        final CalibrationTool calibrationTool = new CalibrationTool();
                    }
                });
            }
        }
        private static org.example.ui.Calibration.Color createColor(Object jsonElement){
            JSONArray jsonArray = (JSONArray) jsonElement;
            int[] colors = new int[6];
            for (int i = 0; i < colors.length; ++i) {
                colors[i] = Integer.parseInt(jsonArray.get(i).toString());
            }
            return new org.example.ui.Calibration.Color(colors[0], colors[1], colors[2], colors[3], colors[4], colors[5]);
        }
    }