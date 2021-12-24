package agh.ics.oop.basics;


import java.util.Objects;

public class Vector2d {
    final public int x;
    final public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x,this.y);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other){
        if (x <= other.x && y<= other.y) {return true;}
        return false;
    }
    public boolean follows(Vector2d other){
        if (x >= other.x && y >= other.y) {return true;}
        return false;
    }

    public Vector2d upperRight(Vector2d other){
        Vector2d result = new Vector2d(Math.max(x,other.x),Math.max(y,other.y));
        return result;
    }

    public Vector2d lowerLeft(Vector2d other){
        Vector2d result = new Vector2d(Math.min(x,other.x),Math.min(y,other.y));
        return result;
    }

    public Vector2d add(Vector2d other){
        Vector2d result = new Vector2d(x + other.x , y + other.y);
        return result;
    }

    public Vector2d substract(Vector2d other){
        Vector2d result = new Vector2d(x - other.x , y - other.y);
        return result;
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y) return true;
        return false;
    }

    public Vector2d opposite(){
        Vector2d result = new Vector2d(x*(-1), y*(-1));
        return result;
    }








}
