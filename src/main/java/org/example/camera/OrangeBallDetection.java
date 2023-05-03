package org.example.camera;

import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.contourArea;

public class OrangeBallDetection {

    Mat hlsimage = new Mat();
    Mat redMask = new Mat();
    int redHueMin = 0;
    int redHueMax = 255 ;
    int redSatMin = 0;
    int redSatMax = 255;
    int redValueMin = 0;
    int redValueMax=255;

    public List<Rect> detect(Mat image, Legofir dude) {

        List<Rect> orangeBalls = new ArrayList<>();

        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HSV);

        Core.inRange(image,new Scalar(redHueMin,redSatMin,redValueMin),new Scalar(redHueMax,redSatMax,redValueMax),redMask);


        ArrayList<MatOfPoint> redContour = new ArrayList<>();


        Mat redHierarchy = new Mat();

        Imgproc.findContours(redMask, redContour, redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //if (dude != null)
        //dude.getMap().removeAllBalls();

        if(!redContour.isEmpty()){
            for(MatOfPoint contour : redContour){
                    if(Imgproc.contourArea(contour) > 0&& Imgproc.contourArea(contour)<1000){
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


