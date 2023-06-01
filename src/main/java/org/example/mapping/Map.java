package org.example.mapping;

import org.example.utility.Geometry;
import org.opencv.core.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static org.example.utility.Geometry.*;

public class Map {

    int x;
    int y;
    int size;
    Edge edge = new Edge();
    RobotPosition robotPosition = new RobotPosition();
    List<TennisBall> balls = new ArrayList<>();
    List<TennisBall> orangeBalls = new ArrayList<>();

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

        if (!orangeBalls.isEmpty()) {
            for (TennisBall orangeTennisBall :orangeBalls){
                closestBall = orangeTennisBall ;
            }
        }else
            for(TennisBall tennisball : balls) {
            // Distance between two points:
            double distance = Math.sqrt((tennisball.x-getRobotPosition().x)*(tennisball.x-getRobotPosition().x)+(tennisball.y-getRobotPosition().y)*(tennisball.y-getRobotPosition().y));
            double robotAngle = getRobotPosition().getHeadingInRadians();
            double ballAngle = Geometry.degreesOfVectorInRadians(tennisball.x,tennisball.y);
            if(distance<closestDistance && distance<100 && ballAngle<robotAngle+0.25 || ballAngle>robotAngle-0.25 ) {
                //TODO skal lave noget kode der sørger for at robotten ikke kører ind i bolden
            } else if(distance<closestDistance  ) {
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
    public void setOrangeBalls(List<TennisBall> newOrangeList) {
        orangeBalls = newOrangeList;
    }



    public Edge getEdge() {
        return edge;
    }

    /**
     * Looks at two vectors from the two sides of the robot with the same heading as the robot. Returns the shortest distance on either of these vectors to the edge of the map.
     * @return
     */
    public double distanceToEdge(Point heading) {
        // Returns the distance of the robot to the edge of the map.

        // Starting point of rightSide vector
        Point rightSide = new Point(robotPosition.rightSideX, robotPosition.rightSideY);

        // Starting point of leftSide vector
        Point leftSide = new Point(robotPosition.leftSideX, robotPosition.leftSideY);

        // Lines of the robot
        Line2D leftRobotLine = new Line2D.Double(leftSide.x, leftSide.y, leftSide.x+1000*heading.x, leftSide.y+1000*heading.y);
        Line2D rightRobotLine = new Line2D.Double(rightSide.x, rightSide.y, rightSide.x+1000*heading.x, rightSide.y+1000*heading.y);

        // Edge points of the map

        Point topLeft = edge.getTopLeft();
        Point topRight = edge.getTopRight();
        Point bottomLeft = edge.getBottomLeft();
        Point bottomRight = edge.getBottomRight();

        // Lines of the map
        Line2D.Double[] edges = {new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y),
                new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y),
                new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y),
                new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y)};

        double distanceFromRightSideRobotToEdge = Double.MAX_VALUE;
        double distanceFromLeftSideRobotToEdge = Double.MAX_VALUE;

        double shortestDistance = Double.MAX_VALUE;

        // Looks through all 4 edges, calculates the distance from the two robot sides to the edge,
        // and if the distance found is shorter than the currently shortest distance, it is set as the new shortest distance.
        for(Line2D edge : edges){
            if(rightRobotLine.intersectsLine(edge)){
                distanceFromRightSideRobotToEdge=distanceBetweenPoints(rightSide,intersection(rightRobotLine, edge));
                if(distanceFromRightSideRobotToEdge<shortestDistance){
                    shortestDistance = distanceFromRightSideRobotToEdge;
                }
            }
            if(leftRobotLine.intersectsLine(edge)){
                distanceFromLeftSideRobotToEdge=distanceBetweenPoints(leftSide,intersection(leftRobotLine, edge));
                if(distanceFromLeftSideRobotToEdge<shortestDistance){
                    shortestDistance = distanceFromLeftSideRobotToEdge;
                }
            }
        }

        return shortestDistance;
    }

    public RobotPosition getRobotPosition() {
        return robotPosition;
    }

    public List<TennisBall> getBalls() {
        return balls;
    }
    public List<TennisBall> getOrangeBalls(){
        return orangeBalls;
    }

}
