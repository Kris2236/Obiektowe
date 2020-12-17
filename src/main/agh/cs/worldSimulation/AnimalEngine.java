package agh.cs.worldSimulation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class AnimalEngine {
    private final ArrayList<Animal> animalsList = new ArrayList<>();
    private final ArrayList<Animal> deadAnimalsList = new ArrayList<>();
    private final IWorldMap map;
    private final int startEnergy;
    private final int moveEnergy;

    AnimalEngine(IWorldMap map, int startEnergy, int moveEnergy) {
        this.map = map;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
    }

    public void reproduce(Vector2d position) {
        ArrayList<Animal> parents = getStrongestParents(position);

        //System.out.println("Parents energy: " + parents.get(0).lifeEnergy + " " + parents.get(1).lifeEnergy);
        //System.out.println("Child: " + (parents.get(0).lifeEnergy/4 + parents.get(1).lifeEnergy/4));

        if (parents.get(0).lifeEnergy < startEnergy/2 || parents.get(1).lifeEnergy < startEnergy/2  // Not enough energy
            || getPositionForSmallAnimal(parents.get(0).getPosition()) == null)                     // No empty position for child
            return;

        // Getting energy from parents
        int energyFromParents = parents.get(0).lifeEnergy/4 + parents.get(1).lifeEnergy/4;
        parents.get(0).lifeEnergy = parents.get(0).lifeEnergy - parents.get(0).lifeEnergy/4;
        parents.get(1).lifeEnergy = parents.get(1).lifeEnergy - parents.get(1).lifeEnergy/4;

        Vector2d smallAnimalPosition = getPositionForSmallAnimal(parents.get(0).getPosition());

        Animal smallAnimal = new Animal(map, smallAnimalPosition, energyFromParents, new Genotype(parents.get(0).genotype, parents.get(1).genotype), moveEnergy);
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

        ArrayList<Vector2d> emptyPositions = map.getEmptyJunglePositions();             // Else get random position
        if (emptyPositions.isEmpty())
            emptyPositions = map.getEmptyStepPositions();
        else if (map.getEmptyStepPositions().isEmpty())
            return null;

        return emptyPositions.get(randomNumberBetween(0, emptyPositions.size() - 1));
    }

    public ArrayList<Animal> getAnimalsOn(Vector2d position) {
        ArrayList<Animal> animalsOn = new ArrayList<>();
        for(Animal animal : animalsList) {
            if (animal.getPosition().equals(position))
                animalsOn.add(animal);
        }
        Collections.sort(animalsOn);
        Collections.reverse(animalsOn);

        return animalsOn;
    }

    private ArrayList<Animal> getStrongestParents(Vector2d position) {
        ArrayList<Animal> parents = getAnimalsOn(position);
        parents.subList(0,1);
        return parents;
    }

    public ArrayList<Animal> getAnimalsList(){ return this.animalsList; }

    public ArrayList<Animal> getDeadAnimalsList(){ return this.deadAnimalsList; }

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
        animalsList.remove(animal);
    }

    public void generateAnimals(int numberOfAnimalsToAdd) {
        ArrayList<Vector2d> emptyPositions = map.getEmptyJunglePositions();
        int randomId;

        for(int i=0; i<numberOfAnimalsToAdd; i++) {

            if (emptyPositions.isEmpty())
                emptyPositions = map.getEmptyStepPositions();
            else if (map.getEmptyStepPositions().isEmpty())
                return;

            randomId = randomNumberBetween(0, emptyPositions.size() - 1);
            Animal newAnimal = new Animal(map, emptyPositions.get(randomId), startEnergy, new Genotype(), moveEnergy);
            emptyPositions.remove(randomId);

            map.place(newAnimal);
        }
    }

    public int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }
}
