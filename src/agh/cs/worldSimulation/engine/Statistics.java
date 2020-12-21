package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.elements.animal.Genotype;
import agh.cs.worldSimulation.map.IWorldMap;

import java.util.*;

public class Statistics implements IDayObserver {
    private final IWorldMap map;
    private int day;
    private double globalAvgNumberOfAnimals = 0;
    private double globalAvgNumberOfAnimalsEnergy = 0;
    private double globalAvgNumberOfGrass = 0;
    private double globalAvgLifespan = 0;
    private double globalAvgNumberOfChildren = 0;
    LinkedList<Vector2d> animalsWithDominantGenotype;


    public Statistics(IWorldMap map) {
        this.map = map;
    }

    public void printStatistics() {
        System.out.println("------------------");
        System.out.println(getDay());
        System.out.println(getNumberOfAnimals());
        System.out.println(getAvgAnimalEnergy());
        System.out.println(getNumberOfGrass());
        System.out.println(getAvgLifespan());
        System.out.println(getAvgNumberOfChildren());
        System.out.println(getDominantGenotype());
        System.out.println("------------------");
    }

    public LinkedList<Vector2d> getAnimalsPosWithDominantGenotype() {
        return this.animalsWithDominantGenotype;
    }

    public String getDay() {
        return "Day: " + day;
    }

    public String getNumberOfAnimals() {
        return "Number of animals: \t" + map.getAnimalsList().size();
    }

    public String getAvgAnimalEnergy() {
        return "Avg animals energy: " + avgAnimalsEnergy();
    }

    public String getNumberOfGrass() {
        return "Number of grass: \t" + map.getGrassMapSize();
    }

    public String getAvgLifespan() {
        return "Avg lifespan dead animals: \t" + avgAnimalsLifespan();
    }

    public String getAvgNumberOfChildren() {
        return "Avg number of children:\t\t" + avgAnimalsChildren();
    }

    public String getDominantGenotype() { return "Dominant genotype: " + dominantGenotype(); }

    private double avgAnimalsEnergy() {
        LinkedList<Animal> animals = map.getAnimalsList();
        double avgEnergy = 0;
        if (animals.isEmpty())
            return avgEnergy;

        for(Animal animal : animals)
            avgEnergy += animal.getEnergy();

        avgEnergy /= animals.size();

        return Math.round(avgEnergy * 100.0)/100.0;
    }

    public LinkedList<Vector2d> setAnimalsPosWithDominantGenotype() {
        Genotype genotype = dominantGenotype();
        LinkedList<Animal> animals = map.getAnimalsList();
        LinkedList<Vector2d> animalsWithGenotype = new LinkedList<>();

        for(Animal animal : animals) {
            if(genotypesEquals(animal.getGenotype(), genotype)) {
                animalsWithGenotype.add(animal.getPosition());
            }
        }

        return animalsWithGenotype;
    }

    private boolean genotypesEquals(Genotype g1, Genotype g2) {
        List<Integer> listg1 = g1.getGenotype();
        List<Integer> listg2 = g2.getGenotype();

        for(int i=0; i< listg1.size(); i++) {
            if (!listg1.get(i).equals(listg2.get(i))) {
                return false;
            }
        }

        return true;
    }

    private Genotype dominantGenotype() {
        LinkedList<Animal> animals = map.getAnimalsList();
        if (animals.isEmpty())
            return null;

        HashMap< List<Integer>, Integer> genotypes = new HashMap<>();
        int max = 1;
        List<Integer> dominantGenotpe = animals.get(0).getGenotype().getGenotype();   // take first genotype

        // Getting dominant genotype
        for(Animal animal : animals) {
            if (genotypes.containsKey(animal.getGenotype().getGenotype())){
                int numerOfAnimalsWithGenotpe = genotypes.get(animal.getGenotype().getGenotype()) + 1;
                genotypes.replace(animal.getGenotype().getGenotype(), numerOfAnimalsWithGenotpe);

                if (numerOfAnimalsWithGenotpe > max) {
                    max = numerOfAnimalsWithGenotpe;
                    dominantGenotpe = animal.getGenotype().getGenotype();
                } else if (numerOfAnimalsWithGenotpe == max) {
                    // Many genotypes are dominating, in app there is not enough space to display
                }
            }
            else
                genotypes.put(animal.getGenotype().getGenotype(), 1);
        }

        return new Genotype(dominantGenotpe);
    }

    private double avgAnimalsLifespan() {
        LinkedList<Animal> deadAnimals = map.getDeadAnimalsList();
        double avgLifespan = 0;
        if (deadAnimals.isEmpty())
            return avgLifespan;     // No dead animal

        for(Animal animal : deadAnimals)
            avgLifespan += (animal.getDeathDay() - animal.getBirthDay());

        avgLifespan /= deadAnimals.size();
        return Math.round(avgLifespan * 100.0)/100.0;
    }

    private double avgAnimalsChildren() {
        LinkedList<Animal> animals = map.getAnimalsList();
        double avgChildren = 0;
        if (animals.isEmpty())
            return avgChildren;

        for(Animal animal : animals)
            avgChildren += animal.getChildren().size();

        avgChildren /= animals.size();
        return Math.round(avgChildren * 100.0)/100.0;
    }

    public String getGlobalStatistics() {
        return "Days: " + day + "\n" +
                "Avg Number of animals per day: \t " + globalAvgNumberOfAnimals/day + "\n" +
                "Avg animals energy per day: " + globalAvgNumberOfAnimalsEnergy/day + "\n" +
                "Avg number of grass per day: \t " + globalAvgNumberOfGrass/day + "\n" +
                "Avg lifespan dead animals per day: \t " + globalAvgLifespan/day + "\n" +
                "Avg number of children per day:\t\t" + globalAvgNumberOfChildren/day + "\n";
    }

    private void updateAvgToSave() {
        if(day <=1) {
            globalAvgNumberOfAnimals = map.getAnimalsList().size();
            globalAvgNumberOfAnimalsEnergy = avgAnimalsEnergy();
            globalAvgNumberOfGrass = map.getGrassMapSize();
            globalAvgLifespan =  avgAnimalsLifespan();
            globalAvgNumberOfChildren = avgAnimalsChildren();
        } else {
            globalAvgNumberOfAnimals += map.getAnimalsList().size();
            globalAvgNumberOfAnimalsEnergy += avgAnimalsEnergy();
            globalAvgNumberOfGrass += map.getGrassMapSize();
            globalAvgLifespan += avgAnimalsLifespan();
            globalAvgNumberOfChildren += avgAnimalsChildren();
        }
    }

    @Override
    public void dayChanged(int day) {
        this.day = day;
        updateAvgToSave();       // we can add a boolean variable to the canDispaly class and track the average stats from a specific day
        this.animalsWithDominantGenotype = setAnimalsPosWithDominantGenotype();
    }
}