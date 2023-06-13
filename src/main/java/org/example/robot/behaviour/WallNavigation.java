package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import org.opencv.core.Point;

public class WallNavigation {
    Legofir dude;
    Navigation nav;
    Point nextBall = new Point(0,0);
    public WallNavigation(Legofir dude, Navigation nav) {
        this.dude = dude;
        this.nav = nav;
    }

    //needs an enum of Heading
    public void walldrive(Point waypoint) {
        nextBall = new Point(dude.getMap().getNextBall().getX(), dude.getMap().getNextBall().getY() );
        while (nav.myBehavior.isSuppressed()) {
            while(!checkIfRobotIsOnPoint()){
                nav.turnsTowardsWayPoint(waypoint);

            }
            nav.driveTowardsWaypoint(waypoint);
           // needs slowspeed




        }
    }
    public Boolean checkIfRobotIsOnPoint(){
        return (dude.getMap().getRobotPosition().getX() + 25 > dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getX() - 25 < dude.getMap().getWayPoint().x &&
                dude.getMap().getRobotPosition().getY() + 25 > dude.getMap().getWayPoint().y &&
                dude.getMap().getRobotPosition().getY() - 25 < dude.getMap().getWayPoint().y);
    }
}
