package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static org.opencv.imgproc.Imgproc.line;

public class ObstacleDetection {
    Mat hlsimage = new Mat();
    Mat redMask = new Mat();
    Mat croppedMask = new Mat();
    int hMin = ObjectColor.getEdge().getHueMin();
    int hMax = ObjectColor.getEdge().getHueMax();
    int sMin = ObjectColor.getEdge().getSatMin();
    int sMax = ObjectColor.getEdge().getSatMax();
    int lMin = ObjectColor.getEdge().getValMin();
    int lMax = ObjectColor.getEdge().getValMax();
    int offsetY= 120;
    int offsetX= 400;
    public ObstacleDetection() {}

    public Mat detect(Mat image, Point topObstacle, Point bottomObstacle, Point leftObstacle, Point rightObstacle, Legofir dude) {

        Core.inRange(image, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), redMask);

        // Clear edges
        croppedMask=redMask.colRange(new Range(offsetX,image.width()-offsetX)).rowRange(new Range(offsetY,image.height()-offsetY));

        // Apply Hough Line Transform
        Mat lines = new Mat();
        Imgproc.HoughLinesP(croppedMask, lines, 1, Math.PI / 180, 80 , 30, 10);

        List<Point> points = new ArrayList<>();
        for( int i = 0; i < lines.rows(); i++ )
        {
            double[] data = lines.get(i, 0);
            Point pt1 = new Point(data[0]+offsetX, data[1]+offsetY);
            Point pt2 = new Point(data[2]+offsetX, data[3]+offsetY);
            points.add(pt1);
            points.add(pt2);
        }
        Point topPoint = new Point(0,10000);
        Point bottomPoint = new Point(0,0);
        Point leftPoint = new Point(10000,0);
        Point rightPoint = new Point(0,0);
        for(Point point : points){
            if(point.y < topPoint.y){
                topPoint = point;
            }
            if(point.y > bottomPoint.y){
                bottomPoint = point;
            }
            if(point.x < leftPoint.x){
                leftPoint = point;
            }
            if(point.x > rightPoint.x){
                rightPoint = point;
            }
        }

        Point robotTopPoint= new Point(topPoint.x, -topPoint.y);
        Point robotBottomPoint= new Point(bottomPoint.x, -bottomPoint.y);
        Point robotLeftPoint= new Point(leftPoint.x, -leftPoint.y);
        Point robotRightPoint= new Point(rightPoint.x, -rightPoint.y);

        dude.getMap().setObstacle(robotTopPoint, robotBottomPoint, robotLeftPoint, robotRightPoint);


        return image;
    }
    private void addLine(double rho, double theta, List<Double> rhoList, List<Line2D> edges, Mat image){
        double a = cos(theta);
        double b = sin(theta);
        double x0 = a*rho;
        double y0 = b*rho;
        //Drawing lines on the image
        Point pt1 = new Point();
        Point pt2 = new Point();
        pt1.x = Math.round(x0 + 1000*(-b));
        pt1.y = Math.round(y0 + 1000*(a));
        pt2.x = Math.round(x0 - 10000*(-b));
        pt2.y = Math.round(y0 - 10000 *(a));
        rhoList.add(rho);

        edges.add(new Line2D.Double(pt1.x, -(pt1.y), pt2.x, -(pt2.y)));
    }


}
