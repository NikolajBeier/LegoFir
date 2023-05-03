package org.example.mapping;

import org.example.ui.Calibration.Color;

public class ObjectColor {
    static Color whiteBall, orangeBall, edge, blueRobot, greenRobot;

    public static Color getWhiteBall() {
        return whiteBall;
    }

    public static void setWhiteBall(Color whiteBall) {
        ObjectColor.whiteBall = whiteBall;
    }

    public static Color getOrangeBall() {
        return orangeBall;
    }

    public static void setOrangeBall(Color orangeBall) {
        ObjectColor.orangeBall = orangeBall;
    }

    public static Color getEdge() {
        return edge;
    }

    public static void setEdge(Color edge) {
        ObjectColor.edge = edge;
    }

    public static Color getBlueRobot() {
        return blueRobot;
    }

    public static void setBlueRobot(Color blueRobot) {
        ObjectColor.blueRobot = blueRobot;
    }

    public static Color getGreenRobot() {
        return greenRobot;
    }

    public static void setGreenRobot(Color greenRobot) {
        ObjectColor.greenRobot = greenRobot;
    }
}
