package agh.cs.worldSimulation;

public class World {
    public static void main(String[] args) {

        try{
            MoveDirection[] directions = new OptionsParser().parse(args);
            Vector2d[] positions = { new Vector2d(1,1), new Vector2d(2,4),new Vector2d(3,4)};
            IWorldMap jungleMap = new Jungle(10,5,0,0.5);
            //IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, positions);
            IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, 2);
            engineJungleWorld.run();

            System.out.println("===========================");
            engineJungleWorld.run(10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
