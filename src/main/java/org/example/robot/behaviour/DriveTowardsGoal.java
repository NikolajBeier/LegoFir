package org.example.robot.behaviour;

import org.example.mapping.Map;
import org.example.mapping.RobotPosition;
import org.example.robot.model.Legofir;
import org.example.utility.Geometry;
import org.opencv.core.Point;
import org.example.robot.behaviour.Navigation;

public class DriveTowardsGoal implements MyBehavior{
    Legofir dude;
    String BehaviorName = "DriveTowardsGoal";
    boolean suppressed = false;
    boolean stopCondition = false;
    double currentAngle;
    double angleToLine;
    double distanceToLine;
    Navigation navigation;

    public DriveTowardsGoal(Legofir dude) {
        this.dude = dude;
        navigation = new Navigation(dude);
    }

    @Override
    public boolean takeControl() {
        if (dude.getMap().getOrangeBalls().isEmpty() && dude.getMap().getBalls().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        suppressed=false;
        dude.setCurrentBehaviourName(BehaviorName);

        while (!suppressed){

           navigation.checkDirection(dude.getMap().getWayPoint());
           dude.moveForward();




            /*RobotPosition robotPosition = dude.getMap().getRobotPosition();

            int getLineX = 0;
            int getlineY = 0;

            currentAngle = dude.getAngle();

            Point lineVector = new Point(getLineX-robotPosition.getX(), getlineY-robotPosition.getY());
            // Vinkel af vektor...
            angleToLine= Geometry.degreesOfVectorInRadians(lineVector.x,lineVector.y);



             */


        }



    }

    @Override
    public void suppress() { suppressed =true; }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition=stopCondition;
        suppressed= true;
        }
}

