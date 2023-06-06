package org.example.robot.behaviour;

import org.example.robot.model.Legofir;
import static java.lang.Thread.sleep;

public class DropBalls implements MyBehavior {

    Legofir dude;
    String BehaviorName = "DropBalls";
    boolean suppressed = false;
    boolean stopCondition = false;

    int test = 1;

    public DropBalls(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public void action() {
        suppressed = false;
        dude.setCurrentBehaviourName(BehaviorName);
        if(!suppressed){
            dude.openCheeks();
            dude.beginHarvester();
            try {
                sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dude.closeCheeks();
            dude.stopHarvester();
            dude.closePorts();

        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void setStopCondition(Boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    @Override
    public boolean takeControl() {
        /// needs to be somehthing else
        return true;
    }

}
