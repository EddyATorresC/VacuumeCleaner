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
public class CentralConfig {
    
    private final HashMap<MyVector, PositionStatus> setInfo;

    private int direction;
    private MyVector position;
    
    private boolean isObstacle;
    private boolean isBump;
    private boolean isDirty;
    private boolean shutdown;

    public CentralConfig() {
        setInfo = new HashMap<MyVector, PositionStatus>();
        direction = Direction.NORTH; 
        position = new MyVector(0, 0); 
        setInfo.put(position, new PositionStatus(false)); 
        isObstacle = false;
    }


    public int getdirection() {
        return direction;
    }


    public MyVector getposition() {
        return position;
    }

 
    public HashMap<MyVector, PositionStatus> getsetInfo() {
        return setInfo;
    }

    public void update(VacPercept p) {
        // Ingresa informacion sobre la posicion actual del agente en el actual
        // conocimiento del mapa
        PositionStatus current;
        if (!setInfo.containsKey(position)) {
            current = new PositionStatus(false);
            setInfo.put(position, current);
        }

        // Actualiza la informacion de la ubicacion si esta esta sucia o no
        isDirty = p.seeDirt();
        isBump = p.feelBump();

        current = setInfo.get(position);
        current.setDirty(isDirty);
        current.setanalizado(true);

        // Revisa las posiciones adyacentes al agente
        MyVector aroundPosition;
        boolean obstacle;
        for (int i = Direction.NORTH; i <= Direction.WEST; ++i) {
            aroundPosition = new MyVector(position.getX() + Direction.DELTA_X[i], position.getY() + Direction.DELTA_Y[i]);
            obstacle = false; // Se asume que la posicion a revisar no es un obstaculo
            // Ubicacion en frente como se solicita
            if (i == direction) {
                isObstacle = p.seeObstacle();
                obstacle = isObstacle;
            }
            if (!setInfo.containsKey(aroundPosition)) {
                setInfo.put(aroundPosition, new PositionStatus(obstacle));
            }
            // Si verdaderamente hay un obstaculo se debe actualizar esta informacion
            if (obstacle) {
                PositionStatus obstacleLoc = setInfo.get(aroundPosition);
                obstacleLoc.setobstaculo(obstacle);
            }
        }
    }
    /*
    Se actualiza la posicion del agente
     */
    private void updatePosition(MyVector position) {
        this.position = position;
    }

    /*
    Se actualiza el CentralConfig en caso de que exista suciedad o no
     */
    private void updateDirty(MyVector position, boolean dirty) {
        // La posicion puede existir o no en el mapa, en caso de no existir se 
        // añade
        if (!setInfo.containsKey(position)) {
            setInfo.put(position, new PositionStatus(false));
        }

        PositionStatus current = setInfo.get(position);
        current.setDirty(dirty);
    }

    public boolean isLocationDirty(MyVector position) {
        PositionStatus loc = setInfo.get(position);
        if (loc != null) {
            return loc.isDirty();
        } else {
            return true;
        }
    }

    public boolean isLocationObstacle(MyVector position) {
        PositionStatus loc = setInfo.get(position);
        if (loc != null) {
            return loc.isobstaculo();
        } else {
            return false;
        }
    }

    public boolean isLocationExplored(MyVector position) {
        PositionStatus loc = setInfo.get(position);
        if (loc != null) {
            return loc.isanalizado();
        } else {
            return false;
        }
    }

    public boolean isLocationSeen(MyVector position) {
        return setInfo.containsKey(position);
    }

    public static int costoOperacion(MyVector start, MyVector end, int direction) {
        return Costos.costoEstimado(start, end, direction); 
        //Esta Heuristica funciona con precision dado que se compara cuadros
        //adyacentes
    }

    /*
    Devuelve las cuadrillas posibles a ser exploradas
     */
    public LinkedList<MyVector> adyacentes(MyVector position) {
        LinkedList<MyVector> actualadyacentes = new LinkedList<MyVector>();

        // Se a;ade solo si no se sabe que son obstacualos y no esiten en el mapa
        MyVector neighbor;
        for (int i = Direction.NORTH; i <= Direction.WEST; ++i) {
            neighbor = new MyVector(position.getX() + Direction.DELTA_X[i],position.getY() + Direction.DELTA_Y[i]);
            if (setInfo.containsKey(neighbor) && !isLocationObstacle(neighbor)) {
                actualadyacentes.add(neighbor);
            }
        }
        return actualadyacentes;
    }

    /*
    Acutaliza el Internal State dada una accion
     */
    public void update(Action next) {
        if (next instanceof SuckDirt) { 
            updateDirty(position, false);
        } else if (next instanceof GoForward && !isisBump()) { 
            updatePosition(MyVector.add(position,
                    MyVector.directionToVector(direction)));
        } else if (next instanceof TurnLeft) { 
            --direction;
            if (direction < Direction.NORTH) {
                direction = Direction.WEST;
            }
        } else if (next instanceof TurnRight) {
            ++direction;
            if (direction > Direction.WEST) {
                direction = Direction.NORTH;
            }
        } else if (next instanceof ShutOff) {
            shutdown = true;
        }
    }

    public boolean isisObstacle() {
        return isObstacle;
    }

    public boolean isisBump() {
        return isBump;
    }

    public boolean isshutdown() {
        return shutdown;
    }

    public boolean isisDirty() {
        return isDirty;
    }
}
