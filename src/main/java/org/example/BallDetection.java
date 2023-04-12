package org.example;

import org.example.robot.Legofir;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.putText;

public class BallDetection {


    public BallDetection() {

    }


    // LOGIC
    Mat hlsImage = new Mat();
    Mat whiteMask = new Mat();
    int hMin = 0;
    int hMax = 255;
    int sMin = 0;
    int sMax = 255;
    int lMin = 220;
    int lMax = 240;

    public List<Rect> detect(Mat image) {

        List<Rect> balls = new ArrayList<>();

        Imgproc.cvtColor(image, hlsImage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsImage,new Scalar(hMin,lMin,sMin),new Scalar(hMax,lMax,sMax),whiteMask);

        // init
        ArrayList<MatOfPoint> whiteContour = new ArrayList<>();
        Mat whiteHierarchy = new Mat();


// find contours
        Imgproc.findContours(whiteMask, whiteContour, whiteHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if(!whiteContour.isEmpty()){
            for(MatOfPoint contour : whiteContour){
                if(Imgproc.contourArea(contour) > 500){
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    balls.add(boundingRect);
                }
            }
        }
        return balls;
    }

}

