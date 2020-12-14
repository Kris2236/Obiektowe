package agh.cs.worldSimulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JungleTest {
    Vector2d v = new Vector2d(2,2);
    IWorldMap map = new Jungle(10, 5, 0, 0.1, 25);

    @Test
    void canMoveToTest(){
        v = new Vector2d(3,3);
        assertTrue(map.canMoveTo(v));

        v = new Vector2d(9,4);
        assertTrue(map.canMoveTo(v));

        v = new Vector2d(0,0);
        assertTrue(map.canMoveTo(v));

        v = new Vector2d(-1,-1);
        assertFalse(map.canMoveTo(v));

        v = new Vector2d(-1,5);
        assertFalse(map.canMoveTo(v));

        v = new Vector2d(10,5);
        assertFalse(map.canMoveTo(v));

        v = new Vector2d(10,-1);
        assertFalse(map.canMoveTo(v));
    }

    @Test
    void isOccupiedTest(){
        map.place(new Animal(map, v));
        assertTrue(map.isOccupied(v));

        v = new Vector2d(-1,-1);
        assertFalse(map.isOccupied(v));
    }

    @Test
    void objectAtTest(){
        map.place(new Animal(map, v));
        assertTrue(map.isOccupied(v));
        assertTrue(map.objectAt(v) instanceof Animal);

        v = new Vector2d(-1,-1);
        assertFalse(map.isOccupied(v));
        assertFalse(map.objectAt(v) instanceof Animal);
    }

    @Test
    void JungleNorthWrapTest() {
        Vector2d v1 = new Vector2d(1,4);
        Vector2d v2 = new Vector2d(3,4);
        Vector2d v3 = new Vector2d(5,4);
        Vector2d v4 = new Vector2d(7,4);
        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH };

        String[] commands = new String[]{"f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2, v3, v4 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(1,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(1,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(3,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(3,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(5,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(5,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v3) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(7,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(7,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v4) instanceof Animal);
    }

    @Test
    void JungleSouthWrapTest() {
        Vector2d v1 = new Vector2d(1,0);
        Vector2d v2 = new Vector2d(3,0);
        Vector2d v3 = new Vector2d(5,0);
        Vector2d v4 = new Vector2d(7,0);
        MapDirection[] animalsDirections = new MapDirection[]{MapDirection.SOUTH, MapDirection.SOUTH, MapDirection.SOUTH, MapDirection.SOUTH};

        String[] commands = new String[]{"f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap2 = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = new Vector2d[]{v1, v2, v3, v4};
        IEngine engineJungleWorld2 = new SimulationEngine(directions, jungleMap2, positions, animalsDirections);
        engineJungleWorld2.run();

        assertTrue(jungleMap2.isOccupied(new Vector2d(1,4)));
        assertTrue(jungleMap2.objectAt(new Vector2d(1,4)) instanceof Animal);
        assertFalse(jungleMap2.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap2.isOccupied(new Vector2d(3,4)));
        assertTrue(jungleMap2.objectAt(new Vector2d(3,4)) instanceof Animal);
        assertFalse(jungleMap2.objectAt(v2) instanceof Animal);

        assertTrue(jungleMap2.isOccupied(new Vector2d(5,4)));
        assertTrue(jungleMap2.objectAt(new Vector2d(5,4)) instanceof Animal);
        assertFalse(jungleMap2.objectAt(v3) instanceof Animal);

        assertTrue(jungleMap2.isOccupied(new Vector2d(7,4)));
        assertTrue(jungleMap2.objectAt(new Vector2d(7,4)) instanceof Animal);
        assertFalse(jungleMap2.objectAt(v4) instanceof Animal);
    }

    @Test
    void JungleEastWrapTest() {
        Vector2d v1 = new Vector2d(9,1);
        Vector2d v2 = new Vector2d(9,3);

        MapDirection[] animalsDirections = { MapDirection.EAST, MapDirection.EAST};

        String[] commands = new String[]{"f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(0,3)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,3)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(0,1)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,1)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleWestWrapTest() {
        Vector2d v1 = new Vector2d(0,1);
        Vector2d v2 = new Vector2d(0,3);

        MapDirection[] animalsDirections = { MapDirection.WEST, MapDirection.WEST};

        String[] commands = new String[]{"f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(9,3)));
        assertTrue(jungleMap.objectAt(new Vector2d(9,3)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(9,1)));
        assertTrue(jungleMap.objectAt(new Vector2d(9,1)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleCornerTest() {
        Vector2d cornerRightUpperPosition = new Vector2d(9,4);
        Vector2d cornerLeftUpperPosition = new Vector2d(0,4);
        Vector2d cornerLeftLowerPosition = new Vector2d(0,0);
        Vector2d cornerRightLowerPosition = new Vector2d(9,0);

        String[] commands = new String[]{"f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        MapDirection[] animalsDirections = {MapDirection.NORTH_EAST, MapDirection.SOUTH_EAST};
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { cornerRightUpperPosition, cornerRightLowerPosition };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(cornerLeftLowerPosition));
        assertTrue(jungleMap.objectAt(cornerLeftLowerPosition) instanceof Animal);
        assertFalse(jungleMap.objectAt(cornerRightUpperPosition) instanceof Animal);

        assertTrue(jungleMap.isOccupied(cornerLeftUpperPosition));
        assertTrue(jungleMap.objectAt(cornerLeftUpperPosition) instanceof Animal);
        assertFalse(jungleMap.objectAt(cornerRightLowerPosition) instanceof Animal);


        commands = new String[]{"f", "f"};
        MoveDirection[] directions2 = new OptionsParser().parse(commands);
        MapDirection[] animalsDirections2 = { MapDirection.NORTH_WEST, MapDirection.SOUTH_WEST,};
        IWorldMap jungleMap2 = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions2 = { cornerLeftUpperPosition, cornerLeftLowerPosition };
        IEngine engineJungleWorld2 = new SimulationEngine(directions2, jungleMap2, positions2, animalsDirections2);
        engineJungleWorld2.run();

        assertTrue(jungleMap2.isOccupied(cornerRightLowerPosition));
        assertTrue(jungleMap2.objectAt(cornerRightLowerPosition) instanceof Animal);
        assertFalse(jungleMap2.objectAt(cornerLeftUpperPosition) instanceof Animal);

        assertTrue(jungleMap2.isOccupied(cornerRightUpperPosition));
        assertTrue(jungleMap2.objectAt(cornerRightUpperPosition) instanceof Animal);
        assertFalse(jungleMap2.objectAt(cornerLeftLowerPosition) instanceof Animal);
    }

    @Test
    void JungleNorthEastWrapTest() {
        Vector2d v1 = new Vector2d(1,4);
        Vector2d v2 = new Vector2d(4,4);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"1", "1"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(2,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(2,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(5,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(5,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleNorthWestWrapTest() {
        Vector2d v1 = new Vector2d(1,4);
        Vector2d v2 = new Vector2d(4,4);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"7", "7"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(0,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(3,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(3,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleSouthWestWrapTest() {
        Vector2d v1 = new Vector2d(1,0);
        Vector2d v2 = new Vector2d(4,0);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"5", "5"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(0,4)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,4)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(3,4)));
        assertTrue(jungleMap.objectAt(new Vector2d(3,4)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleSouthEastWrapTest() {
        Vector2d v1 = new Vector2d(1,0);
        Vector2d v2 = new Vector2d(4,0);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"3", "3"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(2,4)));
        assertTrue(jungleMap.objectAt(new Vector2d(2,4)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(5,4)));
        assertTrue(jungleMap.objectAt(new Vector2d(5,4)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleRightBoundTest() {
        Vector2d v1 = new Vector2d(9,3);
        Vector2d v2 = new Vector2d(9,1);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"1", "3"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(0,4)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,4)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(0,0)));
        assertTrue(jungleMap.objectAt(new Vector2d(0,0)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleManyAnimalsInOnePositionTest() {
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(3,1);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"2", "6"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(new Vector2d(2,1)));
        assertTrue(jungleMap.objectAt(new Vector2d(2,1)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(new Vector2d(2,1)));
        assertTrue(jungleMap.objectAt(new Vector2d(2,1)) instanceof Animal);
        assertFalse(jungleMap.objectAt(v2) instanceof Animal);
    }

    @Test
    void JungleAnimalRoundTest() {
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(3,1);

        MapDirection[] animalsDirections = { MapDirection.NORTH, MapDirection.NORTH};

        String[] commands = new String[]{"2", "6", "f", "f", "b", "b", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(commands);
        IWorldMap jungleMap = new Jungle(10,5,0, 0.2, 25);
        Vector2d[] positions = { v1, v2 };
        IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions, animalsDirections);
        engineJungleWorld.run();

        assertTrue(jungleMap.isOccupied(v1));
        assertTrue(jungleMap.objectAt(v1) instanceof Animal);

        assertTrue(jungleMap.isOccupied(v2));
        assertTrue(jungleMap.objectAt(v2) instanceof Animal);
    }
}
