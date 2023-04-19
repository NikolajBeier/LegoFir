package org.example.mapping;

import org.opencv.core.Point;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Dijkstras_Algorithm {

    Point point;

    RobotPosition startPos = new RobotPosition(0, 0, point );
    TennisBall tennis1 = new TennisBall(10, 10);
    TennisBall tennis2 = new TennisBall(20, 20);
    TennisBall tennis3 = new TennisBall(30, 30);



    Map map = new Map(180, 120);
    TennisBall defaultsize = new TennisBall(map.x, map.y);

    //TennisBall[] Queue = new TennisBall[ballarray.size()];





    private Set<Node> nodes = new HashSet<>();

}
