package agh.cs.lab4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldTest {
    Vector2d v1;    // poz 1 animal
    Vector2d v2;    // poz 2 animal
    String[] colisionComands;
    MoveDirection[] inputComans;
    Vector2d[] animalPositions = { new Vector2d(2,2), new Vector2d(3,4) };

    @Test
    public void IntegralWordTest(){
        colisionComands = new String[]{"f", "b", "r", "l", "f", "f"};
        inputComans = new OptionsParser().parse(colisionComands);
        IWorldMap map = new RectangularMap(10, 5);
        IEngine engine = new SimulationEngine(inputComans, map, animalPositions);
        System.out.println(map.toString(map));
        engine.run();

        v1 = new Vector2d(2,3);
        v2 = new Vector2d(3,3);
        assertEquals(map.isOccupied(v1), true);
        assertEquals(map.isOccupied(v2), true);
    }

    @Test
    public void IntegralWordTest2(){        // nie mogę sworzyć 2 map
        colisionComands = new String[]{"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        inputComans = new OptionsParser().parse(colisionComands);
        IWorldMap map = new RectangularMap(10, 5);
        IEngine engine = new SimulationEngine(inputComans, map, animalPositions);
        System.out.println(map.toString(map));
        engine.run();

        v1 = new Vector2d(2,3);
        v2 = new Vector2d(3,3);
        assertEquals(map.isOccupied(v1), false);
        assertEquals(map.isOccupied(v2), false);
    }
}

