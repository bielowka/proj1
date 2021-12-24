package agh.ics.oop.worldelements;

import agh.ics.oop.basics.Vector2d;

import java.util.ArrayList;
import java.util.Map;

public interface IWorldMap {

    boolean canMoveTo(Vector2d position);

    Vector2d newPosition(Vector2d movePossibility);

    boolean place(Animal animal);

    boolean delete(Animal animal);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

    Map<Vector2d, ArrayList<Animal>> getAnimals();
    Map<Vector2d, Grass> getGrasses();
    int getHeight();
    int getWidth();

    void positionChanged(Animal animal,Vector2d prev, Vector2d next);

    void addGrass(Vector2d position);

    void removeGrass(Vector2d position);

    int getNumOfGrasses();


}