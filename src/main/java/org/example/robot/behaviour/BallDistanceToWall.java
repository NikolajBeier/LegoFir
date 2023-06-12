package org.example.robot.behaviour;

import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class BallDistanceToWall {



    public double BallDistanceToWall(TennisBall tennisBall, Legofir dude){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());
        double distanceToEdge= dude.getMap().distanceToEdge(point);



        return distanceToEdge;

    }
}
