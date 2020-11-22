package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final ArrayList<Grass> grassPositions = new ArrayList<>();
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final int numberOfGrass;

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
            Grass newPosition = new Grass(new Vector2d(x,y));

            // check if new random grass position equals other existing
            for(Grass grass : grassPositions){
                if(grass.getPosition().equals(newPosition.getPosition())){
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
        return !isOccupied(position) || objectAt(position) instanceof Grass;
    }

    @Override
    public boolean place(Animal animal) {
        if(super.place(animal)){ // do zmiany!!!!!!!!!!!!!!!!!!!!!!!!
            animals.add(animal);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {

        // check animals in AbstractWorldMap
        boolean result = super.isOccupied(position);

        if(result){
            return true;
        }

        // check grass
        for(Grass grass : grassPositions){
            if(grass.getPosition().equals(position)){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {

        // return Animal object as first - display priority in AbstractWorldMap
        Object object = super.objectAt(position);

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
        return null;
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

    @Override
    public List<Animal> getAnimalsList() {
        return this.animals;
    }
}