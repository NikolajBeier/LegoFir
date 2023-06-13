package org.example.robot.behaviour;

import org.example.mapping.Map;
import org.example.mapping.TennisBall;
import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class BallDistanceToWall {



    public Map.Direction BallHeadingtoWall(TennisBall tennisBall, Legofir dude){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());
        Map.Direction headingToEdge = dude.getMap().FindNearestWall(point,tennisBall.getDistanceToClosestWall());


        return headingToEdge;

    }
    public double shortestDistanceToWall(TennisBall tennisBall, Legofir dude){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());
        double distance = dude.getMap().distanceToEdge(point);
        return distance;
    }

    public  boolean isCloseToWall(TennisBall tennisBall, Legofir dude){
        Point point = new Point(tennisBall.getX(),tennisBall.getY());
        double distance = dude.getMap().distanceToEdge(point);
        if(distance < 100){
            return true;
        }
        return false;
    }
}