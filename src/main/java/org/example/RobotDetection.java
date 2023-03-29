package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.findContours;

public class RobotDetection {

    JFrame jFrame;
    int camHeight;
    int camWidth;
    JLabel cameraScreen;
    VideoCapture capture;
    Mat webCamImage;
    Mat correctedImage;

    public RobotDetection(JFrame jFrame, int camHeight, int camWidth, JLabel cameraScreen, VideoCapture capture, Mat webCamImage, Mat correctedImage) {
        this.jFrame = jFrame;
        this.camHeight = camHeight;
        this.camWidth = camWidth;
        this.cameraScreen = cameraScreen;
        this.capture = capture;
        this.webCamImage = webCamImage;
        this.correctedImage=correctedImage;
    }
    public void start() {
        // UI
        JPanel mainPanel = new JPanel(new GridLayout(0,2));
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(new Dimension(width, height));
        jFrame.getContentPane().removeAll();
        JLabel robotCameraScreen = new JLabel();
        robotCameraScreen.setBounds(0, 0, camWidth, camHeight);
        jFrame.add(robotCameraScreen);

        mainPanel.setPreferredSize(new Dimension(width, height-130));
        mainPanel.add(cameraScreen);
        mainPanel.add(robotCameraScreen);

        jFrame.add(mainPanel, BorderLayout.CENTER);
        jFrame.setLocationRelativeTo(null);
        jFrame.revalidate();
        jFrame.repaint();
        jFrame.setVisible(true);

        // LOGIC
        Mat image;
        byte[] imageData;
        ImageIcon icon;
        Mat mask = new Mat();
        int hueMin = 40;
        int hueMax = 80;
        int satMin = 120;
        int satMax = 255;
        int valMin = 15;
        int valMax = 255;

        while (true) {
            if (capture != null) {
                image = correctedImage;
                //Post proccessing to smooth the image


                Scalar minValues = new Scalar(hueMin, satMin, valMin);
                Scalar maxValues = new Scalar(hueMax, satMax, valMax);
                Core.inRange(image, minValues, maxValues, mask);






                Mat blurredImage = new Mat();
                Mat hsvImage = new Mat();
                Mat morphOutput = new Mat();


                // morphological operators
// dilate with large element, erode with small ones
                Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
                Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

                Imgproc.erode(mask, morphOutput, erodeElement);
                Imgproc.erode(mask, morphOutput, erodeElement);

                Imgproc.dilate(mask, morphOutput, dilateElement);
                Imgproc.dilate(mask, morphOutput, dilateElement);


                // init
                ArrayList<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();

// find contours
                Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

// if any contour exist...
                if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
                {
                    // for each contour, display it in blue
                    for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
                    {
                        Imgproc.drawContours(webCamImage, contours, idx, new Scalar(250, 0, 0));
                    }
                }




                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", morphOutput, buf);

                imageData = buf.toArray();


                // Add to JLabel
                icon = new ImageIcon(imageData);

                robotCameraScreen.setIcon(icon);
            }
        }
    }
}
