package agh.cs.worldSimulation.elements.plant;

import agh.cs.worldSimulation.data.Vector2d;

public class Grass {
    private final Vector2d position;
    private final int plantEnergy;                      // In future we can for each plant generate random energy


    public Grass(Vector2d position, int plantEnergy){
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public int getEnergy() {return this.plantEnergy; }

    public String toString(){
        return "*";
    }
}