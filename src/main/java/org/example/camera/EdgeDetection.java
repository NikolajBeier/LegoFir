package org.example.camera;

import org.example.mapping.ObjectColor;
import org.example.robot.model.Legofir;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static org.example.utility.Geometry.distanceBetweenPoints;
import static org.example.utility.Geometry.intersection;
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
        //Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        hlsimage=image;
        Core.inRange(hlsimage, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), redMask);

        Imgcodecs.imwrite("previous.jpg", redMask);

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
                     Point topRight = new Point(bottomRight.x, topLeft.y);
                     Point bottomLeft = new Point(topLeft.x, bottomRight.y);
                     int height = edgeRect.height;
                     int width = edgeRect.width;
                     dude.getMap().setEdge(topLeft, topRight, bottomLeft, bottomRight, height, width);
                     dude.getMap().calcDepositPoints();
                     return edgeRect;
                }


            }
        }
        return null;
    }



    public void intersectionDetect(Mat image, Legofir dude) {
        //Imgproc.cvtColor(image, hlsimage, Imgproc.COLOR_BGR2HLS);
        Core.inRange(image, new Scalar(hMin, sMin, lMin), new Scalar(hMax, sMax, lMax), redMask);

        // Apply Hough Line Transform
        Mat lines = new Mat();
        Imgproc.HoughLines(redMask, lines, 1, Math.PI / 180, 10 , 0, 0);

        // This will ensure only the top 4 lines are processed
        List<Double> rhoList = new ArrayList<Double>();

        double difference = 50;
        List<Line2D> edges = new ArrayList<Line2D>();
        List<Point> intersection = new ArrayList<Point>();


        for (int i = 0; i < lines.rows() && i < 10; i++) {
            if(edges.size()==4){
                break;
            }

            double[] data = lines.get(i, 0);
            double rho = data[0];
            double theta = data[1];

            if(rhoList.isEmpty()) {
                addLine(rho, theta, rhoList,edges, image);
            } else {
                boolean isFalse = false;
                for(int j = 0; j < rhoList.size(); j++) {
                    if((rho < rhoList.get(j)+difference && rho > rhoList.get(j)-difference)){
                        isFalse = true;
                        break;
                    }
                }
                if(!isFalse){
                    addLine(rho, theta, rhoList,edges, image);
                }
            }
        }


        Point[] returnValues = new Point[4];
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                if(edges.get(i).intersectsLine(edges.get(j)) && edges.get(i) != edges.get(j)){
                    intersection.add(intersection(edges.get(i), edges.get(j)));



                    if((int)intersection.get(intersection.size()-1).x < image.width()/2){
                        if((int)intersection.get(intersection.size()-1).y < -image.height()/2){
                            //bottom left
                            returnValues[0] = intersection.get(intersection.size()-1);

                        } else if((int)intersection.get(intersection.size()-1).y > -image.height()/2){
                            //top left
                            returnValues[1] = intersection.get(intersection.size()-1);
                        }

                    } else if((int)intersection.get(intersection.size()-1).x > image.width()/2){
                        if((int)intersection.get(intersection.size()-1).y < -image.height()/2){
                            //bottom right
                            returnValues[2] = intersection.get(intersection.size()-1);

                        } else if((int)intersection.get(intersection.size()-1).y > -image.height()/2){
                            //top right
                            returnValues[3] = intersection.get(intersection.size()-1);
                        }
                    }
                }
            }
         }

        try {
            dude.getMap().setEdge(returnValues[1], returnValues[3], returnValues[0], returnValues[2], (int) distanceBetweenPoints(returnValues[1], returnValues[0]), (int) distanceBetweenPoints(returnValues[1], returnValues[3]));
        } catch (NullPointerException e) {
            System.out.println("No intersection found");
        }
        dude.getMap().calcDepositPoints();
    }

    private void addLine(double rho, double theta, List<Double> rhoList, List<Line2D> edges, Mat image){
        double a = Math.cos(theta);
        double b = Math.sin(theta);
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
