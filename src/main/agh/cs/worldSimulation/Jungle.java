package agh.cs.worldSimulation;

import java.security.SecureRandom;
import java.util.*;

public class Jungle extends AbstractWorldMap implements IWorldMap {
    private final Vector2d mapBoundLower = new Vector2d(0,0);
    private final Vector2d mapBoundUpper;
    private Vector2d jungleBoundLower;
    private Vector2d jungleBoundUpper;
    private final double jungleRatio;
    private GrassEngine grassEngine;

    // inna klasa ma się tym zajmować
    private final ArrayList<Animal> animalsList = new ArrayList<>();
    private final ArrayList<Animal> deadAnimalsList = new ArrayList<>();


    public Jungle(int width, int height, int initialNumberOfGrass, double jungleRatio, int plantEnergy) {
        this.mapBoundUpper = new Vector2d(width-1,height-1);
        this.jungleRatio = jungleRatio;
        //this.plantEnergy = plantEnergy;
        createJungle(this.jungleRatio);

        this.grassEngine = new GrassEngine(plantEnergy, this);

        grassEngine.placeGrass(initialNumberOfGrass);
    }

    public Vector2d getMapBoundLower() { return  this.mapBoundLower; }
    public Vector2d getMapBoundUpper() { return  this.mapBoundUpper; }
    public Vector2d getJungleBoundLower() { return  this.mapBoundLower; }
    public Vector2d getJungleBoundUpper() { return  this.mapBoundUpper; }

    private void createJungle(double jungleRatio) {
        if(jungleRatio <= 0 || jungleRatio > 1)
            throw new IllegalArgumentException(jungleRatio + " is not legal. JungleRatio have to be in range (0.0 < jungleRatio <= 1.0).\n");

        int x = (int) (mapBoundUpper.x*jungleRatio);
        int y = (int) (mapBoundUpper.y*jungleRatio);

        if(jungleRatio != 0) {          // Set min jungle size
            if(x == 0)
                x = 1;
            if(y == 0)
                y = 1;
        }

        Vector2d jungleDimensions = new Vector2d(x,y);
        Vector2d mapCenter = new Vector2d(mapBoundUpper.x/2,mapBoundUpper.y/2);

        if(jungleRatio == 1) {          // Always the map must be divided into 2 types (jungle and step)
            this.jungleBoundUpper = mapBoundUpper.substract(new Vector2d(1,1));
            this.jungleBoundLower = mapBoundLower.add(new Vector2d(1,1));
        } else {
            this.jungleBoundUpper = new Vector2d(mapCenter.x + jungleDimensions.x/2,mapCenter.y + jungleDimensions.y/2);
            this.jungleBoundLower = new Vector2d(mapCenter.x - jungleDimensions.x/2,mapCenter.y - jungleDimensions.y/2);
        }
    }

    // to mapVisualizer
    private void printJungle(){
        for(int y=0; y<=mapBoundUpper.y; y++){
            for(int x=0; x<=mapBoundUpper.x; x++){
                if(isInJungle(new Vector2d(x,y)))
                    System.out.print("T");
                else
                    System.out.print("F");
            }
            System.out.println();
        }
    }

    // new class GrassEngine
    public void addDailyGrass() {
        grassEngine.placeGrass(2);
    }

    public int getEnergyFrom(Vector2d position) {
        return grassEngine.getEnergyFrom(position);
    }
    // ----

    // klasa randomGenerator
    private Vector2d randomVector2dBetween(Vector2d boundFieldLower, Vector2d boundFieldUpper) {
        SecureRandom generator = new SecureRandom();
        int x = generator.nextInt(boundFieldUpper.x - boundFieldLower.x + 1) + boundFieldLower.x;
        int y = generator.nextInt(boundFieldUpper.y - boundFieldLower.y + 1) + boundFieldLower.y;
        return new Vector2d(x,y);
    }

    public int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }

    // new class Genotype
    private int[] generateRandomGenotype(int genotypeLength) {
        int[] randomGenotype = new int[genotypeLength];
        for(int i=0; i<genotypeLength; i++)
            randomGenotype[i] = randomNumberBetween(0,7);

        return randomGenotype;
    }


    // new Class AnimalsEngine
    @Override
    public ArrayList<Animal> generateAnimals(int numberOfAnimalsToAdd, int startEnergy, int genptypeLength, int moveEnergy) {
        int counterFailedTrial = 0;
        Vector2d boundFieldLower = mapBoundLower;
        Vector2d boundFieldUpper = mapBoundUpper;
        ArrayList<Animal> animalsList = new ArrayList<>();

        for(int i=0; i<numberOfAnimalsToAdd; i++) {
            boolean uniquePosition = true;

            if(counterFailedTrial > numberOfAnimalsToAdd) {         // Enlarge the adding area
                if(!boundFieldLower.precedes(mapBoundLower))
                    boundFieldLower.substract(new Vector2d(1,1));
                if(boundFieldUpper.follows(mapBoundUpper))
                    boundFieldUpper.add(new Vector2d(1,1));
            }

            Animal newAnimal = new Animal(this, randomVector2dBetween(boundFieldLower, boundFieldUpper), startEnergy, generateRandomGenotype(genptypeLength), moveEnergy);

            if(objectAt(newAnimal.getPosition()) instanceof Animal) {        // Check if new random animal position equals other existing
                i--;
                counterFailedTrial++;
                uniquePosition = false;
            }

            if(grassEngine.getGrassMap().containsKey(newAnimal.getPosition())) {      // Move grass if is collision
                grassEngine.placeGrassBetween(1, mapBoundLower, mapBoundUpper);        // Add grass in all field because animals are adding in savanna
                grassEngine.removeGrass(newAnimal.getPosition(), this);
            }

            if(uniquePosition) {
                this.place(newAnimal);
                animalsList.add(newAnimal);
            }
        }

        return animalsList;
    }
    // -----

    // to map direction
    private final Vector2d vX = new Vector2d(1,0);
    private final Vector2d vY = new Vector2d(0,1);

    private Vector2d northWrap(Vector2d position) {
        if(canMoveTo(position.add(vY)))
            return position.add(vY);
        return new Vector2d(position.x, mapBoundLower.y);
    }

    private Vector2d northEastWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).add(vX)))
            return northWrap(position).add(vX);
        return eastWrap(northWrap(position));
    }

    private Vector2d eastWrap(Vector2d position) {
        if(canMoveTo(position.add(vX)))
            return position.add(vX);
        return new Vector2d(mapBoundLower.x, position.y);
    }

    private Vector2d southEastWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).add(vX)))
            return southWrap(position).add(vX);
        return eastWrap(southWrap(position));
    }

    private Vector2d southWrap(Vector2d position) {
        if(canMoveTo(position.substract(vY)))
            return position.substract(vY);
        return new Vector2d(position.x, mapBoundUpper.y);
    }

    private Vector2d southWestWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).substract(vX)))
            return southWrap(position).substract(vX);
        return westWrap(southWrap(position));
    }

    private Vector2d westWrap(Vector2d position) {
        if(canMoveTo(position.substract(vX)))
            return position.substract(vX);
        return new Vector2d(mapBoundUpper.x, position.y);
    }

    private Vector2d northWestWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).substract(vX)))
            return northWrap(position).substract(vX);
        return westWrap(northWrap(position));
    }

    public Vector2d wrapEdge(Animal animal, Vector2d position, MoveDirection direction) {
        //Animal animal = animalsPositionMap.get(oldPosition);
        // Animal animal = animalsList.get(getAnimalId(animal));

        // tounitVector() !!!
        Vector2d wrappedPosition = switch (animal.toString()) {
            case "^" -> northWrap(position);
            case "1" -> northEastWrap(animal.getPosition());
            case ">" -> eastWrap(position);
            case "3" -> southEastWrap(animal.getPosition());
            case "v" -> southWrap(position);
            case "5" -> southWestWrap(animal.getPosition());
            case "<" -> westWrap(position);
            case "7" -> northWestWrap(animal.getPosition());
            default -> null;
        };

        return wrappedPosition;
    }

    private int getAnimalId(Animal animal) throws IllegalArgumentException {
        for(int i=0; i<animalsList.size(); i++) {
            if(animalsList.get(i).equals(animal))    // czy ok???
                return i;
        }

        throw new IllegalArgumentException(animal + ": animal is not in animalsList. \n");
    }

    @Override
    public void animalDied(Animal animal) {
        animalsList.get(getAnimalId(animal)).unregister(this);
        deadAnimalsList.add(animalsList.get(getAnimalId(animal)));       // add to dead animal list
        animalsList.remove(animal);
    }

    private boolean isInMap(Vector2d position) {
    return position.precedes(mapBoundUpper) && position.follows(mapBoundLower);
    }

    private boolean isInJungle(Vector2d position) {
        return position.follows(jungleBoundLower) && position.precedes(jungleBoundUpper);
    }

    public boolean isInStep(Vector2d position) {
        return isInMap(position) && !isInJungle(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return isInMap(position);
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal);
    }

    public boolean placeWithDirection(Animal animal, MapDirection direction) {
        if(super.place(animal)) {
            animal.direction = direction;
            return true;
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        boolean result = super.isOccupied(position);        // check animals in AbstractWorldMap
        if(result)
            return true;

        return grassEngine.getGrassMap().containsKey(position);      // check grass
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);        // return Animal object as first - display priority in AbstractWorldMap
        if(object != null)
            return object;

        if(grassEngine.getGrassMap().containsKey(position))        // return Grass object
            return grassEngine.getGrassMap().get(position);

        return null;        // return no object
    }

    @Override
    public String toString(IWorldMap map) { return super.toString(map); }

    @Override
    public Vector2d lowerLeft() { return mapBoundLower; }

    @Override
    public Vector2d upperRight() { return mapBoundUpper; }

    @Override
    public ArrayList<Animal> getAnimalsList() { return this.animalsList; }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}