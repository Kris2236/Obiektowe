package agh.cs.lab5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrassFieldTest {
    Vector2d v1 = new Vector2d(2,2);
    GrassField mapWithGrass = new GrassField(10);
    Vector2d animalPosition = v1;

    @Test
    void placeTest(){
        mapWithGrass.place(new Animal(mapWithGrass, animalPosition));
        assertEquals(mapWithGrass.isOccupied(v1), true);

        v1 = new Vector2d(-1,-10);
        Vector2d animalPosition = v1;
        mapWithGrass.place(new Animal(mapWithGrass, animalPosition));
        assertEquals(mapWithGrass.isOccupied(v1), true);
    }

    @Test
    void canMoveToTest(){
        v1 = new Vector2d(-2147483648,-2147483648);
        assertEquals(mapWithGrass.canMoveTo(v1), true);

        v1 = new Vector2d(2147483647,2147483647);
        assertEquals(mapWithGrass.canMoveTo(v1), true);
    }

    @Test
    void isOccupiedTest(){
        mapWithGrass.place(new Animal(mapWithGrass, animalPosition));
        assertEquals(mapWithGrass.isOccupied(v1), true);
    }
}
