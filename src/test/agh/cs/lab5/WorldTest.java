package agh.cs.lab5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldTest {
    Vector2d v1;    // poz animal 1
    Vector2d v2;    // poz animal 2
    String[] colisionComands;
    MoveDirection[] inputComans;
    Vector2d[] animalPositions = { new Vector2d(2,2), new Vector2d(3,4) };


    @Test
    public void IntegralWordWithGrassTest(){
        colisionComands = new String[]{"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        inputComans = new OptionsParser().parse(colisionComands);

        GrassField mapWithGrass = new GrassField(5);
        IEngine engineWithGrass = new SimulationEngine(inputComans, mapWithGrass, animalPositions);
        System.out.println(mapWithGrass.toString(mapWithGrass));
        engineWithGrass.run();

        v1 = new Vector2d(2,-1);
        v2 = new Vector2d(3,7);

        assertEquals(mapWithGrass.isOccupied(v1), true);
        assertEquals(mapWithGrass.isOccupied(v2), true);
        assertEquals(mapWithGrass.objectAt(v1).toString(), "v");    // check is there animal in a given position
        assertEquals(mapWithGrass.objectAt(v2).toString(), "^");    // check is there animal in a given position
    }
}

