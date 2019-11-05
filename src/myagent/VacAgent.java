/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

import agent.Action;
import agent.Agent;
import agent.Percept;
import vacworld.VacPercept;

/**
 *
 * @author eddy
 */
public class VacAgent extends Agent{
    private final String ID = "1";
    /*
     La clase InternalState contiene informacion como la posicion del agente,
     su direccion e informacion sobre el mapa global
     */
    private InternalState internalState;

    /*
    Planner ayuda al agente a decidir que accion tomar
     */
    private Planner planner;

    public VacAgent() {
        internalState = new InternalState();
        planner = new Planner(internalState);
    }

    @Override
    public void see(Percept p) {
        // Se modifco la funcion para que quede en terminos del Internal State
        // y esta devuelva una accion que es proporcionada por la clase antes
        // mencionada, en base a un plan y una heuristica.
        if (!(p instanceof VacPercept)) {
            return;
        }
        //Actuliza el internal state en base al percept
        internalState.update((VacPercept) p);
    }

    @Override
    public Action selectAction() {
        //Planner es el encargado de ejecutar las acciones
        return planner.nextAction();
    }

    @Override
    public String getId() {
        return ID;
    }  
}
