package agh.cs.worldSimulation;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulationEngine implements IEngine{
    private final ArrayList<MoveDirection> commands = new ArrayList<>();
    private final ArrayList<Vector2d> positions = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    protected IWorldMap map;

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, Vector2d[] positions) {

        // Add animals move commands
        this.commands.addAll(Arrays.asList(commands));

        // Add animals positions to list
        this.positions.addAll(Arrays.asList(positions));

        this.map = map;
        addAnimalsToMap();
    }

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, int numberOfAnimals) {

        // Add animals move commands
        this.commands.addAll(Arrays.asList(commands));
        this.map = map;
        animals = map.generateAnimals(numberOfAnimals);

    }

    private void addAnimalsToMap() {

        // Create animals
        for(Vector2d position : this.positions) {
            animals.add(new Animal(map, position));
        }

        // Place animals on the map
        for(Animal animal : animals) {
            //animal.direction = MapDirection.NORTH;
            map.place(animal);
        }
    }

    @Override
    public void run() {
        int day = 0;

        for(int i=0; i<commands.size(); i++) {

            // Show the map after every turn
            if(i % animals.size() == 0) {
                System.out.println("Day: " + day);
                day++;
                System.out.println(map.toString(map));
                map.placeGrass(2);
                System.out.println(map.toString(map));
            }

            // Make moves for the animals in turn
            animals.get(i % animals.size()).move(commands.get(i));
        }
        System.out.println(map.toString(map));

    }
}
