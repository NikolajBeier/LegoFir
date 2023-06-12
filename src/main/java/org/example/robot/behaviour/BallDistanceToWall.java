package org.example.robot.behaviour;

import org.example.mapping.Map;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class BallDistanceToWall {



    public Map.direction BallDistanceToWall(TennisBall tennisBall, Legofir dude){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());
        Map.direction distanceToEdge= dude.getMap().distanceToEdge(point);



        return distanceToEdge;

    }
}
