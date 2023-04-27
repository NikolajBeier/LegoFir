package org.example.mapping;

import lejos.robotics.geometry.Line;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Map {

    int x;
    int y;
    int size;
    Edge edge = new Edge();

    public RobotPosition getRobotPosition() {
        return robotPosition;
    }

    public List<TennisBall> getBalls() {
        return balls;
    }

    RobotPosition robotPosition = new RobotPosition();
    List<TennisBall> balls = new ArrayList<>();

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = x*y;
    }



    public void addBallCord(int x, int y){
        TennisBall tennisball = new TennisBall(x,y);
        balls.add(tennisball);
    }

    public void setRobotPosition(int x, int y, Point heading){
        robotPosition.x = x;
        robotPosition.y = y;
        robotPosition.heading = heading;
    }

    public void removeAllBalls() {
        balls.clear();
    }

    public TennisBall getNextBall() {



        // find the tennis ball closest to the robot
        TennisBall closestBall = new TennisBall(0,0);
        double closestDistance = Integer.MAX_VALUE;

        for(TennisBall tennisball : balls) {
            // Distance between two points:
            double distance = Math.sqrt((tennisball.x-getRobotPosition().x)*(tennisball.x-getRobotPosition().x)+(tennisball.y-getRobotPosition().y)*(tennisball.y-getRobotPosition().y));
            if(distance<closestDistance) {
                closestDistance = distance;
                closestBall = tennisball;
            }
        }
        /*
        System.out.println("closest distance: "+closestDistance);
        System.out.println("robot x: "+getRobotPosition().x+" robot y: "+getRobotPosition().y);
        System.out.println("ball x: "+closestBall.x+" ball y: "+closestBall.y);

         */


        return closestBall;
    }

    public void setEdge(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, int height, int width) {
        edge.setAll(topLeft, topRight, bottomLeft, bottomRight, height, width);
    }

    public void setBalls(List<TennisBall> newList) {
        balls = newList;
    }

    public Edge getEdge() {
        return edge;
    }

    public double frontDistanceToEdge() {
        // Returns the distance of the robot to the edge of the map.

        // Heading vector of robot
        Point heading = robotPosition.getHeading();

        // Starting point of rightSide vector
        Point rightSide = new Point(robotPosition.rightSideX, robotPosition.rightSideY);

        // Starting point of leftSide vector
        Point leftSide = new Point(robotPosition.leftSideX, robotPosition.leftSideY);

        // Lines of the robot
        Line2D leftRobotLine = new Line2D.Double(leftSide.x, leftSide.y, leftSide.x+heading.x, leftSide.y+heading.y);
        Line2D rightRobotLine = new Line2D.Double(rightSide.x, rightSide.y, rightSide.x+heading.x, rightSide.y+heading.y);

        // Edge points of the map

        Point topLeft = edge.getTopLeft();
        Point topRight = edge.getTopRight();
        Point bottomLeft = edge.getBottomLeft();
        Point bottomRight = edge.getBottomRight();

        // Lines of the map
        Line2D topLine = new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y);
        Line2D bottomLine = new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y);
        Line2D leftLine = new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
        Line2D rightLine = new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y);


        // Intersects
        Point leftRobotTopLineIntersect = new Point(),
                leftRobotRightLineIntersect = new Point(),
                leftRobotLeftLineIntersect = new Point(),
                leftRobotBottomLineIntersect = new Point();
        Point rightRobotTopLineIntersect = new Point(),
                rightRobotRightLineIntersect = new Point(),
                rightRobotLeftLineIntersect = new Point(),
                rightRobotBottomLineIntersect = new Point();
        if(topLine.intersectsLine(leftRobotLine)){
            // Not worried about null case since we used the built in function to check if they even intersect
            leftRobotTopLineIntersect=intersection(topLine, leftRobotLine);
            leftRobotBottomLineIntersect=intersection(bottomLine, leftRobotLine);
            rightRobotTopLineIntersect=intersection(topLine, rightRobotLine);
            rightRobotBottomLineIntersect=intersection(bottomLine, rightRobotLine);
        }
        if(rightLine.intersectsLine(leftRobotLine)){
            leftRobotRightLineIntersect=intersection(rightLine, leftRobotLine);
            leftRobotLeftLineIntersect=intersection(leftLine, leftRobotLine);
            rightRobotRightLineIntersect=intersection(rightLine, rightRobotLine);
            rightRobotLeftLineIntersect=intersection(leftLine, rightRobotLine);
        }

        // Shortest distance between either robot right side or left side and intersect points
        double shortestDistance = Integer.MAX_VALUE;

        for(Point intersect : new Point[]{
                leftRobotTopLineIntersect,
                leftRobotBottomLineIntersect,
                rightRobotTopLineIntersect,
                rightRobotBottomLineIntersect,
                leftRobotRightLineIntersect,
                leftRobotLeftLineIntersect,
                rightRobotRightLineIntersect,
                rightRobotLeftLineIntersect}){
            double distanceRightSide = Math.sqrt((intersect.x-robotPosition.rightSideX)*(intersect.x-robotPosition.rightSideX)+(intersect.y-robotPosition.rightSideY)*(intersect.y-robotPosition.rightSideY));
            double distanceLeftSide = Math.sqrt((intersect.x-robotPosition.rightSideX)*(intersect.x-robotPosition.rightSideX)+(intersect.y-robotPosition.rightSideY)*(intersect.y-robotPosition.rightSideY));

            if(distanceRightSide<shortestDistance){
                shortestDistance = distanceRightSide;
            }
            if(distanceLeftSide<shortestDistance){
                shortestDistance = distanceLeftSide;
            }

        }
        return shortestDistance;

    }
    Point intersection(Line2D a, Line2D b){
        // Two points on each line
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();

        // Denominator is the difference in slope
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // If they are the same return null.
        if (d == 0) {
            return null;
        }

        // Intersection point
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point(xi, yi);
    }
}
