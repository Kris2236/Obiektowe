package agh.cs.lab7;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulationEngine implements IEngine{
    private final ArrayList<MoveDirection> commands = new ArrayList<>();
    private final ArrayList<Vector2d> positions = new ArrayList<>();
    private final ArrayList<Animal> animals = new ArrayList<>();
    protected IWorldMap mapCurrentWorld;

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, Vector2d[] positions){

        // Add animals move commands
        this.commands.addAll(Arrays.asList(commands));

        // Add animals positions to list
        this.positions.addAll(Arrays.asList(positions));

        this.mapCurrentWorld = map;
        addAnimalsToMap();
    }

    private void addAnimalsToMap(){

        // Create animals
        for(Vector2d position : this.positions){
            animals.add(new Animal(mapCurrentWorld, position));
        }

        // Place animals on the map
        for(Animal animal : animals){
            animal.direction = MapDirection.NORTH;
            mapCurrentWorld.place(animal);
        }
    }

    @Override
    public void run() {
        int year = 0;

        for(int i=0; i<commands.size(); i++){

            // Show the map after every turn
            if(i % animals.size() == 0) {
                System.out.println("Year: " + year);
                year++;
                System.out.println(mapCurrentWorld.toString(mapCurrentWorld));
                // After every turn add 2 grass to map - notify map
            }

            // Make moves for the animals in turn
            animals.get(i % animals.size()).move(commands.get(i));
        }
        System.out.println(mapCurrentWorld.toString(mapCurrentWorld));

    }
}