package org.example.ui.Calibration;

import nu.pattern.OpenCV;
import org.example.mapping.ObjectColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;

public class CalibrationTool {
    JFrame mainFrame = new JFrame();
    JSlider hueMin, hueMax, valMin, valMax, satMin, satMax;
    Color whiteBall, orangeBall, edge, blueRobot, greenRobot;
    byte[] imageData;
    ImageIcon icon;
    Mat mask;
    JLabel title = new JLabel("Configuration Tool", SwingConstants.CENTER);
    VideoCapture capture;
    Mat image;
    boolean running = true;
    boolean hlsImage = false;
    int i = 0;

    JSONObject jsonObject = new JSONObject();

    public CalibrationTool() {
        OpenCV.loadLocally();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startCalibration();
                    }
                }).start();
            }
        });
    }

    public void startCalibration(){
        capture = new VideoCapture(0);
        image = new Mat();
        mask = new Mat();
        capture.read(image);
        final MatOfByte tempBuf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, tempBuf);
        imageData = tempBuf.toArray();
        icon = new ImageIcon(imageData);
        int camWidth = icon.getIconWidth();
        int camHeight = icon.getIconHeight();
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(width, height));
        JLabel cameraScreen = new JLabel();
        JLabel maskedScreen = new JLabel();
        if(camWidth>width|| camHeight>height){
            cameraScreen.setBounds(0, 0, width, height-200);
            maskedScreen.setBounds(0, 0, width, height-200);
        } else {
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            maskedScreen.setBounds(0, 0, camWidth, camHeight);
        }

        //JSliders Configuration
        JPanel sliders = new JPanel();
        sliders.setLayout(new GridLayout(2, 6));
        hueMin = new JSlider(0, 255, 0);
        hueMax = new JSlider(0, 255, 0);
        satMin = new JSlider(0, 255, 0);
        satMax = new JSlider(0, 255, 180);
        valMin = new JSlider(0, 255, 0);
        valMax = new JSlider(0, 255, 255);
        JLabel hueMinName = new JLabel("Hue Min (B Min)");
        JLabel hueMaxName = new JLabel("Hue Max (B Max)");
        JLabel satMinName = new JLabel("Sat Min (G Min)");
        JLabel satMaxName = new JLabel("Sat Max (G Max)");
        JLabel valMinName = new JLabel("Val Min (R Min)");
        JLabel valMaxName = new JLabel("Val Max (R Max)");
        sliders.add(hueMinName);
        sliders.add(hueMaxName);
        sliders.add(satMinName);
        sliders.add(satMaxName);
        sliders.add(valMinName);
        sliders.add(valMaxName);
        sliders.add(hueMin);
        sliders.add(hueMax);
        sliders.add(satMin);
        sliders.add(satMax);
        sliders.add(valMin);
        sliders.add(valMax);
        sliders.setBounds(0, camHeight, camWidth, 100);
        mainFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                    running = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {
                    capture.release();
                    mainFrame.dispose();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        //Label
        title.setPreferredSize(new Dimension(width, 28));
        title.setFont(new Font("Comic Sans",Font.PLAIN, 24));

        //Start
        JButton start = new JButton("Start Calibration");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (i == 0) {
                    hlsImage =true;
                    title.setText("Calibrate White Balls");
                } else if (i == 1) {
                    detectWhiteBall();
                    title.setText("Calibrate Orange Balls");
                } else if (i == 2) {
                    detectOrangeBall();
                    title.setText("Calibrate Edges");
                } else if (i == 3) {
                    detectEdge();
                    title.setText("Calibrate Blue Robot Anchor");
                } else if (i == 4) {
                    detectBlueRobot();
                    title.setText("Calibrate Green Robot Anchor");
                } else if (i == 5) {
                    detectGreenRobot();
                    title.setText("Finish");
                } else {
                    try{
                        FileWriter fileWriter = new FileWriter("src/main/java/org/example/ui/Calibration/colors.json");
                        fileWriter.write(jsonObject.toJSONString());
                        fileWriter.close();
                    } catch(Exception exception){
                        exception.printStackTrace();
                    }
                    running = false;
                    mainFrame.dispose();
                    capture.release();
                }
                i++;
            }
        });
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(camWidth, camHeight));
        mainPanel.setLayout(new GridLayout(0,2));
        mainPanel.add(cameraScreen);
        mainPanel.add(maskedScreen);

        JPanel southPanel = new JPanel(new GridLayout(2,1));
        southPanel.add(sliders);
        southPanel.add(start);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.add(title, BorderLayout.NORTH);
        mainFrame.add(southPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);



        while(running){
            // read image to matrix
            capture.read(image);
            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg",image,buf);
            imageData = buf.toArray();
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
            if(hlsImage){
                Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HLS);
            } else {
                Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
            }

            Scalar minValues = new Scalar(hueMin.getValue(), satMin.getValue(), valMin.getValue());
            Scalar maxValues = new Scalar(hueMax.getValue(), satMax.getValue(), valMax.getValue());
            Core.inRange(image, minValues, maxValues, mask);

            Imgcodecs.imencode(".jpg", mask, buf);

            imageData = buf.toArray();

            // Add to JLabel
            icon = new ImageIcon(imageData);
            maskedScreen.setIcon(icon);

        }
    }
    private void capture(Color element){
        element = new Color(hueMin.getValue(), hueMax.getValue(),valMin.getValue(),valMax.getValue(),satMin.getValue(),satMax.getValue());
    }
    private void detectWhiteBall(){
        capture(whiteBall);
        ObjectColor.setWhiteBall(whiteBall);
        hlsImage =false;
        jsonObject.put("WhiteBall", createJSONArray());
    }
    private void detectOrangeBall(){
        capture(orangeBall);
        ObjectColor.setOrangeBall(orangeBall);
        jsonObject.put("OrangeBall", createJSONArray());
    }
    private void detectEdge(){
        capture(edge);
        ObjectColor.setEdge(edge);
        jsonObject.put("Edge", createJSONArray());
    }
    private void detectBlueRobot(){
        capture(blueRobot);
        ObjectColor.setBlueRobot(blueRobot);
        jsonObject.put("BlueRobot", createJSONArray());
    }
    private void detectGreenRobot(){
        capture(greenRobot);
        ObjectColor.setGreenRobot(greenRobot);
        jsonObject.put("GreenRobot", createJSONArray());
    }

    private JSONArray createJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(hueMin.getValue());
        jsonArray.add(hueMax.getValue());
        jsonArray.add(satMin.getValue());
        jsonArray.add(satMax.getValue());
        jsonArray.add(valMin.getValue());
        jsonArray.add(valMax.getValue());
        return jsonArray;
    }
}
