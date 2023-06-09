package org.example.robot.behaviour;

import org.example.mapping.TennisBall;
import org.opencv.core.Point;

public class BallDistanceToWall {



    public BallDistanceToWall(TennisBall tennisBall){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());

    }
}
