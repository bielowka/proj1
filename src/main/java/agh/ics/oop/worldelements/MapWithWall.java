package agh.ics.oop.worldelements;

import agh.ics.oop.basics.Vector2d;

public class MapWithWall extends AbstractWorldMap {
    public MapWithWall(int width, int height) {
        super(width, height);
    }

    public boolean canMoveTo(Vector2d position) {
        if (position.x >= width) return false;
        if (position.y >= height) return false;
        if (position.x < 0) return false;
        if (position.y < 0) return false;
        return true;
    }

}

