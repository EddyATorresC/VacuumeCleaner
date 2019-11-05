/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

import agent.Action;
import java.util.HashMap;
import java.util.LinkedList;
import vacworld.Direction;
import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import vacworld.VacPercept;

/**
 *
 * @author eddy
 */
public class InternalState {
    
    private final HashMap<MyVector, LocationInformation> worldMap;

    private int agentDirection;
    private MyVector agentPosition;
    private boolean obstacleSeen;
    private boolean feltBump;
    private boolean dirtSeen;
    private boolean turnedOff;

    public InternalState() {
        worldMap = new HashMap<MyVector, LocationInformation>();
        agentDirection = Direction.NORTH; 
        agentPosition = new MyVector(0, 0); 
        worldMap.put(agentPosition, new LocationInformation(false)); 
        obstacleSeen = false;
    }


    public int getAgentDirection() {
        return agentDirection;
    }


    public MyVector getAgentPosition() {
        return agentPosition;
    }

 
    public HashMap<MyVector, LocationInformation> getWorldMap() {
        return worldMap;
    }

    public void update(VacPercept p) {
        // Ingresa informacion sobre la posicion actual del agente en el actual
        // conocimiento del mapa
        LocationInformation current;
        if (!worldMap.containsKey(agentPosition)) {
            current = new LocationInformation(false);
            worldMap.put(agentPosition, current);
        }

        // Actualiza la informacion de la ubicacion si esta esta sucia o no
        dirtSeen = p.seeDirt();
        feltBump = p.feelBump();

        current = worldMap.get(agentPosition);
        current.setDirty(dirtSeen);
        current.setExplored(true);

        // Revisa las posiciones adyacentes al agente
        MyVector aroundPosition;
        boolean obstacle;
        for (int i = Direction.NORTH; i <= Direction.WEST; ++i) {
            aroundPosition = new MyVector(agentPosition.getX()
                    + Direction.DELTA_X[i], agentPosition.getY()
                    + Direction.DELTA_Y[i]);

            obstacle = false; // Se asume que la posicion a revisar no es un obstaculo

            // Ubicacion en frente como se solicita
            if (i == agentDirection) {
                obstacleSeen = p.seeObstacle();
                obstacle = obstacleSeen;
            }

            if (!worldMap.containsKey(aroundPosition)) {
                worldMap.put(aroundPosition, new LocationInformation(obstacle));
            }

            // Si verdaderamente hay un obstaculo se debe actualizar esta informacion
            if (obstacle) {
                LocationInformation obstacleLoc = worldMap.get(aroundPosition);
                obstacleLoc.setObstacle(obstacle);
            }
        }
    }

    /*
    Se actualiza la posicion del agente
     */
    private void updatePosition(MyVector position) {
        this.agentPosition = position;
    }

    /*
    Se actualiza el InternalState en caso de que exista suciedad o no
     */
    private void updateDirty(MyVector position, boolean dirty) {
        // La posicion puede existir o no en el mapa, en caso de no existir se 
        // añade
        if (!worldMap.containsKey(position)) {
            worldMap.put(position, new LocationInformation(false));
        }

        LocationInformation current = worldMap.get(position);
        current.setDirty(dirty);
    }

    public boolean isLocationDirty(MyVector position) {
        LocationInformation loc = worldMap.get(position);
        if (loc != null) {
            return loc.isDirty();
        } else {
            return true;
        }
    }

    public boolean isLocationObstacle(MyVector position) {
        LocationInformation loc = worldMap.get(position);
        if (loc != null) {
            return loc.isObstacle();
        } else {
            return false;
        }
    }

    public boolean isLocationExplored(MyVector position) {
        LocationInformation loc = worldMap.get(position);
        if (loc != null) {
            return loc.isExplored();
        } else {
            return false;
        }
    }

    public boolean isLocationSeen(MyVector position) {
        return worldMap.containsKey(position);
    }

    public static int adjacentCost(MyVector start, MyVector end, int direction) {
        return Heuristics.estimateCost(start, end, direction); 
        //Esta Heuristica funciona con precision dado que se compara cuadros
        //adyacentes
    }

    /*
    Devuelve las cuadrillas posibles a ser exploradas
     */
    public LinkedList<MyVector> neighbors(MyVector position) {
        LinkedList<MyVector> actualNeighbors = new LinkedList<MyVector>();

        // Se a;ade solo si no se sabe que son obstacualos y no esiten en el mapa
        MyVector neighbor;
        for (int i = Direction.NORTH; i <= Direction.WEST; ++i) {
            neighbor = new MyVector(position.getX() + Direction.DELTA_X[i],
                    position.getY() + Direction.DELTA_Y[i]);

            if (worldMap.containsKey(neighbor) && !isLocationObstacle(neighbor)) {
                actualNeighbors.add(neighbor);
            }
        }

        return actualNeighbors;
    }

    /*
    Acutaliza el Internal State dada una accion
     */
    public void update(Action next) {
        if (next instanceof SuckDirt) { 
            updateDirty(agentPosition, false);
        } else if (next instanceof GoForward && !isFeltBump()) { 
            updatePosition(MyVector.add(agentPosition,
                    MyVector.directionToVector(agentDirection)));
        } else if (next instanceof TurnLeft) { 
            --agentDirection;
            if (agentDirection < Direction.NORTH) {
                agentDirection = Direction.WEST;
            }
        } else if (next instanceof TurnRight) {
            ++agentDirection;
            if (agentDirection > Direction.WEST) {
                agentDirection = Direction.NORTH;
            }
        } else if (next instanceof ShutOff) {
            turnedOff = true;
        }
    }

    public boolean isObstacleSeen() {
        return obstacleSeen;
    }

    public boolean isFeltBump() {
        return feltBump;
    }

    public boolean isTurnedOff() {
        return turnedOff;
    }

    public boolean isDirtSeen() {
        return dirtSeen;
    }
}
