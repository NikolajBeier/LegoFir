package org.example.mapping;

import org.w3c.dom.Node;

import java.lang.Math;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Dijkstras_Algorithm {

    TennisBall startPos = new TennisBall(0, 0);
    TennisBall tennis1 = new TennisBall(10, 10);
    TennisBall tennis2 = new TennisBall(20, 20);
    TennisBall tennis3 = new TennisBall(30, 30);

    LinkedList<TennisBall> ballarray = new LinkedList<>();



    Map map = new Map(100, 100, ballarray);
    TennisBall defaultsize = new TennisBall(map.x, map.y);

    TennisBall[] Queue = new TennisBall[ballarray.size()];





    private Set<Node> nodes = new HashSet<>();

}
