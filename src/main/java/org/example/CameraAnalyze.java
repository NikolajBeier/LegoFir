
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
import java.util.ArrayList;

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
        private boolean colorFilter = false;

        public boolean getDetectColor() {
            return this.detectColor;
        }

        public void CameraUI() {
            // Designing UI
            jFrame.setLayout(null);

            cameraScreen = new JLabel();
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            jFrame.add(cameraScreen);

            JPanel buttons = new JPanel(new GridLayout(0,2));
            Button colorDetection = new Button("Color Detection");
            buttons.setBounds(camWidth / 2 - 100, camHeight, 150, 40);
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
            Button colorFilterButton = new Button("Color Filter");
            colorFilterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    colorFilter = !colorFilter;
                }
            });

            buttons.add(colorFilterButton);
            buttons.add(colorDetection);
            jFrame.add(buttons);

            jFrame.setSize(new Dimension(camWidth, camHeight + 65));
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
                // read image to matrix
                capture.read(image);

                if(colorFilter){
                    //Post proccessing to smooth the image
                    Mat postImage = new Mat();

                    Imgproc.blur(image, postImage, new Size(7, 7));

                    //Revert to original picture as HSV
                    Imgproc.cvtColor(postImage, image, Imgproc.COLOR_BGR2HSV);
                }
                // convert matrix to byte
                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();

                // Add to JLabel
                icon = new ImageIcon(imageData);


                if (!setup) {
                    camHeight = icon.getIconHeight();
                    camWidth = icon.getIconWidth();
                    System.out.println(icon.getIconWidth());
                    CameraUI();
                    setup = true;
                } else {
                    cameraScreen.setIcon(icon);
                }
            }
        }

        public void ColorDetector() {
            JFrame colorJframe = new JFrame();
            // Designing UI
            colorJframe.setLayout(null);

            JLabel colorCameraScreen = new JLabel();
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
            colorJframe.add(colorCameraScreen);
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
            colorJframe.add(sliders);

            colorJframe.setSize(new Dimension(camWidth, camHeight + 130));
            colorJframe.setLocationRelativeTo(null);
            colorJframe.setVisible(true);

            Mat mask = new Mat();
            byte[] imageData;
            ImageIcon icon;

            while (true) {
                if (capture != null) {
                    //Post proccessing to smooth the image
                    Mat postImage = new Mat();

                    Imgproc.blur(image, postImage, new Size(7, 7));

                    //Revert to original picture as HSV
                    Imgproc.cvtColor(postImage, mask, Imgproc.COLOR_BGR2HSV);



                    Scalar minValues = new Scalar(hueMin.getValue(), satMin.getValue(), valMin.getValue());
                    Scalar maxValues = new Scalar(hueMax.getValue(), satMax.getValue(), valMax.getValue());
                    Core.inRange(image, minValues, maxValues, mask);

                    final MatOfByte buf = new MatOfByte();
                    Imgcodecs.imencode(".jpg", mask, buf);

                    imageData = buf.toArray();

                    // Add to JLabel
                    icon = new ImageIcon(imageData);

                    colorCameraScreen.setIcon(icon);

                    //Draw Contours (works, but needs to be implemented in the main display loop, instead of the masked display loop)
                    /*ArrayList<MatOfPoint> contours = new ArrayList<>();
                    Mat hierarchy = new Mat();

                    // find contours
                    Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

                    // if any contour exist...
                    if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
                        // for each contour, display it in blue
                        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
                            Imgproc.drawContours(image, contours, idx, new Scalar(250, 0, 0));
                        }
                    }*/
                }
            }
        }
    }
}

