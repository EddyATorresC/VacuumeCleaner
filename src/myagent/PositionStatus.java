/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

/**
 *
 * @author eddy
 */
public class PositionStatus {
   
    private boolean dirty;
    private boolean analizado;
    private boolean obstaculo;

    /*
    Todas las cuadrillas se asumen al inicio como limpias y sin explorar,
    se puede inicializar con un obstaculo
     */
    public PositionStatus(boolean obstaculo) {
        this.dirty = false;
        this.analizado = false;
        this.obstaculo = obstaculo;
    }

    public boolean isobstaculo() {
        return obstaculo;
    }

    public boolean isanalizado() {
        return analizado;
    }

    public void setanalizado(boolean analizado) {
        this.analizado = analizado;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setobstaculo(boolean obstaculo) {
        this.obstaculo = obstaculo;
    } 
}
