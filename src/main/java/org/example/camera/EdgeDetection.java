package org.example.camera;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.contourArea;

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

    public List<MatOfPoint> detect(Mat image) {
        List<MatOfPoint> edges = new ArrayList<>();
        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsimage, new Scalar(redHueMin, redSatMin, redValueMin), new Scalar(redHueMax, redSatMax, redValueMax), redMask);


        // init
        ArrayList<MatOfPoint> redContour = new ArrayList<>();
        Mat redHierarchy = new Mat();


// find contours
        Imgproc.findContours(redMask, redContour,redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        if (!redContour.isEmpty()) {
            for (MatOfPoint contour : redContour) {
                 if(contourArea(contour) > 500) {
                     MatOfPoint boundingContour = contour;
                    edges.add(boundingContour);
                }


            }
        }
        return edges;
    }

}
