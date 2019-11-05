/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myagent;

import vacworld.Direction;

/**
 *
 * @author eddy
 */
public class MyVector {
    /*
     Vetores basados en la clase Direction
     */
    public static final MyVector NORTH = new MyVector(0, -1);
    public static final MyVector EAST = new MyVector(1, 0);
    public static final MyVector SOUTH = new MyVector(0, 1);
    public static final MyVector WEST = new MyVector(-1, 0);

    private final int x;
    private final int y;

    /*
    Inicializa el vetor a (0,0)
     */
    public MyVector() {
        this.x = 0;
        this.y = 0;
    }

    /*
    Inicializa el vector a valores x,y 
     */
    public MyVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof MyVector))
            return false;
        MyVector newO = (MyVector) o;
        return this.x == newO.getX() && this.y == newO.getY();
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    @Override
    public String toString() {
        return String.format("MyVector: (%d, %d)", x, y);
    }

    /*
    Convierte una direccion a su vector asociado
     */
    public static MyVector directionToVector(int direction) {
        if (direction == Direction.NORTH) {
            return NORTH;
        } else if (direction == Direction.WEST) {
            return WEST;
        } else if (direction == Direction.SOUTH) {
            return SOUTH;
        } else if (direction == Direction.EAST) {
            return EAST;
        } else {
            return null;
        }
    }

    /*
    Resta de vectores
     */
    public static MyVector sub(MyVector a, MyVector b) {
        return new MyVector(a.x - b.x, a.y - b.y);
    }

    /*
    Producto punto
     */
    public static int dot(MyVector a, MyVector b) {
        return a.x * b.x + a.y * b.y;
    }

    /*
    Calcula la magnitud de un vector
     */
    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    /*
    Calcula el angulo entre dos vectores
     */
    public static double angle(MyVector a, MyVector b) {
        double dot = MyVector.dot(a, b);
        double mag = a.mag() * b.mag();
        return Math.acos(dot / mag);
    }

    /*
    Convierte un vector a su direccion apropiada
     */
    public static int vectorToDirection(MyVector direction) {
        if (direction.equals(NORTH)) {
            return Direction.NORTH;
        } else if (direction.equals(WEST)) {
            return Direction.WEST;
        } else if (direction.equals(SOUTH)) {
            return Direction.SOUTH;
        } else if (direction.equals(EAST)) {
            return Direction.EAST;
        } else {
            return -1;
        }
    }

    /*
    Suma de vectores
     */
    public static MyVector add(MyVector a, MyVector b) {
        return new MyVector(a.x + b.x, a.y + b.y);
    }
}
