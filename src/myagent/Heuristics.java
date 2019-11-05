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
public class Heuristics {
    
    public static int estimateCost(MyVector inicio, MyVector llegada, int direccion) {
        int x = inicio.getX() - llegada.getX();
        int y = inicio.getY() - llegada.getY();
        
        //Calcula la distancia Manhattan entre dos puntos.
        int moveCost = (Math.abs(x) + Math.abs(y));
        
        /* Estima los costos en términos del giro, 
        es decir cuanto debe girar la aspiradora para direccionarse a una 
        nueva ubicación.
        inicio: es la posición inicial.
        llegada: es la posición final.
        direccion : la direccion frente a la aspiradora.
          */
        int turnCost;
        
        MyVector currentDirectionVector = MyVector.directionToVector(direccion);
        MyVector newDirectionVector = MyVector.sub(llegada, inicio);

        double angle = MyVector.angle(currentDirectionVector, newDirectionVector);

        if (angle == 0.0) {
            turnCost = 0;
        } else if (angle <= Math.PI / 2) {
            turnCost = 1;
        } else if (angle > Math.PI / 2) {
            turnCost = 2;
        } else { 
            turnCost = 0;
        }
        
        return moveCost + turnCost;
    }
}
