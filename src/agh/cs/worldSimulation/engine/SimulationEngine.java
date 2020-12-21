package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.App.CanDisplay;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.map.IWorldMap;

import java.util.*;

public class SimulationEngine implements IEngine, IDaySubject {
    private final List<IDayObserver> observersList;
    private final IWorldMap map;
    private AnimalEngine animalEngine;
    private final Statistics statistics;
    private final CanDisplay canDisplay;
    private int day = 0;


    public SimulationEngine(IWorldMap map, CanDisplay canDisplay) {
        this.map = map;
        this.observersList = new ArrayList<>();
        this.statistics = new Statistics(map);
        this.canDisplay = canDisplay;
    }

    @Override
    public void setAnimalEngine( AnimalEngine animalEngine, int initialNumberOfAnimals) {
        this.animalEngine = animalEngine;
        this.register(this.animalEngine);
        this.register(this.statistics);
        animalEngine.generateAnimals(initialNumberOfAnimals);
        notifyObservers(day);
    }

    public void run(int maxDay) throws InterruptedException {

        if (canDisplay == null)
            throw new IllegalArgumentException("canDisplay is null. App can not synchronize threads");

        while (day < maxDay) {

            if (map.getAnimalsList().isEmpty()) {
                System.out.println("All animals died.");
                statistics.printStatistics();

                ThreadSynchronize();
                canDisplay.setEndSimulation(true);
                return;
            }

            // 1. Making movements and immediately reproduce animals or eat grass
            for (Animal animal : map.getAnimalsList().toArray(new Animal[0])) {
                animal.moveAccordingGenotype();     // Every animal have to move
                //ThreadSynchronize();              // will show every move in app
            }

            animalEngine.sortByEnergy();            // Sort animal according to life energy - strongest animals make first moves
            this.day++;
            notifyObservers(day);
            statistics.printStatistics();

            map.addDailyGrass();                    // Placing grass
            ThreadSynchronize();

            System.out.println(map.toString(map));
        }

        System.out.println(map.toString(map));
        System.out.println("Successful, simulation complete.");
        statistics.printStatistics();

        ThreadSynchronize();
        canDisplay.setEndSimulation(true);
    }

    private void ThreadSynchronize() throws InterruptedException {
        canDisplay.setCanDisplay(true);

        while (canDisplay.getState())     // Waiting for App
            Thread.sleep(10);
    }

    @Override
    public Statistics getStatistics() {
        return this.statistics;
    }

    @Override
    public AnimalEngine getAnimalEngine() {
        return this.animalEngine;
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