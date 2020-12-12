package agh.cs.worldSimulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JungleTest {
    Vector2d v = new Vector2d(2,2);
    IWorldMap map = new Jungle(10, 5, 0, 0.1);

    @Test
    void canMoveToTest(){
        v = new Vector2d(3,3);
        assertEquals(map.canMoveTo(v), true);

        v = new Vector2d(9,4);
        assertEquals(map.canMoveTo(v), true);

        v = new Vector2d(0,0);
        assertEquals(map.canMoveTo(v), true);

        v = new Vector2d(-1,-1);
        assertEquals(map.canMoveTo(v), false);

        v = new Vector2d(-1,5);
        assertEquals(map.canMoveTo(v), false);

        v = new Vector2d(10,5);
        assertEquals(map.canMoveTo(v), false);

        v = new Vector2d(10,-1);
        assertEquals(map.canMoveTo(v), false);
    }

    @Test
    void isOccupiedTest(){
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);

        v = new Vector2d(-1,-1);
        assertEquals(map.isOccupied(v), false);
    }

    @Test
    void objectAtTest(){
        map.place(new Animal(map, v));
        assertEquals(map.isOccupied(v), true);
        assertEquals(map.objectAt(v) instanceof Animal, true);

        v = new Vector2d(-1,-1);
        assertEquals(map.isOccupied(v), false);
        assertEquals(map.objectAt(v) instanceof Animal, false);
    }

    @Test
    void WorldJungleIntegralTest() {
        Vector2d cornerRightUpperPosition = new Vector2d(9,4);
        Vector2d cornerLeftUpperPosition = new Vector2d(0,4);
        Vector2d cornerLeftLowerPosition = new Vector2d(0,0);
        Vector2d cornerRightLowerPosition = new Vector2d(9,0);

        String[] comands = new String[]{"1", "3"};
        MoveDirection[] directions = new OptionsParser().parse(comands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2);
        Vector2d[] positions = { cornerRightUpperPosition, cornerRightLowerPosition };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions);
        engineJungleWorld.run();

        assertEquals(jungleMap.isOccupied(cornerLeftLowerPosition), true);
        assertEquals(jungleMap.objectAt(cornerLeftLowerPosition) instanceof Animal, true);
        assertEquals(jungleMap.objectAt(cornerRightUpperPosition) instanceof Animal, false);

        assertEquals(jungleMap.isOccupied(cornerLeftUpperPosition), true);
        assertEquals(jungleMap.objectAt(cornerLeftUpperPosition) instanceof Animal, true);
        assertEquals(jungleMap.objectAt(cornerRightLowerPosition) instanceof Animal, false);


        comands = new String[]{"5", "7"};
        MoveDirection[] directions2 = new OptionsParser().parse(comands);
        IWorldMap jungleMap2 = new Jungle(10,5,0, 0.2);
        Vector2d[] positions2 = { cornerLeftUpperPosition, cornerLeftLowerPosition };
        IEngine engineJungleWorld2 = new SimulationEngine(directions2, jungleMap2, positions2);
        engineJungleWorld2.run();

        assertEquals(jungleMap2.isOccupied(cornerRightLowerPosition), true);
        assertEquals(jungleMap2.objectAt(cornerRightLowerPosition) instanceof Animal, true);
        assertEquals(jungleMap2.objectAt(cornerLeftUpperPosition) instanceof Animal, false);

        assertEquals(jungleMap2.isOccupied(cornerRightUpperPosition), true);
        assertEquals(jungleMap2.objectAt(cornerRightUpperPosition) instanceof Animal, true);
        assertEquals(jungleMap2.objectAt(cornerLeftLowerPosition) instanceof Animal, false);
    }
}
