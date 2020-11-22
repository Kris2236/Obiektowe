package agh.cs.lab5;


import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends AbstractWorldMap implements IWorldMap {
    private final Vector2d boundLower = new Vector2d(0,0);
    private final Vector2d boundUpper;
    private final ArrayList<Animal> animals = new ArrayList<>();      // animals positions

    public RectangularMap(int width, int height){
        this.boundUpper = new Vector2d(width-1,height-1);
    }

    public String toString(IWorldMap map){
        return super.toString(map);
    }

    @Override
    public Vector2d lowerLeft() {
        return boundLower;
    }

    @Override
    public Vector2d upperRight() {
        return boundUpper;
    }

    @Override
    public List<Animal> getAnimalsList() {
        return this.animals;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(boundUpper) && position.follows(boundLower) && !isOccupied(position);
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return super.objectAt(position);
    }
}