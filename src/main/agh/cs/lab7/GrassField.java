package agh.cs.lab7;

import java.util.HashMap;
import java.util.Random;

public class GrassField extends AbstractWorldMap implements IWorldMap {
    private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
    private final HashMap<Vector2d, Animal> animalsPositionMap = new HashMap<>();
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
            if(grassPositionMap.containsKey(newGrass.getPosition())){
                i--;
                uniquePosition = false;
            }

            if(uniquePosition){
                grassPositionMap.put(newGrass.getPosition(), newGrass);
                newGrass.register(this);    // not used (allow "eating" grass)
                positionChanged(newGrass.getPosition(), newGrass.getPosition(), "Grass");
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
        return grassPositionMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {

        // return Animal object as first - display priority in AbstractWorldMap
        Object object = super.objectAt(position);

        if(object != null){
            return object;
        }

        // return Grass object
        if(grassPositionMap.containsKey(position)){
            return grassPositionMap.get(position);
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
        return super.lowerLeft();
    }

    @Override
    public Vector2d upperRight() {
        return super.upperRight();
    }

    @Override
    public HashMap<Vector2d,Animal> getAnimalsHashMap() {
        return this.animalsPositionMap;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}