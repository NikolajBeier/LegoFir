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
    int hMin = 0;
    int hMax = 255;
    int sMin = 0;
    int sMax = 255;
    int lMin = 220;
    int lMax = 240;

    public List<Rect> detect(Mat image) {
        List<Rect> edges = new ArrayList<>();
        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsimage, new Scalar(hMin, lMin, sMin), new Scalar(hMax, lMax, sMax), redMask);

        // init
        ArrayList<MatOfPoint> redContour = new ArrayList<>();
        Mat redHierarchy = new Mat();


// find contours
        Imgproc.findContours(redMask, redContour, redHierarchy, RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (!redContour.isEmpty()) {
            for (MatOfPoint contour : redContour) {
                //if(Imgproc.contourArea(contour) > 500)
                Rect boundingRect = Imgproc.boundingRect(contour);
                edges.add(boundingRect);


            }
        }
        return edges;
    }

}
