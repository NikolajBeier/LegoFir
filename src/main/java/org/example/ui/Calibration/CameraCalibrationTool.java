package org.example.ui.Calibration;

import nu.pattern.OpenCV;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.opencv.imgproc.Imgproc.resize;

public class CameraCalibrationTool {
    JFrame mainFrame = new JFrame();
    VideoCapture capture;
    private CameraCalibration cameraCalibration;
    Mat image;
    Mat webCamImage;
    byte[] imageData;
    ImageIcon icon;
    boolean running = true;
    boolean lookForBoard = false;
    Thread detection;
    public CameraCalibrationTool() {
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
    private void startCalibration() {
        cameraCalibration = new CameraCalibration();
        mainFrame.setLayout(new BorderLayout());
        capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_BUFFERSIZE, 3);
        image = new Mat();
        webCamImage = new Mat();
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
        if (camWidth > width || camHeight > height) {
            cameraScreen.setBounds(0, 0, width, height - 200);
            maskedScreen.setBounds(0, 0, width, height - 200);
        } else {
            cameraScreen.setBounds(0, 0, camWidth, camHeight);
            maskedScreen.setBounds(0, 0, camWidth, camHeight);
        }

        mainFrame.add(cameraScreen, BorderLayout.CENTER);


        JButton detectBoard = new JButton("Detect Board");
        detectBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JButton takeSnapshot = new JButton("Take Snapshot");
        takeSnapshot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cameraCalibration.TakeSnapShot();
            }
        });
        JButton calibrateCamera = new JButton("Calibrate Camera");
        calibrateCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cameraCalibration.calibrateCam();
            }
        });
        JButton saveCalibration = new JButton("Save Calibration");
        saveCalibration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cameraCalibration.saveCalibration();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(detectBoard);
        buttonPanel.add(takeSnapshot);
        buttonPanel.add(calibrateCamera);
        buttonPanel.add(saveCalibration);

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);


        while(running){
            // read image to matrix
            capture.read(image);
            //image = Imgcodecs.imread("chessboard.jpg");

            cameraCalibration.detectBoard(image);

            if (cameraCalibration.getCalibrated())
            {
                image = cameraCalibration.undistort(image);
            }
            resize(image, image, new Size(1280, 720));
            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);

            imageData = buf.toArray();

            // Add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
        }
    }
}
