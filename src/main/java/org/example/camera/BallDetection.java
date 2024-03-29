package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.contourArea;

public class BallDetection {


    public BallDetection() {

    }


    // LOGIC
    Mat hlsImage = new Mat();
    Mat whiteMask = new Mat();

    int hMin = ObjectColor.getWhiteBall().getHueMin();
    int hMax = ObjectColor.getWhiteBall().getHueMax();
    int sMin = ObjectColor.getWhiteBall().getSatMin();
    int sMax = ObjectColor.getWhiteBall().getSatMax();
    int lMin = ObjectColor.getWhiteBall().getValMin();
    int lMax = ObjectColor.getWhiteBall().getValMax();

    /*int hMin = 0;
    int hMax = 255;
    int sMin = 0;
    int sMax = 255;
    int lMin = 210;
    int lMax = 255;*/


    public List<Rect> detect(Mat image, Legofir dude) {

        List<Rect> balls = new ArrayList<>();

        Imgproc.cvtColor(image, hlsImage, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hlsImage, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), whiteMask);

        // init
        ArrayList<MatOfPoint> whiteContour = new ArrayList<>();
        Mat whiteHierarchy = new Mat();


// find contours
        Imgproc.findContours(whiteMask, whiteContour, whiteHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //if (dude != null)
        //dude.getMap().removeAllBalls();

        if (!whiteContour.isEmpty()) {
            for (MatOfPoint contour : whiteContour) {
                if ( contourArea(contour) > 80) {
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    balls.add(boundingRect);
                    //dude.getMap().addBallCord((int)(boundingRect.x+boundingRect.width*0.5), (int)(boundingRect.y+ boundingRect.height*0.5));
                }
            }
        }
        if (true) {
            dude.addBalls(balls);
        }
        return balls;
    }

}

