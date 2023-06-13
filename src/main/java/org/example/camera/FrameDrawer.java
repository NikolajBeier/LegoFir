package org.example.camera;

import org.example.mapping.Edge;
import org.example.mapping.Map;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Line2D;

import static org.example.utility.Geometry.*;
import static org.opencv.imgproc.Imgproc.*;

public class FrameDrawer {
    Legofir dude;
    FrameDetector frameDetector;
    private final double FRONTAL_AVOID_DISTANCE = 100;
    private final double FRONTAL_MIDDLE_DISTANCE = 25;
    private final double BACKWARDS_AVOID_DISTANCE = 150;
    private final double TURNING_AVOID_DISTANCE = 50;
    public FrameDrawer(Legofir dude, FrameDetector frameDetector){
        this.dude=dude;
        this.frameDetector=frameDetector;
    }
    public void draw(Mat frame){
        drawOrangeBalls(frame);
        drawWhiteBalls(frame);
        drawEdges(frame);
        drawObstacle(frame);
        drawExitHoles(frame);
        drawRobot(frame);
        drawCollision(frame);
        drawWayPoint(frame);
        drawBallWaypoint(frame);
        drawBallHeading(frame);

    }


    private void drawExitHoles(Mat frame) {
        if(frameDetector.edgeDetectionOn) {
            Point tr = new Point(dude.getMap().getDepositPoint().getRightExitTopRight().x, (-1)*dude.getMap().getDepositPoint().getRightExitTopRight().y);
            Point bl = new Point(dude.getMap().getDepositPoint().getRightExitBottomLeft().x, (-1)*dude.getMap().getDepositPoint().getRightExitBottomLeft().y);
            Imgproc.rectangle(frame,tr,bl,new Scalar(255,0,0));
            putText(frame, "Exit Right", tr, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 0, 0), 2);
            tr = new Point(dude.getMap().getDepositPoint().getLeftExitTopRight().x,(-1)*dude.getMap().getDepositPoint().getLeftExitTopRight().y);
            bl = new Point(dude.getMap().getDepositPoint().getLeftExitBottomLeft().x,(-1)*dude.getMap().getDepositPoint().getLeftExitBottomLeft().y);

            Imgproc.rectangle(frame,tr,bl,new Scalar(0,255,0));
            putText(frame, "Exit Left", tr, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
        }
    }

    private void drawCollision(Mat frame) {
        if(frameDetector.isRobotDetectionOn() && frameDetector.isEdgeDetectionOn()){
            drawCollisionDetection(frame);
        }
    }

    private void drawRobot(Mat frame) {
        if(frameDetector.isRobotDetectionOn()) {

            // Middle points of blue box and green box
            Point blueMiddle = new Point(dude.getMap().getRobotPosition().leftSideX, (-1)*dude.getMap().getRobotPosition().leftSideY);
            Point greenMiddle = new Point(dude.getMap().getRobotPosition().rightSideX, (-1)*dude.getMap().getRobotPosition().rightSideY);

            // Draw the middle points
            circle(frame, blueMiddle, 1, new Scalar(0, 0, 255), 1);
            circle(frame, greenMiddle, 1, new Scalar(0, 0, 255), 1);

            // Center point
            Point centerOfLine = new Point((blueMiddle.x + greenMiddle.x) * 0.5, (blueMiddle.y + greenMiddle.y) * 0.5);

            // Vector from blue to green
            Point vectorFromBlueToGreen = new Point(greenMiddle.x - blueMiddle.x, greenMiddle.y - blueMiddle.y);

            // Perpendicular vector (The heading of the robot)
            Point perpendicularVector = new Point(vectorFromBlueToGreen.y, -vectorFromBlueToGreen.x);

            // Point on the perpendicular vector
            Point arrowPoint = new Point(centerOfLine.x + perpendicularVector.x, centerOfLine.y + perpendicularVector.y);

            // Draw the middle point
            circle(frame, centerOfLine, 2, new Scalar(0, 0, 255), 2);

            // Draw the line between the boxes
            line(frame, blueMiddle, greenMiddle, new Scalar(0, 0, 255), 1);

            // Draw the heading line
            arrowedLine(frame, centerOfLine, arrowPoint, new Scalar(0, 0, 255), 1);


            TennisBall nextBall = dude.getMap().getNextBall();

            int nextBallX = nextBall.getX();
            int nextBallY = nextBall.getY();


            // Draw line to the closest ball
            arrowedLine(frame, centerOfLine, new Point(nextBallX, -nextBallY), new Scalar(0, 255, 0), 1);

            drawFrontAndBackPoints(frame);
        }
    }

    private void drawFrontAndBackPoints(Mat frame) {
        Point frontPoint = new Point(dude.getMap().getRobotPosition().getFrontSideX(), (-1)*dude.getMap().getRobotPosition().getFrontSideY());
        Point backPoint = new Point(dude.getMap().getRobotPosition().getBackSideX(), (-1)*dude.getMap().getRobotPosition().getBackSideY());

        circle(frame, frontPoint, 1, new Scalar(0, 0, 255), 1);
        putText(frame, "Front", frontPoint, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 1);
        circle(frame, backPoint, 1, new Scalar(0, 0, 255), 1);
        putText(frame, "Back", backPoint, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 1);
    }

    private void drawObstacle(Mat frame) {
        // draw obstacle
        if(frameDetector.edgeDetectionOn){
            try{
            line(frame, new Point(dude.getMap().getObstacle().getTopPoint().x,-dude.getMap().getObstacle().getTopPoint().y), new Point(dude.getMap().getObstacle().getBottomPoint().x,-dude.getMap().getObstacle().getBottomPoint().y), new Scalar(0, 255, 0), 2, 8, 0);
            line(frame, new Point(dude.getMap().getObstacle().getLeftPoint().x,-dude.getMap().getObstacle().getLeftPoint().y), new Point(dude.getMap().getObstacle().getRightPoint().x,-dude.getMap().getObstacle().getRightPoint().y), new Scalar(0, 255, 0), 2, 8, 0);
            } catch (NullPointerException e){
                System.out.println("Obstacle not found");
            }
        }

    }

    private void drawEdges(Mat frame) {
        if(frameDetector.edgeDetectionOn) {
            try {
                Point bottomLeft = new Point(dude.getMap().getEdge().getBottomLeft().x, -dude.getMap().getEdge().getBottomLeft().y);
                Point bottomRight = new Point(dude.getMap().getEdge().getBottomRight().x, -dude.getMap().getEdge().getBottomRight().y);
                Point topLeft = new Point(dude.getMap().getEdge().getTopLeft().x, -dude.getMap().getEdge().getTopLeft().y);
                Point topRight = new Point(dude.getMap().getEdge().getTopRight().x, -dude.getMap().getEdge().getTopRight().y);
                Imgproc.circle(frame, bottomLeft, 30, new Scalar(225, 255, 255), 2);
                Imgproc.circle(frame, bottomRight, 30, new Scalar(225, 255, 255), 2);
                Imgproc.circle(frame, topLeft, 30, new Scalar(225, 255, 255), 2);
                Imgproc.circle(frame, topRight, 30, new Scalar(225, 255, 255), 2);
                putText(frame, "Bottom left", bottomLeft, FONT_HERSHEY_SIMPLEX, 1, new Scalar(225, 255, 255), 2);
                putText(frame, "Bottom right", bottomRight, FONT_HERSHEY_SIMPLEX, 1, new Scalar(225, 255, 255), 2);
                putText(frame, "Top left", topLeft, FONT_HERSHEY_SIMPLEX, 1, new Scalar(225, 255, 255), 2);
                putText(frame, "Top right", topRight, FONT_HERSHEY_SIMPLEX, 1, new Scalar(225, 255, 255), 2);
            } catch (NullPointerException e) {
                System.out.println("No edge found");
            }
        }
    }

    private void drawWhiteBalls(Mat frame) {
        for (TennisBall whiteBall : dude.getMap().getBalls()) {
            Imgproc.circle(frame, new Point(whiteBall.getX(), -whiteBall.getY()), 12, new Scalar(0, 0, 255), 1);
            putText(frame, "White ball", new Point(whiteBall.getX(), -whiteBall.getY()), FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
        }
    }

    private void drawOrangeBalls(Mat frame) {
        for (TennisBall orangeBall : dude.getMap().getOrangeBalls()) {
            Imgproc.circle(frame, new Point(orangeBall.getX(), -orangeBall.getY()), 12, new Scalar(0, 0, 255), 1);
            putText(frame, "Orange ball", new Point(orangeBall.getX(), -orangeBall.getY()), FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
        }
    }



    private void drawCollisionDetection(Mat image) {
        Point heading = dude.getMap().getRobotPosition().getHeading();
        Point rightHeading = new Point(heading.y, -heading.x);
        Point leftHeading = new Point(-heading.y, heading.x);
        Point backHeading = new Point(-heading.x, -heading.y);
        Point leftSide = new Point(dude.getMap().getRobotPosition().leftSideX, dude.getMap().getRobotPosition().leftSideY);
        Point rightSide = new Point(dude.getMap().getRobotPosition().rightSideX, dude.getMap().getRobotPosition().rightSideY);
        Point frontSide = new Point(dude.getMap().getRobotPosition().getFrontSideX(), dude.getMap().getRobotPosition().getFrontSideY());
        Point backSide = new Point(dude.getMap().getRobotPosition().getBackSideX(), dude.getMap().getRobotPosition().getBackSideY());
        Point middle = new Point(dude.getMap().getRobotPosition().getX(), dude.getMap().getRobotPosition().getY());

        switch(dude.getState()){
            case MOVING_BACKWARD: {
                drawLinesToEdge(image,backHeading,leftSide);
                drawLinesToEdge(image,backHeading,rightSide);
                drawLinesToEdge(image,backHeading,middle);
                drawCollisionThreshold(image,backHeading,leftSide,BACKWARDS_AVOID_DISTANCE);
                drawCollisionThreshold(image,backHeading,rightSide,BACKWARDS_AVOID_DISTANCE);
                drawCollisionThreshold(image,backHeading,middle,BACKWARDS_AVOID_DISTANCE);
                break;
            }
            case MOVING_FORWARD: {
                drawLinesToEdge(image,heading,leftSide);
                drawLinesToEdge(image,heading,rightSide);
                drawLinesToEdge(image,heading,middle);
                drawCollisionThreshold(image,heading,leftSide,FRONTAL_AVOID_DISTANCE);
                drawCollisionThreshold(image,heading,rightSide,FRONTAL_AVOID_DISTANCE);
                drawCollisionThreshold(image,heading,middle,FRONTAL_AVOID_DISTANCE);
                drawCollisionThreshold(image,heading,frontSide,FRONTAL_MIDDLE_DISTANCE);
                break;
            }
            case TURNING_LEFT: {
                drawLinesToEdge(image,leftHeading,frontSide);
                drawLinesToEdge(image,rightHeading,backSide);
                drawCollisionThreshold(image,leftHeading,frontSide,TURNING_AVOID_DISTANCE);
                drawCollisionThreshold(image,rightHeading,backSide,TURNING_AVOID_DISTANCE);
                break;
            }
            case TURNING_RIGHT: {
                drawLinesToEdge(image,rightHeading,frontSide);
                drawLinesToEdge(image,leftHeading,backSide);
                drawCollisionThreshold(image,rightHeading,frontSide,TURNING_AVOID_DISTANCE);
                drawCollisionThreshold(image,leftHeading,backSide,TURNING_AVOID_DISTANCE);
                break;
            }
        }
    }

    private void drawLinesToEdge(Mat image, Point heading, Point startingPoint) {
        Map currentMap = dude.getMap();
        Edge edge = currentMap.getEdge();


        // Returns the distance of the robot to the edge of the map.



        // Lines of the robot
        Line2D line = new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x+10000*heading.x, startingPoint.y+10000*heading.y);
        line(image,new Point(startingPoint.x, -startingPoint.y),new Point(startingPoint.x+10000*heading.x, -startingPoint.y-10000*heading.y),new Scalar(0,255,0),1);

        // Edge points of the map

        Point topLeft = edge.getTopLeft();
        Point topRight = edge.getTopRight();
        Point bottomLeft = edge.getBottomLeft();
        Point bottomRight = edge.getBottomRight();

        // Lines of the map
        Line2D.Double[] edges = {new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y),
                new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y),
                new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y),
                new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y),
                new Line2D.Double(dude.getMap().getObstacle().getRightPoint().x, dude.getMap().getObstacle().getRightPoint().y, dude.getMap().getObstacle().getLeftPoint().x, dude.getMap().getObstacle().getLeftPoint().y),
                new Line2D.Double(dude.getMap().getObstacle().getTopPoint().x, dude.getMap().getObstacle().getTopPoint().y, dude.getMap().getObstacle().getBottomPoint().x, dude.getMap().getObstacle().getBottomPoint().y)
        };

        double distanceFromStartingPointToEdge;

        double shortestDistance = Double.MAX_VALUE;

        // Looks through all 4 edges, calculates the distance from the two robot sides to the edge,
        // and if the distance found is shorter than the current shortest distance, it is set as the new shortest distance.
        for(Line2D edge1 : edges){
            if(line.intersectsLine(edge1)){
                distanceFromStartingPointToEdge=distanceBetweenPoints(startingPoint,intersection(line, edge1));
                if(distanceFromStartingPointToEdge<shortestDistance){
                    shortestDistance = distanceFromStartingPointToEdge;
                    circle(image,new Point(intersection(line, edge1).x,-intersection(line, edge1).y),25,new Scalar(255,0,0),1);
                    putText(image,Integer.toString((int)shortestDistance),new Point(intersection(line, edge1).x,-intersection(line, edge1).y),FONT_HERSHEY_PLAIN,2,new Scalar(255,0,0));
                }
            }
        }
    }
    private void drawCollisionThreshold(Mat image, Point heading, Point startingPoint, double threshold) {
        double rad = degreesOfVectorInRadians(heading.x,heading.y);

        Point collisionPoint = new Point(startingPoint.x - threshold * -Math.cos(rad), startingPoint.y + threshold * Math.sin(rad));
        circle(image, new Point(collisionPoint.x, -collisionPoint.y), 10, new Scalar(0, 255, 0), 2);
        putText(image, "Collision point", new Point(collisionPoint.x, -collisionPoint.y), FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
    }
    private void drawWayPoint(Mat image){
        if (dude.getMap().getWayPoint() !=null && frameDetector.ballDetectionOn && frameDetector.edgeDetectionOn){
            try {
                Point wayPoint = dude.getMap().getWayPoint();
                circle(image,new Point(wayPoint.x,-wayPoint.y),25,new Scalar(255,0,0),1);


                Point wayPointOffSet = new Point(dude.getMap().getWayPoint().x + 10, dude.getMap().getWayPoint().y);
                circle(image,new Point(wayPointOffSet.x,-wayPointOffSet.y),2,new Scalar(255,255,0),1);

                Point wayPointOffSet1 = new Point(dude.getMap().getWayPoint().x - 10, dude.getMap().getWayPoint().y);
                circle(image,new Point(wayPointOffSet1.x,-wayPointOffSet1.y),2,new Scalar(255,255,0),1);

                Point wayPointOffSet2 = new Point(dude.getMap().getWayPoint().x, dude.getMap().getWayPoint().y+10);
                circle(image,new Point(wayPointOffSet2.x,-wayPointOffSet2.y),2,new Scalar(255,255,0),1);

                Point wayPointOffSet3 = new Point(dude.getMap().getWayPoint().x, dude.getMap().getWayPoint().y-10);
                circle(image,new Point(wayPointOffSet3.x,-wayPointOffSet3.y),2,new Scalar(255,255,0),1);


            }catch (NullPointerException e){
                System.out.println("NullPointerException");
            }}


    }
    private void drawBallWaypoint(Mat image){
        try {
        if (dude.getMap().getBallNextToWallWaypoint() != null && frameDetector.ballDetectionOn && frameDetector.edgeDetectionOn) {
            Point waypoint = dude.getMap().getBallNextToWallWaypoint();
            circle(image, new Point(waypoint.x, -waypoint.y), 25, new Scalar(255, 0, 0), 1);
        }
        }catch (NullPointerException e){
            System.out.println("NullPointerException");
        }


    }

}
    private void drawBallHeading(Mat image){
        try {
            if (dude.getMap().getNextBall() != null && frameDetector.ballDetectionOn && frameDetector.edgeDetectionOn) {
                Point ballPos = new Point(dude.getMap().getNextBall().getX(), dude.getMap().getNextBall().getY());
                Point eastHeading = new Point(1, 0);
                Point southHeading = new Point(0, -1);
                Point westHeading = new Point(-1, 0);
                Point northHeading = new Point(0, 1);


                drawLinesToEdge(image, eastHeading, ballPos);
                drawLinesToEdge(image, southHeading, ballPos);
                drawLinesToEdge(image, westHeading, ballPos);
                drawLinesToEdge(image, northHeading, ballPos);
            }
        } catch (NullPointerException e){
            // doesn't draw
        }
    }
}