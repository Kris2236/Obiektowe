package agh.cs.lab5;

public class World {
    public static void main(String[] args) {

        agh.cs.lab5.MoveDirection[] directions = new agh.cs.lab5.OptionsParser().parse(args);
        agh.cs.lab5.Vector2d[] positions = { new agh.cs.lab5.Vector2d(2,2), new agh.cs.lab5.Vector2d(3,4) };

//        IWorldMap map = new RectangularMap(10, 5);
//        IEngine engine = new SimulationEngine(directions, map, positions);
//        System.out.println(map.toString(map));
//        engine.run();

        agh.cs.lab5.GrassField mapWithGrass = new agh.cs.lab5.GrassField(5);
        agh.cs.lab5.IEngine engineWithGrass = new agh.cs.lab5.SimulationEngine(directions, mapWithGrass, positions);
        System.out.println(mapWithGrass.toString(mapWithGrass));
        engineWithGrass.run();
    }
}
