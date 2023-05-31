package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class EdgeDetection {


    public EdgeDetection() {


    }

    Mat hlsimage = new Mat();
    Mat redMask = new Mat();
    int hMin = ObjectColor.getEdge().getHueMin();
    int hMax = ObjectColor.getEdge().getHueMax();
    int sMin = ObjectColor.getEdge().getSatMin();
    int sMax = ObjectColor.getEdge().getSatMax();
    int lMin = ObjectColor.getEdge().getValMin();
    int lMax = ObjectColor.getEdge().getValMax();

    public Rect detect(Mat image, Legofir dude) {
        Rect edgeRect;
        Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(hlsimage, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), redMask);


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



    public Point[] intersectionDetect(Mat image) {
        //Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        //Imgcodecs.imwrite("beforebefore.jpg", hlsimage);
        Core.inRange(image, new Scalar(0, 0, 150), new Scalar(160, 105, 255), redMask);

        Imgcodecs.imwrite("before.jpg", redMask);


        // Canny Edge Detection
        //Mat edges = new Mat();
        //Imgproc.Canny(redMask, edges, 50, 100, 7, false);
        //Imgcodecs.imwrite("canny.jpg", edges);

        // Apply Hough Line Transform
        Mat lines = new Mat();
        Imgproc.HoughLinesP(redMask, lines, 1, Math.PI / 180, 250, 10, 5);

        // Draw the lines
        Mat houghLines = new Mat();
        houghLines.create(redMask.rows(), redMask.cols(), CvType.CV_8UC1);

        Imgcodecs.imwrite("weirdone.jpg", houghLines);

        // This will ensure only the top 4 lines are processed
        int limit = 20;
        for (int i = 0; i < lines.rows() && i < limit; i++) {
            double[] points = lines.get(i, 0);
            double x1, y1, x2, y2;
            x1 = points[0];
            y1 = points[1];
            x2 = points[2];
            y2 = points[3];

            Point pt1 = new Point(x1, y1);
            Point pt2 = new Point(x2, y2);

            // Draw line on the image
            Imgproc.line(image, pt1, pt2, new Scalar(255, 0, 0), 5);
        }

        // Save the output
        Imgcodecs.imwrite("houghlines.jpg", houghLines);

        return null;
    }
}
