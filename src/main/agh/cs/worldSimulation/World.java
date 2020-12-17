package agh.cs.worldSimulation;

public class World {
    public static void main(String[] args) {

        try{
            MoveDirection[] directions = new OptionsParser().parse(args);
            IWorldMap jungleMap = new Jungle(4,2,0,0.5, 25);
            IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, 2, 100, 10);
            engineJungleWorld.run(20);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
