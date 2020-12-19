package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.elements.animal.Genotype;
import agh.cs.worldSimulation.map.IWorldMap;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Collections;

public class AnimalEngine implements IDayObserver {
    private final LinkedList<Animal> animalsList = new LinkedList<>();
    private final LinkedList<Animal> deadAnimalsList = new LinkedList<>();
    public IWorldMap map;
    private final int startEnergy;
    private final int moveEnergy;
    private int day;

    public AnimalEngine(IWorldMap map, int startEnergy, int moveEnergy) {
        this.map = map;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
    }

    public void reproduce(Vector2d position) {
        LinkedList<Animal> parents = getStrongestParents(position);

        if (parents.get(0).getEnergy() < startEnergy/2 || parents.get(1).getEnergy() < startEnergy/2  // Not enough energy
            || getPositionForSmallAnimal(parents.get(0).getPosition()) == null)                     // No empty position for child
            return;

        // Getting energy from parents
        int energyFromParents = parents.get(0).getQuaterEnergy() + parents.get(1).getQuaterEnergy();
//        parents.get(0).lifeEnergy = parents.get(0).lifeEnergy - parents.get(0).lifeEnergy/4;
//        parents.get(1).lifeEnergy = parents.get(1).lifeEnergy - parents.get(1).lifeEnergy/4;

        Vector2d smallAnimalPosition = getPositionForSmallAnimal(parents.get(0).getPosition());
        Animal smallAnimal = new Animal(map, smallAnimalPosition, energyFromParents, new Genotype(parents.get(0).getGenotype(), parents.get(1).getGenotype()), moveEnergy, this.day);

        System.out.println(smallAnimal.getGenotype().getGenotype());

        // Adding small animal as children
        parents.get(0).getChildren().add(smallAnimal);
        parents.get(1).getChildren().add(smallAnimal);

        map.place(smallAnimal);
    }

    private Vector2d getPositionForSmallAnimal(Vector2d parentPosition) {
        Vector2d v;

        for(int x=parentPosition.x-1; x<=parentPosition.x+1; x++) {             // Fining free position nearby parents
            for(int y=parentPosition.y-1; y<=parentPosition.y+1; y++) {
                v = new Vector2d(x,y);
                if (map.canMoveTo(v) && !map.isOccupied(v)) {
                    return v;
                }
            }
        }

        LinkedList<Vector2d> emptyPositions = map.getEmptyJunglePositions();             // Else get random position
        if (emptyPositions.isEmpty())
            emptyPositions = map.getEmptyStepPositions();
        else if (map.getEmptyStepPositions().isEmpty())
            return null;

        return emptyPositions.get(randomNumberBetween(0, emptyPositions.size() - 1));
    }

    public LinkedList<Animal> getAnimalsOn(Vector2d position) {
        LinkedList<Animal> animalsOn = new LinkedList<>();
        for(Animal animal : animalsList) {
            if (animal.getPosition().equals(position))
                animalsOn.add(animal);
        }
        Collections.sort(animalsOn);

        return animalsOn;
    }

    public void sortByEnergy() {
        System.out.println(animalsList);
        Collections.sort(animalsList);

//        for(int i=0; i<animalsList.size(); i++)
//            System.out.println(animalsList.get(i) + " \tEnergy: " + animalsList.get(i).lifeEnergy);
    }

    private LinkedList<Animal> getStrongestParents(Vector2d position) {
        LinkedList<Animal> parents = getAnimalsOn(position);
        parents.subList(0,1);
        return parents;
    }

    public LinkedList<Animal> getAnimalsList(){ return this.animalsList; }

    public LinkedList<Animal> getDeadAnimalsList(){ return this.deadAnimalsList; }

    private int getAnimalId(Animal animal) throws IllegalArgumentException {
        for(int i=0; i<animalsList.size(); i++) {
            if(animalsList.get(i).equals(animal))
                return i;
        }
        throw new IllegalArgumentException(animal + ": animal is not in animalsList. \n");
    }

    public void animalDied(Animal animal) {
        //animalsList.get(getAnimalId(animal)).unregister(observer);
        deadAnimalsList.add(animalsList.get(getAnimalId(animal)));       // add to dead animal list
        animal.dateOfDeath(this.day);
        animalsList.remove(animal);
    }

    public void generateAnimals(int numberOfAnimalsToAdd) {
        LinkedList<Vector2d> emptyPositions = map.getEmptyJunglePositions();
        int randomId;

        for(int i=0; i<numberOfAnimalsToAdd; i++) {

            if (emptyPositions.isEmpty())
                emptyPositions = map.getEmptyStepPositions();
            else if (map.getEmptyStepPositions().isEmpty())
                return;

            randomId = randomNumberBetween(0, emptyPositions.size() - 1);
            Animal newAnimal = new Animal(map, emptyPositions.get(randomId), startEnergy, new Genotype(), moveEnergy, this.day);
            emptyPositions.remove(randomId);

            map.place(newAnimal);
        }
    }

    public int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }

    @Override
    public void dayChanged(int day) {
        this.day = day;
    }
}