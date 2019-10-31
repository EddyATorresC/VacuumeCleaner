/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

import agent.Action;
import agent.Agent;
import agent.Percept;
import java.util.Random;
import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import vacworld.VacPercept;

/* Change the code as appropriate.  This code
   is here to help you understand the mechanism
   of the simulator. 
*/

public class VacAgent extends Agent {

    private final String ID = "1";
    // Think about locations you already visited.  Remember those.
    private boolean dirtStatus = true;
    private boolean bumpFeltInPrevMove = true;
    private boolean obstacleInFront = true;

    public void see(Percept p) {

        VacPercept vp = (VacPercept) p;
        dirtStatus = vp.seeDirt();
        bumpFeltInPrevMove = vp.feelBump();
        obstacleInFront = vp.seeObstacle();
    }

    public Action selectAction() {
        Action action = new ShutOff();
        SuckDirt suckDirt = new SuckDirt();
        TurnLeft turnLeft = new TurnLeft();
        TurnRight turnRight = new TurnRight();
        GoForward goForward = new GoForward();
        
        Random r = new Random(System.currentTimeMillis());
        
        r.setSeed(System.currentTimeMillis());
        float prob;
        
        if (obstacleInFront) {
            prob = r.nextFloat();
            if (prob < 0.5) {
                return turnLeft;
            } else {
                return turnRight;
            }
        } else if (dirtStatus) {
            return suckDirt;
        } else if(bumpFeltInPrevMove){
            System.out.println("sintio la pared");
             prob = r.nextFloat();
            if (prob < 0.5) {
                return turnLeft;
            } else {
                return turnRight;
            }
        }else{
            prob = r.nextFloat();
            System.out.println("otra");
            System.err.println(prob);
            if (prob < 0.15) {
                return turnLeft;
            } else if (prob < 0.30) {
                return turnRight;
            } else if (prob < 0.95) {
                return goForward;
            } else {
                return action;
            }
        }

    }

    public String getId() {
        return ID;
    }

}
