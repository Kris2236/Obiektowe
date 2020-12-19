package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.elements.animal.Genotype;
import agh.cs.worldSimulation.map.IWorldMap;

import java.util.LinkedList;

public class Statistics implements IDayObserver {
    protected final IWorldMap map;
    private int day;
    // save data for n-th day

    public Statistics(IWorldMap map) {
        this.map = map;
    }

    public void printStatistics() {
        System.out.println("------------------");
        System.out.println("Day: " + day);
        System.out.println("Number of animals: \t" + map.getAnimalsList().size());
        System.out.println("Avg animals energy: " + avgAnimalsEnergy());
        System.out.println("Number of grass: \t" + map.getGrassMapSize());
        System.out.println("Avg lifespan dead animals: \t" + avgAnimalsLifespan());
        System.out.println("Avg number of children:\t\t" + avgAnimalsChildren());
        System.out.println("Dominant genotype: ");
        dominantGenotype();
        System.out.println("------------------");
    }

    private double avgAnimalsEnergy() {
        LinkedList<Animal> animals = map.getAnimalsList();
        double avgEnergy = 0;
        if (animals.isEmpty())
            return avgEnergy;

        for(Animal animal : animals)
            avgEnergy += animal.getEnergy();

        avgEnergy /= animals.size();
        return avgEnergy;
    }

    private void dominantGenotype() {
        LinkedList<Animal> animals = map.getAnimalsList();
        if (animals.isEmpty())
            return;

        LinkedList<Genotype> genotypes = new LinkedList<>();

        for(Animal animal : animals)
            genotypes.add(animal.getGenotype());

        // wybierz dominujący - najczęściej występujący

        for(Genotype genotype : genotypes)
            System.out.println(genotype.getGenotype());
    }

    private double avgAnimalsLifespan() {
        LinkedList<Animal> deadAnimals = map.getDeadAnimalsList();
        double avgLifespan = 0;
        if (deadAnimals.isEmpty())
            return avgLifespan;     // No dead animal

        for(Animal animal : deadAnimals)
            avgLifespan += (animal.getDeathDay() - animal.getBirthDay());

        avgLifespan /= deadAnimals.size();
        return avgLifespan;
    }

    private double avgAnimalsChildren() {
        LinkedList<Animal> animals = map.getAnimalsList();
        double avgChildren = 0;
        if (animals.isEmpty())
            return avgChildren;

        for(Animal animal : animals)
            avgChildren += animal.getChildren().size();

        avgChildren /= animals.size();
        return avgChildren;
    }

    @Override
    public void dayChanged(int day) {
        this.day = day;
    }
}
