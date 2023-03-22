package org.example.mapping;

import java.util.LinkedList;

public class Map {

    int x;
    int y;

    int size;

    LinkedList<TennisBall> balls;


    public Map(int x, int y, LinkedList<TennisBall> balls) {
        this.x = x;
        this.y = y;
        this.balls = balls;
        this.size = x*y;
    }
}
