package org.example.camera;


import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.contourArea;

public class OrangeBallDetection {

    Mat hsvImage = new Mat();
    Mat redMask = new Mat();
    int hMin = ObjectColor.getOrangeBall().getHueMin();
    int hMax = ObjectColor.getOrangeBall().getHueMax();
    int sMin = ObjectColor.getOrangeBall().getSatMin();
    int sMax = ObjectColor.getOrangeBall().getSatMax();
    int lMin = ObjectColor.getOrangeBall().getValMin();
    int lMax = ObjectColor.getOrangeBall().getValMax();

    public List<Rect> detect(Mat image, Legofir dude) {

        List<Rect> orangeBalls = new ArrayList<>();

        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Core.inRange(image,new Scalar(hMin,sMin,lMin),new Scalar(hMax,sMax,lMax),redMask);


        ArrayList<MatOfPoint> redContour = new ArrayList<>();


        Mat redHierarchy = new Mat();

        Imgproc.findContours(redMask, redContour, redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        if(!redContour.isEmpty()){
            for(MatOfPoint contour : redContour){
                    if(Imgproc.contourArea(contour) > 200&& Imgproc.contourArea(contour)<400){
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    orangeBalls.add(boundingRect);
                    //dude.getMap().addBallCord((int)(boundingRect.x+boundingRect.width*0.5), (int)(boundingRect.y+ boundingRect.height*0.5));
                }
            }
        }
        if(dude!=null) {
            dude.addBalls(orangeBalls);
        }
        return orangeBalls;
    }

    }


