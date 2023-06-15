package org.example.mapping;

import org.example.utility.Geometry;
import org.opencv.core.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static org.example.utility.Geometry.distanceBetweenPoints;
import static org.example.utility.Geometry.intersection;

public class Map {

    int x;
    int y;
    int size;
    Edge edge = new Edge();
    Edge unwarpedEdge = new Edge();
    Obstacle obstacle = new Obstacle();
    RobotPosition robotPosition = new RobotPosition();
    List<TennisBall> balls = new ArrayList<>();
    List<TennisBall> orangeBalls = new ArrayList<>();

    DepositPoint depositPoint = new DepositPoint(this.edge);

    Point goalWayPoint = new Point();
    Point ballNextToWallWaypoint = new Point();


    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = x * y;
    }

    public Point getWayPoint() {
        return goalWayPoint;
    }

    public void setWayPoint(double x, double y) {
        this.goalWayPoint.x = x;
        this.goalWayPoint.y = y;
    }

    public Point getBallNextToWallWaypoint() {
        return ballNextToWallWaypoint;
    }

    public void setBallNextToWallWaypoint(double x, double y) {
        this.ballNextToWallWaypoint = ballNextToWallWaypoint;
    }

    public void addBallCord(int x, int y) {
        TennisBall tennisball = new TennisBall(x, y, null, false, false);
        balls.add(tennisball);
    }

    public void setRobotPosition(int x, int y, Point heading) {
        robotPosition.x = x;
        robotPosition.y = y;
        robotPosition.heading = heading;
    }

    public void removeAllBalls() {
        balls.clear();
    }


    public double distanceFromStartingPointToEdge(Point startingPoint, Line2D cardinalDirection, Line2D edge) {
        return distanceBetweenPoints(startingPoint, intersection(cardinalDirection, edge));
    }

    public double getDistanceFromStartingPointToEdge(Point startingPoint, Line2D cardinalDirection, Line2D edge) {
        return distanceFromStartingPointToEdge(startingPoint, cardinalDirection, edge);
    }

    public TennisBall getNextBall() {
        // find the tennis ball closest to the robot
        TennisBall closestBall = new TennisBall(0, 0, null, false, false);
        double closestDistance = Integer.MAX_VALUE;

        //  checks if there is a orange ball sets it as closestball
        if (!orangeBalls.isEmpty()) {
            for (TennisBall orangeTennisBall : orangeBalls) {
                closestBall = orangeTennisBall;
            }
        } else {
            for (TennisBall tennisball : balls) {
                // Distance between two points:
                double distance = Math.sqrt((tennisball.x - getRobotPosition().frontSideX) * (tennisball.x - getRobotPosition().frontSideX) + (tennisball.y - getRobotPosition().frontSideY) * (tennisball.y - getRobotPosition().frontSideY));
                double robotAngle = getRobotPosition().getHeadingInRadians();
                double ballAngle = Geometry.degreesOfVectorInRadians(tennisball.x, tennisball.y);
            /*
            // checks if ball is right next to robot and chooses another ball
            if(distance<closestDistance && distance<100 && ballAngle<robotAngle+0.25 || ballAngle>robotAngle-0.25 ) {

                //TODO skal lave noget kode der sørger for at robotten ikke kører ind i bolden
            } else*/
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestBall = tennisball;
                }
            }
        }

        setBallsClosetowalls(closestBall);
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


    public void calcDepositPoints() {
        this.depositPoint.calcExit();
        this.depositPoint.setCoords();
    }

    public Edge getEdge() {
        return edge;
    }

    /**
     * Looks at two vectors from the two sides of the robot with the same heading as the robot. Returns the shortest distance on either of these vectors to the edge of the map.
     *
     * @return
     */
    public double distanceToEdge(Point heading, Point startingPoint) {

        // Line shooting out from the starting point
        Line2D line = new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * heading.x, startingPoint.y + 10000 * heading.y);

        // Edge points of the map

        Point topLeft = edge.getTopLeft();
        Point topRight = edge.getTopRight();
        Point bottomLeft = edge.getBottomLeft();
        Point bottomRight = edge.getBottomRight();
        Point topObstacle = obstacle.getTopPoint();
        Point bottomObstacle = obstacle.getBottomPoint();
        Point leftObstacle = obstacle.getLeftPoint();
        Point rightObstacle = obstacle.getRightPoint();


        // Lines of the map
        Line2D.Double[] edges = {new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y),
                new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y),
                new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y),
                new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y),
                new Line2D.Double(rightObstacle.x, rightObstacle.y, leftObstacle.x, leftObstacle.y),
                new Line2D.Double(topObstacle.x, topObstacle.y, bottomObstacle.x, bottomObstacle.y)
        };

        double distanceFromStartingPointToEdge = Double.MAX_VALUE;
        double shortestDistance = Double.MAX_VALUE;

        // Looks through all 4 edges, calculates the distance from the two robot sides to the edge,
        // and if the distance found is shorter than the currently shortest distance, it is set as the new shortest distance.
        for (Line2D edge : edges) {
            if (line.intersectsLine(edge)) {
                distanceFromStartingPointToEdge = distanceBetweenPoints(startingPoint, intersection(line, edge));
                if (distanceFromStartingPointToEdge < shortestDistance) {
                    shortestDistance = distanceFromStartingPointToEdge;
                }
            }
        }

        return shortestDistance;
    }

    public Double distanceToEdge(Point startingPoint) {

        Line2D[] cardinalDirections = {
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * 1, startingPoint.y + 10000 * 0),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * 0, startingPoint.y + 10000 * 1),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * -1, startingPoint.y + 10000 * 0),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * 0, startingPoint.y + 10000 * -1)
        };

        try {

            // Edge points of the map
            Point topLeft = edge.getTopLeft();
            Point topRight = edge.getTopRight();
            Point bottomLeft = edge.getBottomLeft();
            Point bottomRight = edge.getBottomRight();


            // Lines of the map
            Line2D.Double[] edges = {
                    new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y),
                    new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y),
                    new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y),
                    new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y),
            };

            double shortestDistance = Double.MAX_VALUE;

            for (Line2D edge : edges) {
                for (Line2D cardinalDirection : cardinalDirections) {
                    if (cardinalDirection.intersectsLine(edge)) {
                        double distanceFromStartingPointToEdge = distanceBetweenPoints(startingPoint, intersection(cardinalDirection, edge));
                        if (distanceFromStartingPointToEdge < shortestDistance) {
                            shortestDistance = distanceFromStartingPointToEdge;
                        }
                    }
                }
                return shortestDistance;
            }

        } catch (Exception e) {
            System.out.println("Edge not set");
        }


        /*
        // For each edge line, calculate the shortest distance to it from the starting point
        for(Line2D edge : edges){
            double distanceFromStartingPointToEdge = shortestDistanceToLineSegment(startingPoint, edge);
            if(distanceFromStartingPointToEdge < shortestDistance){
                shortestDistance = distanceFromStartingPointToEdge;
            }
        }

         */
        return null;
    }

    public Direction FindNearestWall(Point startingPoint, Double distanceToEdge) {
        Line2D[] cardinalDirections = {
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000, startingPoint.y),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x, startingPoint.y + 10000),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x + 10000 * -1, startingPoint.y),
                new Line2D.Double(startingPoint.x, startingPoint.y, startingPoint.x, startingPoint.y + 10000 * -1)
        };


        // Edge points of the map
        Point topLeft = edge.getTopLeft();
        Point topRight = edge.getTopRight();
        Point bottomLeft = edge.getBottomLeft();
        Point bottomRight = edge.getBottomRight();
        // direction to be returned
        Direction closestwallheading = null;

        // Lines of the map

        Line2D North = new Line2D.Double(topLeft.x, topLeft.y, topRight.x, topRight.y);
        Line2D South = new Line2D.Double(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y);
        Line2D West = new Line2D.Double(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
        Line2D East = new Line2D.Double(topRight.x, topRight.y, bottomRight.x, bottomRight.y);
        System.out.println(cardinalDirections[0] + " \t" + cardinalDirections[1] + " \t" + cardinalDirections[2] + " \t" + cardinalDirections[3]);

        for (Line2D dir : cardinalDirections) {

        }


        System.out.println(distanceToEdge);
        for (Line2D dir : cardinalDirections) {
            //if (distanceToEdge>=distanceToEdge(startingPoint)){

            if (dir.intersectsLine(North)) {
                if (distanceToEdge >= distanceBetweenPoints(startingPoint, intersection(dir, North)))
                    return Direction.NORTH;
            }
            if (dir.intersectsLine(South)) {
                if (distanceToEdge >= distanceBetweenPoints(startingPoint, intersection(dir, South)))
                    return Direction.SOUTH;
            }
            if (dir.intersectsLine(West)) {
                if (distanceToEdge >= distanceBetweenPoints(startingPoint, intersection(dir, West)))
                    return Direction.WEST;
            }
            if (dir.intersectsLine(East)) {
                if (distanceToEdge >= distanceBetweenPoints(startingPoint, intersection(dir, East)))
                    return Direction.EAST;
            }
        }


       /*     for (Line2D cardinalDirection : cardinalDirections) {
                System.out.println("hello1111");
                if (cardinalDirection.intersectsLine(edge)) {
                    System.out.println("hello2222");
                    if (distanceToEdge <= distanceToEdge(startingPoint))
                        System.out.println("hello3333");
                    closestwallheading = findNearestWall(edges, cardinalDirection);


                }*/


        return closestwallheading;
}

public enum Direction {
    NORTH, SOUTH, EAST, WEST}

    public DepositPoint getDepositPoint() {
        return this.depositPoint;
    }

    public RobotPosition getRobotPosition() {
        return robotPosition;
    }

    public List<TennisBall> getBalls() {
        return balls;
    }

    public List<TennisBall> getOrangeBalls() {
        return orangeBalls;
    }

    public void setObstacle(Point topPoint, Point bottomPoint, Point leftPoint, Point rightPoint) {
        obstacle.setTopPoint(topPoint);
        obstacle.setBottomPoint(bottomPoint);
        obstacle.setLeftPoint(leftPoint);
        obstacle.setRightPoint(rightPoint);
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setUnWarpedEdges(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, int height, int width) {
        unwarpedEdge.setAll(topLeft, topRight, bottomLeft, bottomRight, height, width);
    }

    public Edge getUnwarpedEdge() {
        return unwarpedEdge;
    }

    public Point[] getUnWarpedEdges() {
        return new Point[]{unwarpedEdge.getBottomLeft(), unwarpedEdge.getTopLeft(), unwarpedEdge.getBottomRight(), unwarpedEdge.getTopRight()};
    }

    public Direction findNearestWall(Line2D.Double[] edges, Line2D line) {
        if (edges[0].equals(line)) {
            return Direction.EAST;
        } else if (edges[1].equals(line)) {
            return Direction.SOUTH;
        } else if (edges[2].equals(line)) {
            return Direction.WEST;
        } else if (edges[3].equals(line)) {
            return Direction.NORTH;
        } else {
            return null;
        }
    }

    private void setBallsClosetowalls(TennisBall ball) {
        try {


            double distanceToEdge;
            Point point = new Point(ball.getX(), ball.getY());
            distanceToEdge = distanceToEdge(point);
            if (distanceToEdge < 100) {
                ball.setClosetsWall(FindNearestWall(point, distanceToEdge));
                System.out.println("ball is close to wall");
                ball.setCloseToWall(true);
                System.out.println(ball.getClosetswall());
                switch (ball.getClosetswall()) {
                    case NORTH -> {
                        ballNextToWallWaypoint = new Point(ball.getX(), ball.getY() - 100);
                        System.out.println("N created");
                    }
                    case SOUTH -> {
                        ballNextToWallWaypoint = new Point(ball.getX(), ball.getY() + 100);
                        System.out.println("S created");
                    }
                    case EAST -> {
                        ballNextToWallWaypoint = new Point(ball.getX() - 100, ball.getY());
                        System.out.println("E created");
                    }
                    case WEST -> {
                        ballNextToWallWaypoint = new Point(ball.getX() + 100, ball.getY());
                        System.out.println("W created");
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + ball.getClosetswall());
                }

            }
        } catch (Exception e) {
            System.out.println("ball not set");
        }
    }

}

