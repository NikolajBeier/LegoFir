package org.example.ui;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraTest extends JFrame {

        // Camera screen
        private JLabel cameraScreen;

        // Button for image capture
        private JButton btnCapture;

        // Start camera
        private VideoCapture capture;

        // Store image as 2D matrix
        private Mat image;

        private boolean clicked = false;

        public CameraTest()
        {

            // Designing UI
            setLayout(null);

            cameraScreen = new JLabel();
            cameraScreen.setBounds(0, 0, 640, 480);
            add(cameraScreen);

            btnCapture = new JButton("capture");
            btnCapture.setBounds(300, 480, 80, 40);
            add(btnCapture);

            btnCapture.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {

                    clicked = true;
                }
            });

            setSize(new Dimension(640, 560));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        // Creating a camera
        public void startCamera()
        {
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
                cameraScreen.setIcon(icon);

                // Capture and save to file
                if (clicked) {
                    // prompt for enter image name
                    String name = JOptionPane.showInputDialog(
                            this, "Enter image name");
                    if (name == null) {
                        name = new SimpleDateFormat(
                                "yyyy-mm-dd-hh-mm-ss")
                                .format(new Date(
                                        HEIGHT, WIDTH, getX()));
                    }

                    // Write to file
                    Imgcodecs.imwrite("images/" + name + ".jpg",
                            image);

                    clicked = false;
                }
            }
        }

        // Main driver method
        public static void main(String[] args) {
            OpenCV.loadLocally();
            EventQueue.invokeLater(new Runnable() {
                // Overriding existing run() method
                @Override
                public void run() {
                    final CameraTest camera = new CameraTest();

                    // Start camera in thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            camera.startCamera();
                        }
                    }).start();
                }
            });
        }
}
