package agh.cs.worldSimulation;

public class World {
    public static void main(String[] args) {

        try{
            MoveDirection[] directions = new OptionsParser().parse(args);
            Vector2d[] positions = { new Vector2d(1,1), new Vector2d(2,2),new Vector2d(3,3),new Vector2d(4,4),new Vector2d(5,5) };
            Jungle jungleMap = new Jungle(10,7,15);
            IEngine engineWithGrass = new SimulationEngine(directions, jungleMap, positions);
            engineWithGrass.run();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }
}
