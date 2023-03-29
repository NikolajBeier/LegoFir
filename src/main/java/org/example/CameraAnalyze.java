
package org.example;
/*
import nu.pattern.OpenCV;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;

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
        EventQueue.invokeLater(new Runnable() {
            // Overriding existing run() method

            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!camera.setup){

                        }
                        camera.ColorDetector();
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

        public void CameraUI() {
            // Designing UI
            jFrame.setLayout(null);

            cameraScreen = new JLabel();
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            jFrame.add(cameraScreen);

            jFrame.setSize(new Dimension(camWidth, camHeight));
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
            opencv_core.CvScalar min = cvScalar(0, 0, 130, 0);//BGR-A
            opencv_core.CvScalar max= cvScalar(140, 110, 255, 0);//BGR-A


            JFrame colorJframe = new JFrame();
            // Designing UI
            colorJframe.setLayout(null);

            JLabel colorCameraScreen = new JLabel();
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
            colorJframe.add(colorCameraScreen);

            colorJframe.setSize(new Dimension(camWidth, camHeight));
            colorJframe.setLocationRelativeTo(null);
            colorJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            colorJframe.setVisible(true);

            image = new Mat();
            byte[] imageData;
            ImageIcon icon;

            while(true){
                if(capture!=null) {
                    capture.read(image);

                    image.colRange(10, 50);


                    final MatOfByte buf = new MatOfByte();
                    Imgcodecs.imencode(".jpg", image, buf);

                    imageData = buf.toArray();

                    // Add to JLabel
                    icon = new ImageIcon(imageData);

                    colorCameraScreen.setIcon(icon);
                }
            }
        }
    }
}



 */