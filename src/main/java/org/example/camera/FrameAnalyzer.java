package org.example.camera;

import org.example.mapping.Edge;
import org.example.mapping.Map;
import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.ui.CameraAnalyze;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.example.utility.Geometry.distanceBetweenPoints;
import static org.example.utility.Geometry.intersection;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class FrameAnalyzer {
    Legofir dude;
    CameraAnalyze cameraAnalyze;
    FrameDetector frameDetector;
    FrameDrawer frameDrawer;
    private VideoCapture capture;
    Mat webcamImage = new Mat();
    byte[] imageData;
    List<Mat> channels = new LinkedList<>();
    Mat destimage = new Mat();


    public FrameAnalyzer(Legofir dude, CameraAnalyze cameraAnalyze) {
        this.dude = dude;
        this.cameraAnalyze = cameraAnalyze;
        frameDetector = new FrameDetector(dude);
        frameDrawer = new FrameDrawer(dude,frameDetector);
        capture = new VideoCapture(0);

        //Starts thread which analyses and alters each frame from video capture and updates ui with the result
        EventQueue.invokeLater(() -> new Thread(() -> {
                    while (true) {
                        capture.read(webcamImage);
                        cameraAnalyze.updateUI(analyzeFrame(webcamImage));
                    }
                }).start()
        );
    }

    private ImageIcon analyzeFrame(Mat frame) {

        // resize image
        resize(webcamImage, frame, new Size(1280, 720));

        // Remove glare
        clahe(frame);

        // Find the stuff
        frameDetector.detect(frame);

        // Draw the stuff
        frameDrawer.draw(frame);

        // convert matrix to byte
        imageData = convertMatrixToByte(frame);

        return new ImageIcon(imageData);
    }
    private byte[] convertMatrixToByte(Mat frame){
        final MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, buf);
        return buf.toArray();
    }


    /*
    public void ColorDetector() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 2));
        JPanel buttonsSliders = new JPanel();
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(new Dimension(width, height));
        jFrame.getContentPane().removeAll();

        JLabel colorCameraScreen = new JLabel();
        if(camWidth>width|| camHeight>height){
            colorCameraScreen.setBounds(0, 0, width, height-200);
        } else {
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
        }
        jFrame.add(colorCameraScreen);
        JButton findValues = new JButton("Find Values");
        JPanel sliders = new JPanel();
        sliders.setLayout(new GridLayout(2, 6));
        JSlider hueMin = new JSlider(0, 255, 0);
        JSlider hueMax = new JSlider(0, 255, 0);
        JSlider satMin = new JSlider(0, 255, 0);
        JSlider satMax = new JSlider(0, 255, 180);
        JSlider valMin = new JSlider(0, 255, 0);
        JSlider valMax = new JSlider(0, 255, 255);
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
        jFrame.add(sliders);

        findValues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hue Min: "+hueMin.getValue());
                System.out.println("Hue Max: "+hueMax.getValue());
                System.out.println("Sat Min: "+satMin.getValue());
                System.out.println("Sat Max: "+satMax.getValue());
                System.out.println("Val Min: "+valMin.getValue());
                System.out.println("Val Max: "+valMax.getValue());
            }
        });
        buttonsSliders.add(findValues);


        mainPanel.setPreferredSize(new Dimension(width, height-130));
        mainPanel.add(cameraScreen);
        mainPanel.setPreferredSize(new Dimension(width, height - 130));
        mainPanel.add(cameraScreen);
        mainPanel.add(colorCameraScreen);

        buttonsSliders.setPreferredSize(new Dimension(width, 130));
        buttonsSliders.add(buttons);
        buttonsSliders.add(sliders);


        jFrame.add(mainPanel, BorderLayout.CENTER);
        jFrame.add(buttonsSliders, BorderLayout.SOUTH);
        jFrame.setLocationRelativeTo(null);
        jFrame.revalidate();
        jFrame.repaint();
        jFrame.setVisible(true);

        Mat image;
        byte[] imageData;
        ImageIcon icon;
        Mat mask = new Mat();

        while (true) {
            if (capture != null) {
                image = correctedImage;
                //Post proccessing to smooth the image


                Scalar minValues = new Scalar(hueMin.getValue(), satMin.getValue(), valMin.getValue());
                Scalar maxValues = new Scalar(hueMax.getValue(), satMax.getValue(), valMax.getValue());
                Core.inRange(image, minValues, maxValues, mask);

                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", mask, buf);

                imageData = buf.toArray();

                // Add to JLabel
                icon = new ImageIcon(imageData);

                colorCameraScreen.setIcon(icon);
            }
        }
    }
    */
    public Mat removeglare(Mat image){
        Mat corrected = new Mat();
        CLAHE rg = createCLAHE();
        rg.apply(image, corrected);
        return corrected;
    }

    private Mat clahe(Mat capture) {
        Core.split(capture,channels);
        CLAHE clahe = Imgproc.createCLAHE();
        Size point = new Size(new Point(1,1));
        clahe.setClipLimit(4);
        clahe.setTilesGridSize(point);
        clahe.apply(channels.get(0),destimage);
        Core.merge(channels,capture);
        cvtColor(capture,destimage, 1);
        clahe.collectGarbage();
        return destimage;
    }

    public void setBallDetection(boolean b) {
        frameDetector.setBallDetectionOn(b);
    }
    public boolean isBallDetectionOn() {
        return frameDetector.isBallDetectionOn();
    }

    public void setEdgeDetection(boolean b) {
        frameDetector.setEdgeDetectionOn(b);
    }

    public boolean isEdgeDetectionOn() {
        return frameDetector.isEdgeDetectionOn();
    }

    public void setRobotDetection(boolean b) {
        frameDetector.setRobotDetectionOn(b);
    }

    public boolean isRobotDetectionOn() {
        return frameDetector.isRobotDetectionOn();
    }
}
