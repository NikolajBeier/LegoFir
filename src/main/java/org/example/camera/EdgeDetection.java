package org.example.camera;

import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class EdgeDetection {


    public EdgeDetection() {


    }

    Mat hlsimage = new Mat();
    Mat redMask = new Mat();
    int redHueMin = 5;
    int redHueMax = 15 ;
    int redSatMin = 50;
    int redSatMax = 255;
    int redValueMin = 50;
    int redValueMax=255;

    public Rect detect(Mat image, Legofir dude) {
        Rect edgeRect;
        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsimage, new Scalar(redHueMin, redSatMin, redValueMin), new Scalar(redHueMax, redSatMax, redValueMax), redMask);


        // init
        List<MatOfPoint> redContour = new ArrayList<>();
        Mat redHierarchy = new Mat();


// find contours
        Imgproc.findContours(redMask, redContour,redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        if (!redContour.isEmpty()) {
            for (MatOfPoint contour : redContour) {
                 if(contourArea(contour) > 500) {
                     edgeRect = boundingRect(contour);
                     Point topLeft = new Point(edgeRect.tl().x, -edgeRect.tl().y);
                     Point bottomRight = new Point(edgeRect.br().x, -edgeRect.br().y);
                     Point topRight = new Point(bottomRight.x, -topLeft.y);
                     Point bottomLeft = new Point(topLeft.x, -bottomRight.y);
                     int height = edgeRect.height;
                     int width = edgeRect.width;
                     dude.getMap().setEdge(topLeft, topRight, bottomLeft, bottomRight, height, width);
                     return edgeRect;
                }


            }
        }
        return null;
    }

}
