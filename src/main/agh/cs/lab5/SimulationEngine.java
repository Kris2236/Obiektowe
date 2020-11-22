package agh.cs.lab5;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulationEngine implements IEngine{
    private final ArrayList<agh.cs.lab5.MoveDirection> comands = new ArrayList<>();
    private final ArrayList<Vector2d> positions = new ArrayList<>();
    private final ArrayList<Animal> animals = new ArrayList<>();
    protected IWorldMap mapCurrentWorld;

    public SimulationEngine(agh.cs.lab5.MoveDirection[] comands, IWorldMap map, Vector2d[] positions){
        // Add animals move comands
        this.comands.addAll(Arrays.asList(comands));

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
        for(int i=0; i<comands.size(); i++){
            // make moves for the animals in turn
            animals.get(i%animals.size()).move(comands.get(i));
            System.out.println(mapCurrentWorld.toString(mapCurrentWorld));
        }
    }
}
