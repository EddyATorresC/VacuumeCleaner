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
public class LocationInformation {
   private boolean obstacle;
    private boolean dirty;
    private boolean explored;

    /*
    Todas las cuadrillas se asumen al inicio como limpias y sin explorar,
    se puede inicializar con un obstaculo
     */
    public LocationInformation(boolean obstacle) {
        this.dirty = false;
        this.explored = false;
        this.obstacle = obstacle;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    } 
}
