package org.example.robot.behaviour;

import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.example.robot.Legofir;
import static java.lang.Thread.sleep;

public class DropBalls implements MyBehavior {

    Legofir dude;
    Boolean suppressed = false;
    Boolean stopCondition = false;

    int test = 1;

    public DropBalls(Legofir dude) {
        this.dude = dude;
    }


    @Override
    public void action() {
        suppressed = false;
        if(!suppressed){
            dude.openCheeks();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dude.closeCheeks();
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
        return (test > 0);
    }

}
