package agh.cs.lab5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RectangularMapTest {
    Vector2d v = new Vector2d(2,2);
    IWorldMap map = new RectangularMap(10, 5);

    @Test
    void placeTest(){
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);

        v = new Vector2d(-100,-1010);
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), false);
    }

    @Test
    void canMoveToTest(){
        v = new Vector2d(3,3);
        assertEquals(map.canMoveTo(v), true);

        v = new Vector2d(-1,-1);
        assertEquals(map.canMoveTo(v), false);
    }

    @Test
    void isOccupiedTest(){
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);
    }

    // create new class with animals and grass
//    @Test
//    void objectAtTest(){
//        map.place(new Animal(map, v));
//        assertEquals(map.isOccupied(v), true);
//
//        assertEquals(map.objectAt(v).equals(v), true);
//    }
}
