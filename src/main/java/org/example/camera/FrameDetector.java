package org.example.camera;

import org.example.robot.model.Legofir;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class FrameDetector {
    private Legofir dude;
    int counter=0;
    BallDetection ballDetection = new BallDetection();
    OrangeBallDetection orangeBallDetection = new OrangeBallDetection();
    RobotDetection robotDetection = new RobotDetection();
    EdgeDetection edgeDetection = new EdgeDetection();
    ObstacleDetection obstacleDetection = new ObstacleDetection();

    Boolean ballDetectionOn = false;
    Boolean robotDetectionOn = false;
    Boolean edgeDetectionOn = false;
    Boolean obstacleDetectionOn = false;

    Boolean runIntersectOnce = true;

    public FrameDetector(Legofir dude) {
        this.dude = dude;
    }
    public void detect(Mat frame) {
        detectEdges(frame);
        detectRobot(frame);
        detectBalls(frame);
        incrementFrameCounter();
    }
    private void detectBalls(Mat frame) {
        // Find the balls
        if(ballDetectionOn){
            orangeBallDetection.detect(frame,dude);
            ballDetection.detect(frame,dude);
        }
    }

    private void detectRobot(Mat frame) {
        // Find the robot
        if(robotDetectionOn){
            robotDetection.detect(frame,dude);
        }
    }

    private void detectEdges(Mat frame) {
        // Edge and obstacle detection every 10 frames
        if (edgeDetectionOn) {
            if(runIntersectOnce){
                edgeDetection.intersectionDetect(frame, dude);
                runIntersectOnce = false;
            }
            if(counter%60 == 0){
                edgeDetection.updateEdgeIntersections(frame,dude);
            }
            edgeDetection.warpToEdge(frame, dude.getMap().getUnWarpedEdges());
        }
        if(obstacleDetectionOn){

            obstacleDetection.detect(frame, dude);
        }
    }
    private void incrementFrameCounter() {
        counter++;
        if(counter == Integer.MAX_VALUE) {
            counter = 0;
        }
    }
    public void setBallDetectionOn(Boolean ballDetectionOn) {
        this.ballDetectionOn = ballDetectionOn;
    }

    public void setRobotDetectionOn(Boolean robotDetectionOn) {
        this.robotDetectionOn = robotDetectionOn;
    }

    public void setEdgeDetectionOn(Boolean edgeDetectionOn) {
        this.edgeDetectionOn = edgeDetectionOn;
    }
    public void setObstacleDetectionOn(Boolean obstacleDetectionOn){
        this.obstacleDetectionOn = obstacleDetectionOn;
    }
    public void setRunIntersectOnce(Boolean runIntersectOnce){
        this.runIntersectOnce = runIntersectOnce;
    }

    public Boolean isBallDetectionOn() {
        return ballDetectionOn;
    }

    public Boolean isRobotDetectionOn() {
        return robotDetectionOn;
    }

    public Boolean isEdgeDetectionOn() {
        return edgeDetectionOn;
    }
}
