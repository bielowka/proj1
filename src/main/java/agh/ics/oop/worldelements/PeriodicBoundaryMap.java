package agh.ics.oop.worldelements;

import agh.ics.oop.basics.Vector2d;

public class PeriodicBoundaryMap extends AbstractWorldMap{

    public PeriodicBoundaryMap(int width, int height) {
        super(width, height);
    }

    public Vector2d newPosition(Vector2d movePossibility) {
        int x = movePossibility.x;
        int y = movePossibility.y;
        if (x < 0) x += width;
        if (x >= width) x -= width;
        if (y < 0) y += height;
        if (y >= height) y -= height;

        return new Vector2d(x,y);
    }
}
