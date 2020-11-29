package agh.cs.lab7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTest {
    Vector2d v = new Vector2d(2,2);
    IWorldMap map = new RectangularMap(10, 5);

    @Test
    public void placeExceptionTest() {
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);
        v = new Vector2d(-100,-1010);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            map.place(new Animal(map, v));
        });

        String expectedMessage = "(-100,-1010) is not legal. Off the map or position is occupated.\n";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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

    @Test
    void objectAtTest(){
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);

        assertEquals(map.objectAt(v) instanceof Animal, true);
    }

    @Test
    void WorldRectangularMapTest(){
        String[] colisionComands = new String[]{"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(colisionComands);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
        IEngine engine = new SimulationEngine(directions, map, positions);
        System.out.println(map.toString(map));
        engine.run();
    }
}
