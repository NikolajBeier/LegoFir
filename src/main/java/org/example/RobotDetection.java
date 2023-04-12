package org.example;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.opencv.imgproc.Imgproc.*;

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
        Mat hsvImage = new Mat();
        byte[] imageData;
        ImageIcon icon;
        Mat greenMask = new Mat();
        Mat blueMask = new Mat();
        int greenHueMin = 35;
        int greenHueMax = 75;
        int greenSatMin = 50;
        int greenSatMax = 255;

        int blueHueMin = 105;
        int blueHueMax = 130;
        int blueSatMin = 50;
        int blueSatMax = 255;

        int valMin = 20;
        int valMax = 255;

        while (true) {
            if (capture != null) {
                Imgproc.cvtColor(webCamImage, hsvImage, Imgproc.COLOR_BGR2HSV);


                Scalar greenMinValues = new Scalar(greenHueMin, greenSatMin, valMin);
                Scalar greenMaxValues = new Scalar(greenHueMax, greenSatMax, valMax);

                Scalar blueMinValues = new Scalar(blueHueMin, blueSatMin, valMin);
                Scalar blueMaxValues = new Scalar(blueHueMax, blueSatMax, valMax);
                Core.inRange(hsvImage, greenMinValues, greenMaxValues, greenMask);
                Core.inRange(hsvImage, blueMinValues, blueMaxValues, blueMask);




/*
                Mat morphOutput = new Mat();


                // morphological operators
                // dilate with large element, erode with small ones


                Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
                Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

                Imgproc.erode(mask, morphOutput, erodeElement);

                Imgproc.dilate(mask, morphOutput, dilateElement);



 */





                // init
                ArrayList<MatOfPoint> greenContour = new ArrayList<>();
                Mat greenHierarchy = new Mat();

                ArrayList<MatOfPoint> blueContour = new ArrayList<>();
                Mat blueHierarchy = new Mat();

// find contours
                Imgproc.findContours(greenMask, greenContour, greenHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                Imgproc.findContours(blueMask, blueContour, blueHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


// if any contour exist draw rectangle using the bounding rect
                Rect greenBoundingRect = null;
                Rect blueBoundingRect = null;
                if(!greenContour.isEmpty() || !blueContour.isEmpty()) {
                    for (MatOfPoint contour : greenContour) {
                        if(contourArea(contour) > 500) {
                            greenBoundingRect = boundingRect(contour);
                            rectangle(webCamImage,new Point(greenBoundingRect.x,greenBoundingRect.y),new Point(greenBoundingRect.x+greenBoundingRect.width,greenBoundingRect.y+greenBoundingRect.height),new Scalar(0,0,255),2);
                            putText(webCamImage, "Green", new Point(greenBoundingRect.x, greenBoundingRect.y-10), FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
                        }
                    }
                    for (MatOfPoint contour : blueContour) {
                        if(contourArea(contour) > 500) {
                            blueBoundingRect = boundingRect(contour);
                            rectangle(webCamImage,new Point(blueBoundingRect.x,blueBoundingRect.y),new Point(blueBoundingRect.x+blueBoundingRect.width,blueBoundingRect.y+blueBoundingRect.height),new Scalar(0,0,255),2);
                            putText(webCamImage, "Blue", new Point(blueBoundingRect.x, blueBoundingRect.y-10), FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
                        }
                    }

                    if(greenBoundingRect!=null && blueBoundingRect!=null){
                        Point blueCenter = new Point(blueBoundingRect.x+blueBoundingRect.width*0.5,blueBoundingRect.y+blueBoundingRect.height*0.5);
                        Point greenCenter = new Point(greenBoundingRect.x+greenBoundingRect.width*0.5,greenBoundingRect.y+greenBoundingRect.height*0.5);
                        circle(webCamImage,blueCenter,1,new Scalar(0,0,255),1);
                        circle(webCamImage,greenCenter,1,new Scalar(0,0,255),1);

                        Point centerOfLine = new Point((blueCenter.x+greenCenter.x)*0.5,(blueCenter.y+greenCenter.y)*0.5);

                        Point vectorFromBlueToGreen = new Point(greenCenter.x-blueCenter.x,greenCenter.y-blueCenter.y);
                        int lengthOfVector = (int) Math.sqrt(vectorFromBlueToGreen.x*vectorFromBlueToGreen.x+vectorFromBlueToGreen.y*vectorFromBlueToGreen.y);

                        Point perpendicularVector = new Point(vectorFromBlueToGreen.y,-vectorFromBlueToGreen.x);

                        Point arrowPoint = new Point(centerOfLine.x+perpendicularVector.x,centerOfLine.y+perpendicularVector.y);


                        circle(webCamImage,centerOfLine,2,new Scalar(0,0,255),2);


                        line(webCamImage,blueCenter,greenCenter,new Scalar(0,0,255),1);
                        arrowedLine(webCamImage,centerOfLine,arrowPoint,new Scalar(0,0,255),1);
                    }
                }


                final MatOfByte webcamBuf = new MatOfByte();
                Imgcodecs.imencode(".jpg", webCamImage, webcamBuf);
                byte[] webCamImageArray = webcamBuf.toArray();
                Icon webCamImageIcon = new ImageIcon(webCamImageArray);
                cameraScreen.setIcon(webCamImageIcon);





                final MatOfByte maskBuf = new MatOfByte();
                Imgcodecs.imencode(".jpg", blueMask, maskBuf);

                imageData = maskBuf.toArray();


                // Add to JLabel
                icon = new ImageIcon(imageData);

                robotCameraScreen.setIcon(icon);


            }
        }
    }
}
