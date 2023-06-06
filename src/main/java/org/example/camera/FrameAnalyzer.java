package org.example.camera;

import org.example.mapping.Edge;
import org.example.mapping.Map;
import org.example.mapping.RobotPosition;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.example.ui.CameraAnalyze;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.example.utility.Geometry.distanceBetweenPoints;
import static org.example.utility.Geometry.intersection;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class FrameAnalyzer {
    Legofir dude;
    CameraAnalyze cameraAnalyze;
    BallDetection ballDetection = new BallDetection();
    OrangeBallDetection orangeBallDetection = new OrangeBallDetection();
    RobotDetection robotDetection = new RobotDetection();
    EdgeDetection edgeDetection = new EdgeDetection();
    ObstacleDetection obstacleDetection = new ObstacleDetection();
    Boolean ballDetectionOn = false;
    Boolean robotDetectionOn = false;
    Boolean edgeDetectionOn = false;
    java.util.List<Rect> blue = new ArrayList<>();
    java.util.List<Rect> green = new ArrayList<>();
    java.util.List<Rect> robot = new ArrayList<>();
    java.util.List<Rect> ballRects = new ArrayList<>();
    java.util.List<Rect> orangeBallRects = new ArrayList<>();

    Point topObstacle = null;
    Point bottomObstacle = null;
    Point leftObstacle = null;
    Point rightObstacle = null;
    private VideoCapture capture;
    Mat webcamImage = new Mat();
    byte[] imageData;
    int counter=0;

    public FrameAnalyzer(Legofir dude, CameraAnalyze cameraAnalyze) {
        this.dude = dude;
        this.cameraAnalyze = cameraAnalyze;
        capture = new VideoCapture(0);
        EventQueue.invokeLater(() -> new Thread(() -> {
                    while (true) {
                        capture.read(webcamImage);
                        cameraAnalyze.updateUI(analyzeFrame(webcamImage));
                    }
                }).start()
        );
    }

    private ImageIcon analyzeFrame(Mat frame) {

        // resize image
        resize(webcamImage, frame, new Size(1280, 720));
        //clahe(frame);

        // Find the stuff
        detectEdges(frame);
        detectRobot(frame);
        detectBalls(frame);

        // Draw the stuff
        drawOrangeBalls(frame);
        drawWhiteBalls(frame);
        drawEdges(frame);
        drawObstacle(frame);
        drawExitHoles(frame);
        drawRobot(frame);
        drawCollision(frame);

        // convert matrix to byte
        final MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, buf);
        imageData = buf.toArray();

        // Increment frame counter
        incrementFrameCounter();


        return new ImageIcon(imageData);
    }

    private void incrementFrameCounter() {
        counter++;
        if(counter == Integer.MAX_VALUE) {
            counter = 0;
        }
    }

    private void drawExitHoles(Mat frame) {
        if(edgeDetectionOn) {
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
        if(robotDetectionOn && edgeDetectionOn){
            drawCollisionDetection(frame);
        }
    }

    private void drawRobot(Mat frame) {
        if(robotDetectionOn) {
            // Blue, green and robot rects
            for (Rect boundingRect : blue) {
                Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 1);
                putText(frame, "blue", boundingRect.tl(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
            }
            for (Rect boundingRect : green) {
                Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 1);
                putText(frame, "green", boundingRect.tl(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
            }
            for (Rect boundingRect : robot) {
                Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 1);
                putText(frame, "robot", boundingRect.tl(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
            }
            // back and front points of robot
            Point front = new Point(dude.getMap().getRobotPosition().getFrontSideX(), -dude.getMap().getRobotPosition().getFrontSideY());
            Imgproc.circle(frame, front, 1, new Scalar(0, 0, 255), -1);
            Imgproc.putText(frame, "Front", front, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);

            Point back = new Point(dude.getMap().getRobotPosition().getBackSideX(), -dude.getMap().getRobotPosition().getBackSideY());
            Imgproc.circle(frame, back, 1, new Scalar(0, 0, 255), -1);
            Imgproc.putText(frame, "Back", back, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);


            if (!green.isEmpty() && !blue.isEmpty()) {
                for (Rect blueBoundingRect : blue) {
                    for (Rect greenBoundingRect : green) {

                        Point blueCenter = new Point(blueBoundingRect.x + blueBoundingRect.width * 0.5, blueBoundingRect.y + blueBoundingRect.height * 0.5);
                        Point greenCenter = new Point(greenBoundingRect.x + greenBoundingRect.width * 0.5, greenBoundingRect.y + greenBoundingRect.height * 0.5);
                        circle(frame, blueCenter, 1, new Scalar(0, 0, 255), 1);
                        circle(frame, greenCenter, 1, new Scalar(0, 0, 255), 1);

                        Point centerOfLine = new Point((blueCenter.x + greenCenter.x) * 0.5, (blueCenter.y + greenCenter.y) * 0.5);

                        Point vectorFromBlueToGreen = new Point(greenCenter.x - blueCenter.x, greenCenter.y - blueCenter.y);

                        Point perpendicularVector = new Point(vectorFromBlueToGreen.y, -vectorFromBlueToGreen.x);

                        Point arrowPoint = new Point(centerOfLine.x + perpendicularVector.x, centerOfLine.y + perpendicularVector.y);


                        circle(frame, centerOfLine, 2, new Scalar(0, 0, 255), 2);


                        line(frame, blueCenter, greenCenter, new Scalar(0, 0, 255), 1);
                        //System.out.println("Blue: " + blueCenter.toString() + " Green: " + greenCenter.toString() + " Center: " + centerOfLine.toString() + " Arrow: " + arrowPoint.toString());
                        arrowedLine(frame, centerOfLine, arrowPoint, new Scalar(0, 0, 255), 1);

                        if (dude != null) {
                            TennisBall nextBall = dude.getMap().getNextBall();

                            int nextBallX = nextBall.getX();
                            int nextBallY = nextBall.getY();

                            arrowedLine(frame, centerOfLine, new Point(nextBallX, -nextBallY), new Scalar(0, 255, 0), 1);
                        }
                    }
                }
            }
        }
    }

    private void drawObstacle(Mat frame) {
        // draw obstacle
        if(edgeDetectionOn){
            line(frame, new Point(dude.getMap().getObstacle().getTopPoint().x,-dude.getMap().getObstacle().getTopPoint().y), new Point(dude.getMap().getObstacle().getBottomPoint().x,-dude.getMap().getObstacle().getBottomPoint().y), new Scalar(0, 255, 0), 2, 8, 0);
            line(frame, new Point(dude.getMap().getObstacle().getLeftPoint().x,-dude.getMap().getObstacle().getLeftPoint().y), new Point(dude.getMap().getObstacle().getRightPoint().x,-dude.getMap().getObstacle().getRightPoint().y), new Scalar(0, 255, 0), 2, 8, 0);
        }

    }

    private void drawEdges(Mat frame) {
    }

    private void drawWhiteBalls(Mat frame) {
        for (Rect boundingRect : ballRects) {
            Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 1);
            putText(frame, "Ball", boundingRect.tl(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
        }
    }

    private void drawOrangeBalls(Mat frame) {
        for (Rect boundingRect : orangeBallRects){
            Imgproc.rectangle(frame,boundingRect.tl(),boundingRect.br(),new Scalar(0,0,255, 1));
            putText(frame, "Orange ball",boundingRect.tl(), FONT_HERSHEY_SIMPLEX,1,new Scalar(0,0,255),2);
        }
    }

    private void detectBalls(Mat frame) {
        // Find the balls
        if(ballDetectionOn){
            orangeBallRects = orangeBallDetection.detect(frame,dude);
            ballRects = ballDetection.detect(frame,dude);
        }
    }

    private void detectRobot(Mat frame) {
        // Find the robot
        if(robotDetectionOn){
            java.util.List<Rect>[] robotRects = robotDetection.detect(frame,dude);
            robot = robotRects[2];
            blue = robotRects[1];
            green = robotRects[0];
        }
    }

    private void detectEdges(Mat frame) {
        // Edge and obstacle detection every 100 frames
        if (edgeDetectionOn && counter%100==0) {
            edgeDetection.intersectionDetect(frame, dude);
            obstacleDetection.detect(frame, topObstacle, bottomObstacle, leftObstacle, rightObstacle, dude);
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
                break;
            }
            case MOVING_FORWARD: {
                drawLinesToEdge(image,heading,leftSide);
                drawLinesToEdge(image,heading,rightSide);
                drawLinesToEdge(image,heading,middle);
                break;
            }
            case TURNING_LEFT: {
                drawLinesToEdge(image,leftHeading,frontSide);
                drawLinesToEdge(image,rightHeading,backSide);
                break;
            }
            case TURNING_RIGHT: {
                drawLinesToEdge(image,rightHeading,frontSide);
                drawLinesToEdge(image,leftHeading,backSide);
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

    /*
    public void ColorDetector() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 2));
        JPanel buttonsSliders = new JPanel();
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(new Dimension(width, height));
        jFrame.getContentPane().removeAll();

        JLabel colorCameraScreen = new JLabel();
        if(camWidth>width|| camHeight>height){
            colorCameraScreen.setBounds(0, 0, width, height-200);
        } else {
            colorCameraScreen.setBounds(0, 0, camWidth, camHeight);
        }
        jFrame.add(colorCameraScreen);
        JButton findValues = new JButton("Find Values");
        JPanel sliders = new JPanel();
        sliders.setLayout(new GridLayout(2, 6));
        JSlider hueMin = new JSlider(0, 255, 0);
        JSlider hueMax = new JSlider(0, 255, 0);
        JSlider satMin = new JSlider(0, 255, 0);
        JSlider satMax = new JSlider(0, 255, 180);
        JSlider valMin = new JSlider(0, 255, 0);
        JSlider valMax = new JSlider(0, 255, 255);
        JLabel hueMinName = new JLabel("Hue Min (B Min)");
        JLabel hueMaxName = new JLabel("Hue Max (B Max)");
        JLabel satMinName = new JLabel("Sat Min (G Min)");
        JLabel satMaxName = new JLabel("Sat Max (G Max)");
        JLabel valMinName = new JLabel("Val Min (R Min)");
        JLabel valMaxName = new JLabel("Val Max (R Max)");
        sliders.add(hueMinName);
        sliders.add(hueMaxName);
        sliders.add(satMinName);
        sliders.add(satMaxName);
        sliders.add(valMinName);
        sliders.add(valMaxName);
        sliders.add(hueMin);
        sliders.add(hueMax);
        sliders.add(satMin);
        sliders.add(satMax);
        sliders.add(valMin);
        sliders.add(valMax);
        sliders.setBounds(0, camHeight, camWidth, 100);
        jFrame.add(sliders);

        findValues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hue Min: "+hueMin.getValue());
                System.out.println("Hue Max: "+hueMax.getValue());
                System.out.println("Sat Min: "+satMin.getValue());
                System.out.println("Sat Max: "+satMax.getValue());
                System.out.println("Val Min: "+valMin.getValue());
                System.out.println("Val Max: "+valMax.getValue());
            }
        });
        buttonsSliders.add(findValues);


        mainPanel.setPreferredSize(new Dimension(width, height-130));
        mainPanel.add(cameraScreen);
        mainPanel.setPreferredSize(new Dimension(width, height - 130));
        mainPanel.add(cameraScreen);
        mainPanel.add(colorCameraScreen);

        buttonsSliders.setPreferredSize(new Dimension(width, 130));
        buttonsSliders.add(buttons);
        buttonsSliders.add(sliders);


        jFrame.add(mainPanel, BorderLayout.CENTER);
        jFrame.add(buttonsSliders, BorderLayout.SOUTH);
        jFrame.setLocationRelativeTo(null);
        jFrame.revalidate();
        jFrame.repaint();
        jFrame.setVisible(true);

        Mat image;
        byte[] imageData;
        ImageIcon icon;
        Mat mask = new Mat();

        while (true) {
            if (capture != null) {
                image = correctedImage;
                //Post proccessing to smooth the image


                Scalar minValues = new Scalar(hueMin.getValue(), satMin.getValue(), valMin.getValue());
                Scalar maxValues = new Scalar(hueMax.getValue(), satMax.getValue(), valMax.getValue());
                Core.inRange(image, minValues, maxValues, mask);

                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", mask, buf);

                imageData = buf.toArray();

                // Add to JLabel
                icon = new ImageIcon(imageData);

                colorCameraScreen.setIcon(icon);
            }
        }
    }
    */
    public Mat removeglare(Mat image){
        Mat corrected = new Mat();
        CLAHE rg = createCLAHE();
        rg.apply(image, corrected);
        return corrected;
    }

    private Mat clahe(Mat capture) {
        List<Mat> channels = new LinkedList<>();
        Core.split(capture,channels);
        CLAHE clahe = Imgproc.createCLAHE();
        Size point = new Size(new Point(1,1));
        clahe.setClipLimit(4);
        clahe.setTilesGridSize(point);
        //System.out.println(capture);
        Mat destimage = new Mat();
        clahe.apply(channels.get(0),destimage);
        Core.merge(channels,capture);
        cvtColor(capture,destimage, 1);

        return destimage;
    }

    public void setBallDetection(boolean b) {
        ballDetectionOn = b;
    }
    public boolean isBallDetectionOn() {
        return ballDetectionOn;
    }

    public void setEdgeDetection(boolean b) {
        edgeDetectionOn = b;
    }

    public boolean isEdgeDetectionOn() {
        return edgeDetectionOn;
    }

    public void setRobotDetection(boolean b) {
        robotDetectionOn = b;
    }

    public boolean isRobotDetectionOn() {
        return robotDetectionOn;
    }
}
