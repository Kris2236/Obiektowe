package agh.cs.worldSimulation;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulationEngine implements IEngine{
    private final ArrayList<MoveDirection> commands = new ArrayList<>();
    private final ArrayList<Vector2d> positions = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    protected IWorldMap map;

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, Vector2d[] positions) {
        this.commands.addAll(Arrays.asList(commands));          // Add animals move commands
        this.positions.addAll(Arrays.asList(positions));            // Add animals positions to list
        this.map = map;
        addAnimalsToMap();
    }

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, int numberOfAnimals) {
        this.commands.addAll(Arrays.asList(commands));          // Add animals move commands
        this.map = map;
        animals = map.generateAnimals(numberOfAnimals);
    }

    private void addAnimalsToMap() {
        for(Vector2d position : this.positions) {           // Create animals
            animals.add(new Animal(map, position));
        }

        for(Animal animal : animals) {          // Place animals on the map
            map.place(animal);
        }
    }

    @Override
    public void run() {
        int day = 0;

        for(int i=0; i<commands.size(); i++) {

            if(i % animals.size() == 0) {               // Show the map after every turn
                System.out.println("Day: " + day);
                day++;
                System.out.println(map.toString(map));
                map.placeGrass(2);
                System.out.println(map.toString(map));
            }

            animals.get(i % animals.size()).move(commands.get(i));              // Make moves for the animals in turn
        }

        System.out.println(map.toString(map));
    }
}
