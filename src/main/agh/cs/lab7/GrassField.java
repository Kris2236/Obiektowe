package agh.cs.lab7;

import java.util.HashMap;
import java.util.Random;

public class GrassField extends AbstractWorldMap implements IWorldMap {
    private final HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    private final HashMap<Vector2d, Animal> animalsMap = new HashMap<>();
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
            Grass newGrass = new Grass(new Vector2d(x,y));

            // check if new random grass position equals other existing
            if(grassMap.containsKey(newGrass.getPosition())){
                i--;
                uniquePosition = false;
            }

            if(uniquePosition){
                grassMap.put(newGrass.getPosition(), newGrass);
                // add to map boundry
            }
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) || objectAt(position) instanceof Grass;
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {

        // check animals in AbstractWorldMap
        boolean result = super.isOccupied(position);

        if(result){
            return true;
        }

        // check grass
        return grassMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {

        // return Animal object as first - display priority in AbstractWorldMap
        Object object = super.objectAt(position);

        if(object != null){
            return object;
        }

        // return Grass object
        if(grassMap.containsKey(position)){
            return grassMap.get(position);
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
        Vector2d lowerLeft = new Vector2d(0,0); // Do zmiany - pobierz element z mapy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // determine the extent of the map - checking grass positions
        for(Vector2d grassPos : grassMap.keySet()) {
            lowerLeft = grassPos.lowerLeft(lowerLeft);
        }

        // determine the extent of the map - checking animals positions
        for(Vector2d animalPos : animalsMap.keySet()) {
            lowerLeft = animalPos.lowerLeft(lowerLeft);
        }

        return lowerLeft;
    }

    @Override
    public Vector2d upperRight() {
        Vector2d upperRight = new Vector2d(0,0); // Do zmiany - pobierz element z mapy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // determine the extent of the map - checking grass positions
        for(Vector2d grassPos : grassMap.keySet()) {
            upperRight = grassPos.upperRight(upperRight);
        }

        // determine the extent of the map - checking animals positions
        for(Vector2d animalPos : animalsMap.keySet()) {
            upperRight = animalPos.upperRight(upperRight);
        }

        return upperRight;
    }

    @Override
    public HashMap<Vector2d,Animal> getAnimalsHashMap() {
        return this.animalsMap;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}