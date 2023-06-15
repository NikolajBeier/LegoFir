package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class RobotDetection {




    // LOGIC
    Mat hsvImage = new Mat();
    byte[] imageData;
    ImageIcon icon;
    Mat greenMask = new Mat();
    Mat blueMask = new Mat();
    int greenHueMin = ObjectColor.getGreenRobot().getHueMin();
    int greenHueMax = ObjectColor.getGreenRobot().getHueMax();
    int greenSatMin = ObjectColor.getGreenRobot().getSatMin();
    int greenSatMax = ObjectColor.getGreenRobot().getSatMax();
    int greenValMin = ObjectColor.getGreenRobot().getValMin();
    int greenValMax = ObjectColor.getGreenRobot().getValMax();

    int blueHueMin = ObjectColor.getBlueRobot().getHueMin();
    int blueHueMax = ObjectColor.getBlueRobot().getHueMax();
    int blueSatMin = ObjectColor.getBlueRobot().getSatMin();
    int blueSatMax = ObjectColor.getBlueRobot().getSatMax();

    int blueValMin = ObjectColor.getBlueRobot().getValMin();
    int blueValMax = ObjectColor.getBlueRobot().getValMax();
    private final double OFFSET_FACTOR = 15.0;

    public RobotDetection() {
    }

    /*
    int valMin = 20;
    int valMax = 255;

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
     */
    public List<Rect>[] detect(Mat image, Legofir dude) {
        List<Rect> greens = new ArrayList<>();
        List<Rect> blues = new ArrayList<>();
        List<Rect> combined = new ArrayList<>();

        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);


        Scalar greenMinValues = new Scalar(greenHueMin, greenSatMin, greenValMin);
        Scalar greenMaxValues = new Scalar(greenHueMax, greenSatMax, greenValMax);

        Scalar blueMinValues = new Scalar(blueHueMin, blueSatMin, blueValMin);
        Scalar blueMaxValues = new Scalar(blueHueMax, blueSatMax, blueValMax);
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

// if any contour exist draw rectangles using the bounding rect

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;

        if(!greenContour.isEmpty() || !blueContour.isEmpty()) {
            for (MatOfPoint contour : greenContour) {
                if(contourArea(contour) > 200 && contourArea(contour) < 1500) {
                    System.out.println("Green: "+contourArea(contour));
                    Rect greenBoundingRect = boundingRect(contour);
                    greens.add(greenBoundingRect);

                    minX = Math.min(greenBoundingRect.x, minX);
                    minY = Math.min(greenBoundingRect.y, minY);
                    maxX = Math.max(greenBoundingRect.x + greenBoundingRect.width, maxX);
                    maxY = Math.max(greenBoundingRect.y + greenBoundingRect.height, maxY);
                }
            }
            for (MatOfPoint contour : blueContour) {
                if(contourArea(contour) > 200 && contourArea(contour) < 1500) {
                    System.out.println("Blue: "+contourArea(contour));
                    Rect blueBoundingRect = boundingRect(contour);
                    blues.add(blueBoundingRect);

                    minX = Math.min(blueBoundingRect.x, minX);
                    minY = Math.min(blueBoundingRect.y, minY);
                    maxX = Math.max(blueBoundingRect.x + blueBoundingRect.width, maxX);
                    maxY = Math.max(blueBoundingRect.y + blueBoundingRect.height, maxY);
                }
            }

            Rect combinedRect = new Rect(minX, minY, maxX - minX, maxY - minY);

            combined.add(combinedRect);


            if(!greens.isEmpty() && !blues.isEmpty()) {
                for (Rect blueBoundingRect : blues) {
                    for(Rect greenBoundingRect : greens) {

                        Point centerOfImage = new Point(image.width()/2, -image.height()/2);

                        Point blueCenter = new Point(blueBoundingRect.x + blueBoundingRect.width * 0.5, -blueBoundingRect.y - blueBoundingRect.height/2);

                        double deltaX = blueCenter.x - centerOfImage.x;
                        // 200 -300
                        // -100
                        double realBlueX = blueCenter.x-(deltaX/OFFSET_FACTOR);
                        //200+100/150 = 202 . When perceived x position is 200 and the center of camera is 300, the real x position is 202

                        double deltaY = blueCenter.y - centerOfImage.y;
                        // -200 - - 300
                        // 100

                        double realBlueY = blueCenter.y-(deltaY/OFFSET_FACTOR);
                        // -200 - 100/50 = -202 - When perceived y position is -200 and the center of camera is -300, the real y position is -202

                        dude.getMap().getRobotPosition().setLeftSideX((int)realBlueX);
                        dude.getMap().getRobotPosition().setLeftSideY((int)realBlueY);
                        Point greenCenter = new Point(greenBoundingRect.x + greenBoundingRect.width * 0.5, -greenBoundingRect.y- greenBoundingRect.height/2);

                        double deltaGreenX = greenCenter.x - centerOfImage.x;
                        // 200 -300
                        // -100
                        double realGreenX = greenCenter.x-(deltaGreenX/OFFSET_FACTOR);
                        //200+100/150 = 202 . When perceived x position is 200 and the center of camera is 300, the real x position is 202

                        double deltaGreenY = greenCenter.y - centerOfImage.y;
                        // -200 - - 300
                        // 100

                        double realGreenY = greenCenter.y-(deltaGreenY/OFFSET_FACTOR);
                        // -200 - 100/50 = -202 - When perceived y position is -200 and the center of camera is -300, the real y position is -202


                        greenCenter.x= realGreenX;
                        greenCenter.y= realGreenY;
                        blueCenter.x= realBlueX;
                        blueCenter.y= realBlueY;

                        dude.getMap().getRobotPosition().setRightSideX((int)realGreenX);
                        dude.getMap().getRobotPosition().setRightSideY((int)realGreenY);

                        Point centerOfLine = new Point((blueCenter.x + greenCenter.x) * 0.5, (blueCenter.y + greenCenter.y) * 0.5);
                        Point leftHarvesterStart = new Point(centerOfLine.x *0.3, centerOfLine.y*0.3);
                        Point rightHarvesterStart = new Point(centerOfLine.x *-0.3, centerOfLine.y*-0.3);

                        Point vectorFromBlueToGreen = new Point(greenCenter.x - blueCenter.x, greenCenter.y - blueCenter.y);
                        int lengthOfVector = (int) Math.sqrt(vectorFromBlueToGreen.x * vectorFromBlueToGreen.x + vectorFromBlueToGreen.y * vectorFromBlueToGreen.y);

                        Point perpendicularVector = new Point(-vectorFromBlueToGreen.y, vectorFromBlueToGreen.x);

                        Point arrowPoint = new Point(centerOfLine.x + perpendicularVector.x, centerOfLine.y + perpendicularVector.y);

                        dude.getMap().setRobotPosition((int)centerOfLine.x,(int)centerOfLine.y,perpendicularVector);

                        // calculate front and back points of robot

                        Point center = new Point(dude.getMap().getRobotPosition().getX(), -dude.getMap().getRobotPosition().getY());
                        double ratioBelow = 1.0;
                        double ratioAbove = 0.6;
                        double distanceBetweenColors = Geometry.distanceBetweenPoints(blueCenter, greenCenter);


                        double angleRad = dude.getMap().getRobotPosition().getHeadingInRadians();

                        double deltaTop = distanceBetweenColors*ratioAbove;
                        double deltaBottom = distanceBetweenColors*ratioBelow;

                        Point front = new Point(center.x - deltaTop * -Math.cos(angleRad), center.y - deltaTop * Math.sin(angleRad));
                        Point back = new Point(center.x + deltaBottom * -Math.cos(angleRad), center.y + deltaBottom * Math.sin(angleRad));

                        dude.getMap().getRobotPosition().setFrontSideX((int)front.x);
                        dude.getMap().getRobotPosition().setFrontSideY((int)-front.y);
                        dude.getMap().getRobotPosition().setBackSideX((int)back.x);
                        dude.getMap().getRobotPosition().setBackSideY((int)-back.y);
                    }


                }

            }


        }


        List<Rect>[] returnvalue = new List[3];
        returnvalue[0] = greens;
        returnvalue[1] = blues;
        returnvalue[2] = combined;
        return returnvalue;
    }
}
