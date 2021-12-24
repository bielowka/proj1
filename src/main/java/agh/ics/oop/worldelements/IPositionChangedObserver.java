package agh.ics.oop.worldelements;

import agh.ics.oop.basics.Vector2d;

public interface IPositionChangedObserver {
    void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition);
}
