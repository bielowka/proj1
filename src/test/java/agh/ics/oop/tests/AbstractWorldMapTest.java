package agh.ics.oop.tests;

import agh.ics.oop.basics.Vector2d;
import agh.ics.oop.worldelements.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

class AbstractWorldMapTest {

    @Test
    public void test1(){
        IWorldMap map = new MapWithWall(10,10);
        IWorldMap map2 = new PeriodicBoundaryMap(10,10);

        Animal a1 = new Animal(new Vector2d(3, 3), 50, map, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal a2 = new Animal(new Vector2d(3, 3), 50, map, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal a3 = new Animal(new Vector2d(9, 9), 50, map2, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));


        map.place(a1);
        map.place(a2);
        map2.place(a3);

        map.addGrass(new Vector2d(5,5));
        map.addGrass(new Vector2d(3,3));

        assertTrue(a1.isAt(new Vector2d(3,3)));
        assertTrue(a2.isAt(new Vector2d(3,3)));
        assertTrue(a3.isAt(new Vector2d(9,9)));

        assertTrue(map.objectAt(new Vector2d(3,3)) instanceof Animal);
        assertTrue(map.objectAt(new Vector2d(5,5)) instanceof Grass);
        assertTrue(map.objectAt(new Vector2d(7,7)) == null);

        assertTrue(map.isOccupied(new Vector2d(3,3)));
        assertTrue(map.isOccupied(new Vector2d(5,5)));
        assertTrue(!map.isOccupied(new Vector2d(7,7)));
        assertTrue(map2.isOccupied(new Vector2d(9,9)));

//        System.out.println(a1.getDirection());
//        System.out.println(a2.getDirection());
//
//        a1.move(MoveDirection.FORWARD);
//        a1.positionChanged(new Vector2d(3,3),a1.getPosition());
//
//        a2.move(MoveDirection.FORWARD);
//        a2.positionChanged(new Vector2d(3,3),a2.getPosition());
//
//        Vector2d g = a1.getPosition();
//        a1.move(MoveDirection.FORWARD);
//        a1.positionChanged(g,a1.getPosition());
//
//        System.out.println(map);
//        System.out.println(map.getAnimals());

        IWorldMap map3 = new PeriodicBoundaryMap(10,10);
        Animal b1 = new Animal(new Vector2d(3, 3), 150, map3, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal b2 = new Animal(new Vector2d(3, 3), 50, map3, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal b3 = new Animal(new Vector2d(3, 3), 70, map3, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));

        map3.place(b1);
        map3.place(b2);
        map3.place(b3);

        System.out.println(map3.getAnimals().get(new Vector2d(3,3)));
        System.out.println(map3.objectAt(new Vector2d(3, 3)));
        System.out.println(map3.getAnimals().get(new Vector2d(3,3)));


    }

}