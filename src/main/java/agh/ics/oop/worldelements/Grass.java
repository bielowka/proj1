package agh.ics.oop.worldelements;

import agh.ics.oop.basics.Vector2d;

public class Grass implements IMapElement{
    IWorldMap map;
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }


    public Vector2d getPosition() {
        return position;
    }

    public String Visualize() {
        return "src/main/resources/grass.png";
    }

    @Override
    public String toString() {
        return "*";
    }
}
