
package org.example;

import nu.pattern.OpenCV;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.bytedeco.javacpp.opencv_core.cvScalar;

public class CameraAnalyze {
    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel();
    private JLabel cameraScreen;


    public CameraAnalyze() {
        OpenCV.loadLocally();
        Camera camera = new Camera(jFrame);
        EventQueue.invokeLater(new Runnable() {
            // Overriding existing run() method

            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        camera.startCamera();
                    }
                }).start();
            }
        });
    }

    private class Camera {
        JFrame jFrame;

        public Camera(JFrame jFrame) {
            this.jFrame = jFrame;
        }
        boolean setup = false;
        // Start camera
        private VideoCapture capture;

        // Store image as 2D matrix
        private Mat image;

        private int camWidth, camHeight;

        private boolean detectColor = false;
        public boolean getDetectColor(){
            return this.detectColor;
        }

        public void CameraUI() {
            // Designing UI
            jFrame.setLayout(null);

            cameraScreen = new JLabel();
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            jFrame.add(cameraScreen);


            Button colorDetection = new Button("Color Detection");
            colorDetection.setBounds(camWidth/2-100,camHeight,150,40);
            colorDetection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        // Overriding existing run() method

                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ColorDetector();
                                }
                            }).start();
                        }
                    });
                }
            });
            jFrame.add(colorDetection);

            jFrame.setSize(new Dimension(camWidth, camHeight+65));
            jFrame.setLocationRelativeTo(null);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setVisible(true);
        }
        public void startCamera() {
            capture = new VideoCapture(0);
            image = new Mat();
            byte[] imageData;


            ImageIcon icon;

            while (true) {
                capture.read(image);
                // read image to matrix

                // convert matrix to byte
                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();

                // Add to JLabel
                icon = new ImageIcon(imageData);



                if(!setup){
                    camHeight=icon.getIconHeight();
                    camWidth=icon.getIconWidth();
                    System.out.println(icon.getIconWidth());
                    CameraUI();
                    setup=true;
                } else {
                    cameraScreen.setIcon(icon);
                }
            }
        }
        public void ColorDetector(){
            JFrame colorJframe = new JFrame();
            // Designing UI
            colorJframe.setLayout(null);

            JLabel colorCameraScreen = new JLabel();
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
            colorJframe.add(colorCameraScreen);
            JPanel sliders = new JPanel();
            sliders.setLayout(new GridLayout(2,6));
            JSlider hueMin = new JSlider(0,255,0);
            JSlider hueMax = new JSlider(0,255,0);
            JSlider satMin = new JSlider(0,255,0);
            JSlider satMax = new JSlider(0,255,180);
            JSlider valMin = new JSlider(0,255,0);
            JSlider valMax = new JSlider(0,255,255);
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
            sliders.setBounds(0,camHeight,camWidth,100);
            colorJframe.add(sliders);

            colorJframe.setSize(new Dimension(camWidth, camHeight+130));
            colorJframe.setLocationRelativeTo(null);
            colorJframe.setVisible(true);

            Mat mask = new Mat();
            byte[] imageData;
            ImageIcon icon;

            while(true){
                if(capture!=null) {
                    Scalar minValues = new Scalar(hueMin.getValue(),satMin.getValue(), valMin.getValue());
                    Scalar maxValues = new Scalar(hueMax.getValue(),satMax.getValue(),valMax.getValue());
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
    }
}

