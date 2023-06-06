package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static org.opencv.imgproc.Imgproc.*;

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
    Point pt1;
    Point pt2;
    List<Point> points = new ArrayList<>();
    Point topPoint = new Point(0,10000);
    Point bottomPoint = new Point(0,0);
    Point leftPoint = new Point(10000,0);
    Point rightPoint = new Point(0,0);

    Point robotTopPoint= new Point(0,0);
    Point robotBottomPoint= new Point(0,0);
    Point robotLeftPoint= new Point(0,0);
    Point robotRightPoint= new Point(0,0);
    Mat lines = new Mat();

    public ObstacleDetection() {}



    public void detect(Mat image, Point topObstacle, Point bottomObstacle, Point leftObstacle, Point rightObstacle, Legofir dude) {

        Core.inRange(image, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), redMask);

        // Clear edges
        croppedMask=redMask.colRange(new Range(offsetX,image.width()-offsetX)).rowRange(new Range(offsetY,image.height()-offsetY));




        // Apply Hough Line Transform
        Imgproc.HoughLinesP(croppedMask, lines, 1, Math.PI / 180, 80 , 30, 10);

        points.clear();
        for( int i = 0; i < lines.rows(); i++ )
        {
            double[] data = lines.get(i, 0);
            pt1 = new Point(data[0]+offsetX, data[1]+offsetY);
            pt2 = new Point(data[2]+offsetX, data[3]+offsetY);
            points.add(pt1);
            points.add(pt2);
        }

        topPoint.set(new double[]{0,10000});
        bottomPoint.set(new double[]{0,0});
        leftPoint.set(new double[]{10000,0});
        rightPoint.set(new double[]{0,0});

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

        robotTopPoint.x = topPoint.x;
        robotTopPoint.y = -topPoint.y;
        robotBottomPoint.x = bottomPoint.x;
        robotBottomPoint.y = -bottomPoint.y;
        robotLeftPoint.x = leftPoint.x;
        robotLeftPoint.y = -leftPoint.y;
        robotRightPoint.x = rightPoint.x;
        robotRightPoint.y = -rightPoint.y;

        dude.getMap().setObstacle(robotTopPoint, robotBottomPoint, robotLeftPoint, robotRightPoint);

    }



}
