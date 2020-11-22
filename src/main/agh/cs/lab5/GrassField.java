package agh.cs.lab5;

import java.util.ArrayList;
import java.util.Random;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    protected ArrayList<agh.cs.lab5.Grass> grassPositions = new ArrayList<>();
    protected ArrayList<Animal> animals = new ArrayList<>();
    private int numberOfGrass;

    public GrassField(int numberOfGrass){
        this.numberOfGrass = numberOfGrass;
        placeGrass();
    }

    private void placeGrass(){
        for(int i=0; i<this.numberOfGrass; i++){
            Random generator = new Random();

            int x = generator.nextInt((int) Math.sqrt( (this.numberOfGrass+1)) * 10);
            int y = generator.nextInt((int) Math.sqrt( (this.numberOfGrass+1)) * 10);
            boolean uniquePosition = true;
            agh.cs.lab5.Grass newPosition = new agh.cs.lab5.Grass(new agh.cs.lab5.Vector2d(x,y));

            // check if new random grass position equals other existing
            for(agh.cs.lab5.Grass grass : grassPositions){
                if(grass.getPosition().equals(newPosition)){
                    i--;
                    uniquePosition = false;
                }
            }

            if(uniquePosition){
                grassPositions.add(newPosition);
            }
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(objectAt(position) instanceof Animal){
            return false;
        }
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if(super.place(animal, this.animals)){
            animals.add(animal);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {

        // check animals in AbstractWorldMap
        boolean result = super.isOccupied(position, this.animals);

        if(result){
            return true;
        }

        // check grass
        for(Grass grass : grassPositions){
            if(grass.getPosition().equals(position)){
                return true;
            }
        }

        return result;
    }

    @Override
    public Object objectAt(Vector2d position) {
        // return Animal object as first - display priority in AbstractWorldMap
        Object object = super.objectAt(position, this.animals);

        if(object != null){
            return object;
        }

        // return Grass object
        for(Grass grass : grassPositions){
            if(grass.getPosition().equals(position)){
                return grass;
            }
        }

        // return no object
        return object;
    }

    @Override
    public String toString(IWorldMap map) {
        return super.toString(map);
    }

    @Override
    public Vector2d lowerLeft() {
        Vector2d lowerLeft = grassPositions.get(0).getPosition();

        // determine the extent of the map - checking grass positions
        for(Grass grass : grassPositions) {
            lowerLeft = grass.getPosition().lowerLeft(lowerLeft);
        }

        // determine the extent of the map - checking animals positions
        for(Animal animal : animals) {
            lowerLeft = animal.position.lowerLeft(lowerLeft);
        }

        return lowerLeft;
    }

    @Override
    public Vector2d upperRight() {
        Vector2d upperRight = grassPositions.get(0).getPosition();

        // determine the extent of the map - checking grass positions
        for(Grass grass : grassPositions) {
            upperRight = grass.getPosition().upperRight(upperRight);
        }

        // determine the extent of the map - checking animals positions
        for(Animal animal : animals) {
            upperRight = animal.position.upperRight(upperRight);
        }

        return upperRight;
    }
}
