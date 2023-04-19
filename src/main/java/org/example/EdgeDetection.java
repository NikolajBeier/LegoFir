package org.example;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;

public class EdgeDetection {


    public EdgeDetection() {


    }

    Mat hlsimage = new Mat();
    Mat redMask = new Mat();
    int redHueMin = 0;
    int redHueMax = 50 ;
    int redSatMin = 0;
    int redSatMax = 100;

    public List<Rect> detect(Mat image) {
        List<Rect> edges = new ArrayList<>();
        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsimage, new Scalar(redHueMin, redSatMin, 150), new Scalar(redHueMax, redSatMax, 255), redMask);



        // init
        ArrayList<MatOfPoint> redContour = new ArrayList<>();
        Mat redHierarchy = new Mat();


// find contours
        Imgproc.findContours(redMask, redContour, redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (!redContour.isEmpty()) {
            for (MatOfPoint contour : redContour) {
                if(Imgproc.contourArea(contour) > 200) {
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    edges.add(boundingRect);
                }


            }
        }
        return edges;
    }

}
