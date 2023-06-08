package org.example;

//import nu.pattern.OpenCV;
import org.example.robot.model.Legofir;
import org.example.mapping.ObjectColor;
import org.example.ui.Calibration.CalibrationTool;
import org.example.ui.FirstScreen;
import org.example.ui.ConnectToRobot;
import org.example.ui.Visualization;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.FileReader;

public class Main {
    public static Logger logger = Logger.getLogger("Main");

    public static void main(String[] args) {
        FileHandler fh;
        try {
            fh = new FileHandler("log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.addHandler(fh);
        final Legofir dude= new Legofir();
        initializeColors();
        FirstScreen firstScreen = new FirstScreen(dude);

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