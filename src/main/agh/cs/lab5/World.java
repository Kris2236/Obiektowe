package agh.cs.lab5;

public class World {
    public static void main(String[] args) {

        MoveDirection[] directions = new OptionsParser().parse(args);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };

        GrassField mapWithGrass = new GrassField(5);
        IEngine engineWithGrass = new SimulationEngine(directions, mapWithGrass, positions);
        System.out.println(mapWithGrass.toString(mapWithGrass));
        engineWithGrass.run();
    }
}
