
package org.example;

import nu.pattern.OpenCV;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import javax.swing.border.Border;
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
        private Mat webCamImage = new Mat();
        private Mat correctedImage = new Mat();
        private int camWidth, camHeight;

        private boolean detectColor = false;
        private boolean colorFilter = false;
        JPanel buttons;
        Button colorDetection;
        Button colorFilterButton;


        public boolean getDetectColor() {
            return this.detectColor;
        }

        public void CameraUI() {
            // Designing UI
            jFrame.setLayout(null);

            cameraScreen = new JLabel();
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            jFrame.add(cameraScreen);

            buttons = new JPanel(new GridLayout(0, 2));
            colorDetection = new Button("Color Detection");
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
            colorFilterButton = new Button("Color Filter");
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
            Mat image = new Mat();
            byte[] imageData;


            ImageIcon icon;

            while (true) {
                // read image to matrix
                capture.read(webCamImage);
                image = webCamImage;
                if (colorFilter) {
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
                    correctedImage = image;
                }



                /*
                if (mask != null) {
                    //Contours
                    ArrayList<MatOfPoint> contours = new ArrayList<>();
                    Mat hierarchy = new Mat();
                    // find contours
                    Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

                    // if any contour exist...
                    if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
                        // for each contour, display it in blue
                        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
                            Imgproc.drawContours(image, contours, idx, new Scalar(250, 0, 0));
                        }
                    }
                }*/
            }
        }

        public void ColorDetector() {
            JPanel mainPanel = new JPanel(new GridLayout(0,2));
            JPanel buttonsSliders = new JPanel();
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            jFrame.setLayout(new BorderLayout());
            jFrame.setSize(new Dimension(width, height));
            jFrame.getContentPane().removeAll();

            JLabel colorCameraScreen = new JLabel();
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
            jFrame.add(colorCameraScreen);
            JPanel sliders = new JPanel();
            sliders.setLayout(new GridLayout(2, 6));
            JSlider hueMin = new JSlider(0, 256, 0);
            JSlider hueMax = new JSlider(0, 256, 255);
            JSlider satMin = new JSlider(0, 256, 0);
            JSlider satMax = new JSlider(0, 256, 255);
            JSlider valMin = new JSlider(0, 256, 0);
            JSlider valMax = new JSlider(0, 256, 255);
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


            mainPanel.setPreferredSize(new Dimension(width, height-130));
            mainPanel.add(cameraScreen);
            mainPanel.add(colorCameraScreen);

            buttonsSliders.setPreferredSize(new Dimension(width,130));
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
    }
}

