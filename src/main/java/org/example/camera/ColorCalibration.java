package org.example.camera;

import nu.pattern.OpenCV;
import org.example.mapping.ObjectColor;
import org.example.ui.Calibration.Color;
import org.example.ui.Calibration.ColorCalibrationUI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;

import static org.opencv.imgproc.Imgproc.*;

public class ColorCalibration {
    ColorCalibrationUI colorCalibrationUI;
    private VideoCapture capture;
    Mat webcamImage;
    MatOfInt params = new MatOfInt();

    JSlider hueMin, hueMax, satMin, satMax, valMin, valMax;

    Color whiteBall, orangeBall, edge, blueRobot, greenRobot;
    Calibration calibration = Calibration.WHITE_BALL;
    Mat mask;
    public enum Calibration {
        WHITE_BALL, ORANGE_BALL, EDGE, BLUE_ROBOT, GREEN_ROBOT, FINISHED
    }
    boolean existingColorsAreSet = false;



    public ColorCalibration(ColorCalibrationUI colorCalibrationUI, JSlider hueMin, JSlider hueMax, JSlider satMin, JSlider satMax, JSlider valMin, JSlider valMax) {
        this.hueMin = hueMin;
        this.hueMax = hueMax;
        this.satMin = satMin;
        this.satMax = satMax;
        this.valMin = valMin;
        this.valMax = valMax;
        this.colorCalibrationUI = colorCalibrationUI;
        OpenCV.loadLocally();
        webcamImage = new Mat();
        mask = new Mat();
        // jpeg quality settings
        params.fromArray(Imgcodecs.IMWRITE_JPEG_QUALITY, 50);

        // depending on os for performance

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // operating system is Windows
            capture = new VideoCapture(0, Videoio.CAP_DSHOW);
        } else {
            // operating system is Mac or other
            capture = new VideoCapture(0);
        }
        
        EventQueue.invokeLater(() -> new Thread(() -> {
                    while (true) {
                        capture.read(webcamImage);
                        colorCalibrationUI.updateUI(calibrateColor(webcamImage), calibration);
                    }
                }).start()
        );
    }

    private Mat[] calibrateColor(Mat webcamImage) {
        resize(webcamImage, webcamImage, new Size(1260, 840), webcamImage.width()/2, webcamImage.height()/2, INTER_AREA);
        switch(calibration) {
            case WHITE_BALL -> calibrateWhiteBall(webcamImage);
            case ORANGE_BALL -> calibrateOrangeBall();
            case EDGE -> calibrateEdge();
            case BLUE_ROBOT -> calibrateBlueRobot();
            case GREEN_ROBOT -> calibrateGreenRobot();
            case FINISHED -> {
                return new Mat[]{webcamImage, webcamImage};
            }
        }
        if(mask.empty()){
            return new Mat[]{webcamImage, webcamImage};
        }
        return new Mat[] {webcamImage, mask};
    }
    public void nextCalibration(){
        calibration = Calibration.values()[calibration.ordinal() + 1];
        existingColorsAreSet=false;
    }

    private void calibrateGreenRobot() {
        if(!existingColorsAreSet){
            setColorValues(ObjectColor.getGreenRobot());
            existingColorsAreSet=true;
        } else {
            applyMask(Imgproc.COLOR_BGR2HSV);
            greenRobot = new Color(hueMin.getValue(), hueMax.getValue(), satMin.getValue(), satMax.getValue(), valMin.getValue(), valMax.getValue());
            ObjectColor.setGreenRobot(greenRobot);
        }
    }

    private void calibrateBlueRobot() {
        if(!existingColorsAreSet){
            setColorValues(ObjectColor.getBlueRobot());
            existingColorsAreSet=true;
        } else {
            applyMask(Imgproc.COLOR_BGR2HSV);
            blueRobot = new Color(hueMin.getValue(), hueMax.getValue(), satMin.getValue(), satMax.getValue(), valMin.getValue(), valMax.getValue());
            ObjectColor.setBlueRobot(blueRobot);
        }
    }

    private void calibrateEdge() {
        if(!existingColorsAreSet){
            setColorValues(ObjectColor.getEdge());
            existingColorsAreSet=true;
        } else {
            applyMask(0);
            edge = new Color(hueMin.getValue(), hueMax.getValue(), satMin.getValue(), satMax.getValue(), valMin.getValue(), valMax.getValue());
            ObjectColor.setEdge(edge);
        }
    }

    private void calibrateOrangeBall() {
        if(!existingColorsAreSet){
            setColorValues(ObjectColor.getOrangeBall());
            existingColorsAreSet=true;
        } else {
            applyMask(Imgproc.COLOR_BGR2HSV);
            orangeBall = new Color(hueMin.getValue(), hueMax.getValue(), satMin.getValue(), satMax.getValue(), valMin.getValue(), valMax.getValue());
            ObjectColor.setOrangeBall(orangeBall);
        }
    }

    private void calibrateWhiteBall(Mat webcamImage) {
        if(!existingColorsAreSet){
            setColorValues(ObjectColor.getWhiteBall());
            existingColorsAreSet=true;
        } else {
            applyMask(Imgproc.COLOR_BGR2HSV);
            whiteBall = new Color(hueMin.getValue(), hueMax.getValue(), satMin.getValue(), satMax.getValue(), valMin.getValue(), valMax.getValue());
            ObjectColor.setWhiteBall(whiteBall);
        }
    }
    public void applyMask(int imageType){
        Scalar minValues = new Scalar(hueMin.getValue(), satMin.getValue(), valMin.getValue());
        Scalar maxValues = new Scalar(hueMax.getValue(), satMax.getValue(), valMax.getValue());
        if(imageType!=0) {
            cvtColor(webcamImage, mask, imageType);
            Core.inRange(mask, minValues, maxValues, mask);
        } else {
            Core.inRange(webcamImage, minValues, maxValues, mask);
        }
    }
    private void setColorValues(Color color){
        try {
            hueMin.setValue(color.getHueMin());
            hueMax.setValue(color.getHueMax());
            satMin.setValue(color.getSatMin());
            satMax.setValue(color.getSatMax());
            valMin.setValue(color.getValMin());
            valMax.setValue(color.getValMax());
        } catch (Exception e){
            setDefaultColorValues();
        }
    }

    private void setDefaultColorValues() {
        hueMin.setValue(0);
        hueMax.setValue(255);
        satMin.setValue(0);
        satMax.setValue(255);
        valMin.setValue(0);
        valMax.setValue(255);
    }
    public void writeColorToJSON(){
        try{
            FileWriter fileWriter = new FileWriter("src/main/java/org/example/ui/Calibration/colors.json");

            JSONObject json = new JSONObject();
            json.put("WhiteBall", createJSONArray(ObjectColor.getWhiteBall()));
            json.put("GreenRobot", createJSONArray(ObjectColor.getGreenRobot()));
            json.put("OrangeBall",createJSONArray(ObjectColor.getOrangeBall()));
            json.put("Edge",createJSONArray(ObjectColor.getEdge()));
            json.put("BlueRobot",createJSONArray(ObjectColor.getBlueRobot()));
            fileWriter.write(json.toJSONString());
            System.out.println(json.toJSONString());
            fileWriter.close();
        } catch(Exception exception){
            exception.printStackTrace();
        }

    }

    private JSONArray createJSONArray(Color color) {

        JSONArray jsonArray = new JSONArray();

        jsonArray.add(color.getHueMin());
        jsonArray.add(color.getHueMax());
        jsonArray.add(color.getSatMin());
        jsonArray.add(color.getSatMax());
        jsonArray.add(color.getValMin());
        jsonArray.add(color.getValMax());
        return  jsonArray;
    }
}
