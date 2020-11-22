package agh.cs.lab5;

import java.util.ArrayList;
import java.util.Random;

public class GrassField extends agh.cs.lab5.AbstractWorldMap implements agh.cs.lab5.IWorldMap, agh.cs.lab5.IMapElement {
    protected ArrayList<agh.cs.lab5.Grass> grassPositions = new ArrayList<>();
    protected ArrayList<Grass> animalsPositions = new ArrayList<>();
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
                System.out.println("Added grass: " + newPosition.getPosition());
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
        return object;
    }

    @Override
    public String toString(IWorldMap map) {
        Vector2d upperRight = grassPositions.get(0).getPosition();
        Vector2d lowerLeft = grassPositions.get(0).getPosition();

        // determine the extent of the map - checking grass positions
        for(Grass grass : grassPositions) {
            upperRight = grass.getPosition().upperRight(upperRight);
            lowerLeft = grass.getPosition().lowerLeft(lowerLeft);
        }

        // determine the extent of the map - checking animals positions
        for(Animal animal : animals) {
            upperRight = animal.position.upperRight(upperRight);
            lowerLeft = animal.position.lowerLeft(lowerLeft);
        }

        // initializing the map display
        MapVisualizer visualize = new MapVisualizer(map);
        return visualize.draw(lowerLeft, upperRight);
    }
}
