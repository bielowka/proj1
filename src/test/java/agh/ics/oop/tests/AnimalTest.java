package agh.ics.oop.tests;

import agh.ics.oop.basics.MapDirection;
import agh.ics.oop.basics.MoveDirection;
import agh.ics.oop.basics.Vector2d;
import agh.ics.oop.worldelements.Animal;
import agh.ics.oop.worldelements.IWorldMap;
import agh.ics.oop.worldelements.MapWithWall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

class AnimalTest {

    @Test
    public void test1() {
        IWorldMap map = new MapWithWall(20,20);

        Animal dog = new Animal(new Vector2d(2, 3), 150, map, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
        Animal cat = new Animal(new Vector2d(4, 7), 50, map, new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));

        Animal catdog = new Animal(dog.getPosition(), dog, cat, map,0);

        assertTrue(
                catdog.getGenome().equals(new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1)))
        );
    }

    @Test
    public void test2() {
        IWorldMap map1 = new MapWithWall(20,20);

        Animal dog1 = new Animal(new Vector2d(2, 3), 50, map1, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal cat1 = new Animal(new Vector2d(4, 7), 50, map1, new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));

        Animal catdog1 = new Animal(dog1.getPosition(), dog1, cat1, map1,0);

        assertEquals(24, catdog1.getEnergy());
    }

    @Test
    public void test3(){
        IWorldMap map2 = new MapWithWall(20,20);
        Animal dog2 = new Animal(new Vector2d(2, 3), 50, map2, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        MapDirection t1 = dog2.getDirection();
        dog2.move(MoveDirection.BACKWARD);
        MapDirection t2 = dog2.getDirection();
        assertEquals(t1,t1);

    }

    @Test
    public void test4(){
        IWorldMap map3 = new MapWithWall(20,20);
        Animal dog3 = new Animal(new Vector2d(2, 3), 50, map3, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        Animal cat3 = new Animal(new Vector2d(2, 3), 50, map3, new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,1,1,2,2,2,2,2,2,3,3,4,4,4,4,4,4,5,5,6,6,7,7,7,7)));
        assertNotSame(dog3,cat3);

    }

}