/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

import agent.Action;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;

/**
 *
 * @author eddy
 */
public class Planner {
    
    InternalState initialState;

    LinkedList<Action> plan;

    public Planner(InternalState initialState) {
        this.initialState = initialState;
        this.plan = new LinkedList<Action>();
    }

    /*
    Decide que accion debe tomar el agente dado su internal state y devuelve una
    accion
     */
    public Action nextAction() {
        if (initialState.isTurnedOff()) {
            return null;
        }

        // Si un obstaculo o basura se ve en el camino el agente debe cambiar el
        // plan a seguir
        if (initialState.isObstacleSeen() || initialState.isDirtSeen()
                || initialState.isFeltBump()) {
            plan.clear();
        }

        // Si no hay plan se crea uno
        if (plan.isEmpty()) {
            buildPlan();
        }

        // Se toma la accion del plan y se ejecuta
        Action next = plan.remove();

        // Se actualiza de acuerdo al plan a seguir
        initialState.update(next);

        return next;
    }

    /*
    Construye una cola de acciones que el agente tomará
     */
    private void buildPlan() {
        final MyVector position = initialState.getAgentPosition();
        if (initialState.isLocationDirty(position)) { 
            //Si una ubiacion esta sucia el agente solo succionara
            plan.add(new SuckDirt());
        } else {
            buildMovementPlan();
        }

        //Si no hay plan se apaga
        if (plan.isEmpty()) {
            plan.add(new ShutOff());
        }
    }

    /*
    Esto construye un plan de secuencia de movimientos. 
    Se enfoca en:
     1) Encontrar una posicion sin explorar para explorarla
     2) Encontrar el major camino para llegar a ella
     */
    private void buildMovementPlan() {
        // Busca una posicion para explorar en base al menor costo de heuristica
        final MyVector unexplored = findUnexploredPosition();

        // Si se visito todas las ubicaciones simplemente se devuelve void
        if (unexplored == null) {
            return;
        }

        // Se utiliza busqueda por A* para encotrar el mejor camino a la cuadrilla
        //no explorada
        LinkedList<MyVector> path = findPath(unexplored);

        // Removemos la primera ubicacion que es la actual
        if (!path.isEmpty()) {
            path.remove();
        }

        MyVector current = initialState.getAgentPosition();
        MyVector next;
        int currentDirection = initialState.getAgentDirection();
        int nextDirection;

        // Itera sobre cada ubicacion en el path para construir la correcta secuencia
        // de acciones en el plan.
        while (!path.isEmpty()) {
            next = path.remove();

            //Toma la direccion correcta entre dos cuadrillas
            nextDirection = MyVector.vectorToDirection(MyVector
                    .sub(next, current));

            if (nextDirection != currentDirection) { // solo se gira en direcciones diferentes

                int diff = nextDirection - currentDirection;
                if (diff > 3) { //se arregla las direcciones negativas
                    diff = diff - 4;
                } else if (diff < 0) {
                    diff = diff + 4;
                }
                if (diff == 1) {
                    plan.add(new TurnRight());
                } else if (diff == 2) {
                    plan.add(new TurnRight());
                    plan.add(new TurnRight());
                } else if (diff == 3) { // tres giros a la derecha es uno a la izquierda
                    plan.add(new TurnLeft());
                }
            }

            plan.add(new GoForward());

            // se mueve a la siguiente direccion en el path
            current = next;
            currentDirection = nextDirection;
        }
    }

    /*
    Se busca la posicion cerca sin explorar a la cual tenga un costo bajo
     */
    private MyVector findUnexploredPosition() {
       
        final HashMap<MyVector, LocationInformation> map = initialState.getWorldMap();
        Entry<MyVector, LocationInformation> pair;

        int lowestCost = Integer.MAX_VALUE; 
        int cost;
        MyVector lowestCostPosition = null;

        // Itera sobre el mapa para buscar la menor heuristica
        Iterator<Entry<MyVector, LocationInformation>> it = map.entrySet()
                .iterator();
        MyVector pos;

        while (it.hasNext()) {
            pair = it.next();
            pos = pair.getKey();
            if (!initialState.isLocationExplored(pos) && !initialState.isLocationObstacle(pos)) {
                cost = Heuristics.estimateCost(initialState.getAgentPosition(), pos,
                        initialState.getAgentDirection());

                if (cost < lowestCost) {
                    lowestCost = cost;
                    lowestCostPosition = pair.getKey();
                }
            }

        }

        return lowestCostPosition;
    }

    /*Mediante el algoritmo A* de busqueda se encuentra el camino mas corto entre
      la posicion actual y la posicion objetivo
     */
    private LinkedList<MyVector> findPath(MyVector goal) {
        
        MyVector start = initialState.getAgentPosition();
        int currentDirection = initialState.getAgentDirection();

        
        int tentativeG;

        // Nos permite construir el path optimo
        HashMap<MyVector, MyVector> cameFrom = new HashMap<MyVector, MyVector>();

        final HashMap<MyVector, Integer> g = new HashMap<MyVector, Integer>();
        final HashMap<MyVector, Integer> f = new HashMap<MyVector, Integer>();

        // Se inicializa los valores de f y g a 0
        HashMap<MyVector, LocationInformation> worldMap = initialState.getWorldMap();
        Iterator<Entry<MyVector, LocationInformation>> it = worldMap.entrySet()
                .iterator();
        while (it.hasNext()) {
            MyVector pos = it.next().getKey();
            f.put(pos, 0);
            g.put(pos, 0);
        }

        // Conjuntos abierto y cerrados, se utiliza una cola de prioridad para 
        //los valores de f
        PriorityQueue<MyVector> open = new PriorityQueue<MyVector>(11,
                new Comparator<MyVector>() { // Asegura que la cola se ordene por los valores menores de f
                    public int compare(MyVector a, MyVector b) {
                        return f.get(a) - f.get(b);
                    }
                });

        Set<MyVector> closed = new HashSet<MyVector>();
        g.put(start, 0);
        f.put(start,
                g.get(start)
                        + Heuristics
                                .estimateCost(start, goal, currentDirection));

        LinkedList<MyVector> neighbors;
        MyVector neighbor;
        MyVector current;

        open.add(start);

        while (!open.isEmpty()) {
            current = open.remove(); 

            // Si se llega al goal se construye el path
            if (current.equals(goal)) {
                return constructPath(cameFrom, goal);
            }

            // Se guarda current en el conjunto cerrado
            open.remove(current);
            closed.add(current);

            // Se revisa los adyacentes de current
            neighbors = initialState.neighbors(current);
            Iterator<MyVector> it2 = neighbors.iterator();
            while (it2.hasNext()) {
                neighbor = it2.next();
                
                currentDirection = MyVector.vectorToDirection(MyVector.sub(
                        neighbor, current));

                tentativeG = g.get(current)
                        + InternalState.adjacentCost(current, neighbor,
                                currentDirection);
                if (closed.contains(neighbor) && tentativeG >= g.get(neighbor)) {
                    continue;
                }

                if (g.get(neighbor) == 0 || tentativeG < g.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    g.put(neighbor, tentativeG);

                    f.put(neighbor,
                            g.get(neighbor)
                                    + Heuristics.estimateCost(neighbor, goal,
                                            currentDirection));
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }

        }

        // Si no se encuentra un camino se devuelve null
        return null;

    }

    /*
    Se construye recursivamente el path mediante el algoritmo A*
     */
    private static LinkedList<MyVector> constructPath(
            HashMap<MyVector, MyVector> cameFrom, MyVector current) {
        LinkedList<MyVector> p;
        if (cameFrom.containsKey(current)) { 
            p = constructPath(cameFrom, cameFrom.get(current));
            p.add(current);
            return p;
        } else {
            p = new LinkedList<MyVector>();
            p.add(current);
            return p;
        }
    }
}
