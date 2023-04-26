package org.example.camera;

import org.example.robot.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class RobotDetection {


    public RobotDetection() {
    }

    // LOGIC
    Mat hsvImage = new Mat();
    byte[] imageData;
    ImageIcon icon;
    Mat greenMask = new Mat();
    Mat blueMask = new Mat();
    int greenHueMin = 30;
    int greenHueMax = 80;
    int greenSatMin = 30;
    int greenSatMax = 255;

    int blueHueMin = 105;
    int blueHueMax = 130;
    int blueSatMin = 50;
    int blueSatMax = 255;

    int valMin = 20;
    int valMax = 255;
    public List<Rect>[] detect(Mat image, Legofir dude) {
        List<Rect> greens = new ArrayList<>();
        List<Rect> blues = new ArrayList<>();

        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);


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

        if(!greenContour.isEmpty() || !blueContour.isEmpty()) {
            for (MatOfPoint contour : greenContour) {
                if(contourArea(contour) > 400) {
                    Rect greenBoundingRect = boundingRect(contour);
                    greens.add(greenBoundingRect);
                }
            }
            for (MatOfPoint contour : blueContour) {
                if(contourArea(contour) > 400) {
                    Rect blueBoundingRect = boundingRect(contour);
                    blues.add(blueBoundingRect);
                }
            }


            if(!greens.isEmpty() && !blues.isEmpty()) {
                for (Rect blueBoundingRect : blues) {
                    for(Rect greenBoundingRect : greens) {

                        Point blueCenter = new Point(blueBoundingRect.x + blueBoundingRect.width * 0.5, blueBoundingRect.y + blueBoundingRect.height * 0.5);
                        Point greenCenter = new Point(greenBoundingRect.x + greenBoundingRect.width * 0.5, greenBoundingRect.y + greenBoundingRect.height * 0.5);


                        Point centerOfLine = new Point((blueCenter.x + greenCenter.x) * 0.5, (blueCenter.y + greenCenter.y) * 0.5);

                        Point vectorFromBlueToGreen = new Point(greenCenter.x - blueCenter.x, greenCenter.y - blueCenter.y);
                        int lengthOfVector = (int) Math.sqrt(vectorFromBlueToGreen.x * vectorFromBlueToGreen.x + vectorFromBlueToGreen.y * vectorFromBlueToGreen.y);

                        Point perpendicularVector = new Point(vectorFromBlueToGreen.y, -vectorFromBlueToGreen.x);

                        Point arrowPoint = new Point(centerOfLine.x + perpendicularVector.x, centerOfLine.y + perpendicularVector.y);

                        if(dude.isLaunched())
                        dude.getMap().setRobotPosition((int)centerOfLine.x,(int)centerOfLine.y,perpendicularVector);
                    }


                }

            }


        }


        List<Rect>[] returnvalue = new List[2];
        returnvalue[0] = greens;
        returnvalue[1] = blues;
        return returnvalue;
    }
}