package agh.cs.worldSimulation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class GrassEngine{
    private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
    private int plantEnergy = 0;
    protected IWorldMap map;

    GrassEngine(int plantEnergy, IWorldMap map) {
        this.plantEnergy = plantEnergy;
        this.map = map;
    }

    public int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }

    public HashMap<Vector2d, Grass> getGrassMap() {
        return this.grassPositionMap;
    }

    public void placeGrass(int numberOfGrassToPlace) {
        placeGrassInStep(numberOfGrassToPlace/2);

        if(numberOfGrassToPlace % 2 == 0)
            placeGrassInJungle(numberOfGrassToPlace/2);
        else
            placeGrassInJungle(numberOfGrassToPlace/2 + 1);
    }

    public void placeGrassInJungle(int numberOfGrassToPlace) {
        ArrayList<Vector2d> emptyPositions = map.getEmptyJunglePositions();
        // do klasy abstrakcyjnej   #1
        int randomId;

        if (emptyPositions.isEmpty())
            return;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            randomId = randomNumberBetween(0, emptyPositions.size() - 1);
            addGrass(new Grass(emptyPositions.get(randomId), this.plantEnergy));
            emptyPositions.remove(randomId);
        }
    }

    private void placeGrassInStep(int numberOfGrassToPlace) {
        ArrayList<Vector2d> emptyPositions = map.getEmptyStepPositions();
        // do klasy abstrakcyjnej   #1
        int randomId;

        if (emptyPositions.isEmpty())
            return;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            randomId = randomNumberBetween(0, emptyPositions.size() - 1);
            addGrass(new Grass(emptyPositions.get(randomId), this.plantEnergy));
            emptyPositions.remove(randomId);
        }
    }

    public void addGrass(Grass newGrass, IPositionChangeObserver observer) {
        grassPositionMap.put(newGrass.getPosition(), newGrass);
        newGrass.register(observer);
        //positionGrassAdd(newGrass.getPosition());
    }
    public void addGrass(Grass newGrass) {
        grassPositionMap.put(newGrass.getPosition(), newGrass);
    }

    public void removeGrass(Vector2d position, IPositionChangeObserver observer) {
        grassPositionMap.get(position).unregister(observer);
        grassPositionMap.remove(position);
        //positionGrassDied(position);
    }

    // trawa ma informować jak zostanie zjedzona
    public int getEnergyFrom(Vector2d position) {
        int grassEnergy = 0;
        if(map.objectAt(position) instanceof Grass) {
            grassEnergy = grassPositionMap.get(position).getEnergy();
            grassPositionMap.remove(position);
            //positionGrassDied(position);
        }

        return grassEnergy;
    }
}