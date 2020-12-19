package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.data.MoveDirection;
import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.map.IWorldMap;

import java.util.*;

public class SimulationEngine implements IEngine, IDaySubject {
    private final ArrayList<MoveDirection> commands = new ArrayList<>();
    private final ArrayList<Vector2d> positions = new ArrayList<>();
    private List<IDayObserver> observersList;
    private ArrayList<Animal> animals = new ArrayList<>();
    protected IWorldMap map;
    protected int day = 0;
    protected AnimalEngine animalEngine;
    protected Statistics statistics;

//    public SimulationEngine(MoveDirection[] commands, IWorldMap map, Vector2d[] positions, MapDirection[] moveDirections) {
//        this.commands.addAll(Arrays.asList(commands));          // Add animals move commands
//        this.positions.addAll(Arrays.asList(positions));        // Add animals positions to list
//        this.map = map;
//        addAnimalsToMapWithDirections(moveDirections);
//    }

    public SimulationEngine(MoveDirection[] commands, IWorldMap map, int numberOfAnimals, AnimalEngine animalEngine) {
        this.commands.addAll(Arrays.asList(commands));            // Add animals move commands
        this.map = map;

        this.observersList = new ArrayList<>();

        this.statistics = new Statistics(map);
        this.register(this.statistics);

        this.animalEngine = animalEngine;
        this.register(this.animalEngine);

        notifyObservers(day);

        animalEngine.generateAnimals(numberOfAnimals);
    }

//    private void addAnimalsToMapWithDirections(MapDirection[] directions) {
//        for(Vector2d position : this.positions) {           // Create animals
//            animals.add(new Animal(map, position));
//        }
//
//        int i=0;
//        for(Animal animal : animals) {                      // Place animals on the map
//            if(i < directions.length)
//                map.placeWithDirection(animal, directions[i]);
//            else
//                map.place(animal);
//            i++;
//        }
//    }

    @Override
    public void run() {
        int day = 0;

        for(int i=0; i<commands.size(); i++) {

            if(animals.size() == 0){
                System.out.println("Day: " + day + ". All animals died. PRINT STATICTICS");
                return;
            }

            if(i % animals.size() == 0) {               // Show the map after every turn
                System.out.println("Day: " + day);
                day++;
                System.out.println(map.toString(map));
                map.addDailyGrass();
                System.out.println(map.toString(map));
            }

            animals.get(i % animals.size()).move(commands.get(i));              // Make moves for the animals in turn
        }

        System.out.println(map.toString(map));
    }


    public void run(int maxDay) {
        while (day < maxDay){

            if(map.getAnimalsList().isEmpty()){
                System.out.println("All animals died.");
                statistics.printStatistics();
                return;
            }

            // 1. Making movements and immediately reproduce animals or eat grass
            for (Animal animal : map.getAnimalsList().toArray(new Animal[0]))        // Every animal have to move
                animal.moveAccordingGenotype();

            // Sort animal according to life energy - strongest animals make first moves
            animalEngine.sortByEnergy();

            statistics.printStatistics();
            this.day++;
            notifyObservers(day);
            System.out.println(map.toString(map));

            // Placing grass
            map.addDailyGrass();
            System.out.println(map.toString(map));
        }

        System.out.println(map.toString(map));
        System.out.println("Successful, simulation complete.");
        statistics.printStatistics();
    }

    @Override
    public void register(IDayObserver o) {
        observersList.add(o);
    }

    @Override
    public void unregister(IDayObserver o) {
        observersList.remove(o);
    }

    @Override
    public void notifyObservers(int day) {
        for(IDayObserver observer : observersList)
            observer.dayChanged(day);
    }
}