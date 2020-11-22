package agh.cs.lab4;


import java.util.ArrayList;
import java.util.Arrays;

public class SimulationEngine implements IEngine{
    private ArrayList<MoveDirection> comands = new ArrayList<>();
    private ArrayList<Vector2d> positions = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private IWorldMap mapCurrentWorld;

    public SimulationEngine(MoveDirection[] comands, IWorldMap map, Vector2d[] positions){
        this.comands.addAll(Arrays.asList(comands));
        this.positions.addAll(Arrays.asList(positions));
        this.mapCurrentWorld = map;

        addAnimalsToMap();
    }

    private void addAnimalsToMap(){
        // Create animals
        for(Vector2d position : this.positions){
            animals.add(new Animal(mapCurrentWorld, position));
        }

        // Push animal in to map
        for(Animal animal : animals){
            animal.direction = MapDirection.NORTH;
            mapCurrentWorld.place(animal);
        }
    }

    @Override
    public void run() {
        for(int i=0; i<comands.size(); i++){
            animals.get(i%animals.size()).move(comands.get(i));
            System.out.println(mapCurrentWorld.toString(mapCurrentWorld));
        }
    }
}