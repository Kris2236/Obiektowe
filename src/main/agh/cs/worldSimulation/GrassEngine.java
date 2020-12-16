package agh.cs.worldSimulation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrassEngine{
    private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
    private int plantEnergy = 0;
    protected IWorldMap map;

    GrassEngine(int plantEnergy, IWorldMap map) {
        this.plantEnergy = plantEnergy;
        this.map = map;
    }

    // extrakcja
    private Vector2d randomVector2dBetween(Vector2d boundFieldLower, Vector2d boundFieldUpper) {
        SecureRandom generator = new SecureRandom();
        int x = generator.nextInt(boundFieldUpper.x - boundFieldLower.x + 1) + boundFieldLower.x;
        int y = generator.nextInt(boundFieldUpper.y - boundFieldLower.y + 1) + boundFieldLower.y;
        return new Vector2d(x,y);
    }

    public int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }   //----------


    public HashMap<Vector2d, Grass> getGrassMap() {
        return this.grassPositionMap;
    }

    public void placeGrass(int numberOfGrassToPlace) {
        placeGrassInStep(numberOfGrassToPlace/2, map.getMapBoundLower(), map.getMapBoundUpper());

        if(numberOfGrassToPlace % 2 == 0)
            placeGrassBetween(numberOfGrassToPlace/2, map.getJungleBoundLower(), map.getJungleBoundUpper());
        else
            placeGrassBetween(numberOfGrassToPlace/2 + 1, map.getJungleBoundLower(), map.getJungleBoundUpper());
    }

    public void placeGrassBetween(int numberOfGrassToPlace,Vector2d boundFieldLower ,Vector2d boundFieldUpper) {
        int counterFailedTrial = 0;
        Grass newGrass;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            boolean uniquePosition = true;
            newGrass = new Grass(randomVector2dBetween(boundFieldLower, boundFieldUpper), this.plantEnergy);

            if(counterFailedTrial > 2 * numberOfGrassToPlace) {       // Check if are there any empty positions !!!! you can check how many position in field are empty
                Vector2d[] emptyPositions = emptyPositionsBetween(boundFieldLower, boundFieldUpper);
                if(emptyPositions.length == 0)
                    return;                     // stop function - all positions are occupied
                else
                    newGrass = new Grass(emptyPositions[randomNumberBetween(0, emptyPositions.length-1)]);     // Choose random empty position

            } else if(map.objectAt(newGrass.getPosition()) instanceof Animal) {
                i--;
                uniquePosition = false;
                counterFailedTrial++;
            }

            if(uniquePosition)
                addGrass(newGrass);
        }
    }

    private Vector2d[] emptyPositionsBetween(Vector2d boundFieldLower, Vector2d boundFieldUpper) {
        List<Vector2d> emptyPositions = new ArrayList<>();

        for(int x=boundFieldLower.x; x<=boundFieldUpper.x; x++) {
            for(int y=boundFieldLower.y; y<=boundFieldUpper.y; y++) {
                if(!map.isOccupied(new Vector2d(x,y)))
                    emptyPositions.add(new Vector2d(x,y));
            }
        }

        return emptyPositions.toArray(new Vector2d[0]);
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

    private void placeGrassInStep(int numberOfGrassToPlace,Vector2d boundFieldLower ,Vector2d boundFieldUpper) {
        Grass newGrass;
        int counterFailedTrial = 0;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            newGrass = new Grass(randomVector2dBetween(boundFieldLower, boundFieldUpper), this.plantEnergy);

            // Do zoptymalizowania!!!
            while(map.objectAt(newGrass.getPosition()) instanceof Animal || !map.isInStep(newGrass.getPosition())){
                counterFailedTrial++;
                newGrass = new Grass(randomVector2dBetween(boundFieldLower, boundFieldUpper));
            }

            // Będzie warotść informująca ile procent mapt jest zajęte !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if(counterFailedTrial > 10*numberOfGrassToPlace)    // jeśli 100% mapy jest zajęte
                return;

            addGrass(newGrass);
        }
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