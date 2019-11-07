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
     La clase CentralConfig contiene informacion como la posicion del agente,
     su direccion e informacion sobre el mapa global
     */
    private CentralConfig CentralConfig;

    /*
    Organizador ayuda al agente a decidir que accion tomar
     */
    private Organizador Organizador;

    public VacAgent() {
        CentralConfig = new CentralConfig();
        Organizador = new Organizador(CentralConfig);
    }

    @Override
    public void see(Percept p) {
        // Se modifco la funcion para que quede en terminos del Internal State
        // y esta devuelva una accion que es proporcionada por la clase antes
        // mencionada, en base a un estrategia y una heuristica.
        if (!(p instanceof VacPercept)) {
            return;
        }
        //Actuliza el internal state en base al percept
        CentralConfig.update((VacPercept) p);
    }

    @Override
    public Action selectAction() {
        //Organizador es el encargado de ejecutar las acciones
        return Organizador.nextAction();
    }

    @Override
    public String getId() {
        return ID;
    }  
}
