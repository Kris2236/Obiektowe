package agh.cs.worldSimulation;

public class World {
    public static void main(String[] args) {

        try{
            MoveDirection[] directions = new OptionsParser().parse(args);

            AnimalEngine animalEngine = new AnimalEngine(new Jungle(), 100, 10);
            IWorldMap jungleMap = new Jungle(4,2,0,0.5, 25, animalEngine);

            IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, 2, animalEngine);
            engineJungleWorld.run(20);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
