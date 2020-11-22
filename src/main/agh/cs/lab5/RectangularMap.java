package agh.cs.lab5;


import java.util.ArrayList;

public class RectangularMap extends agh.cs.lab5.AbstractWorldMap implements agh.cs.lab5.IWorldMap {
    private final Vector2d boundLower = new Vector2d(0,0);
    private final Vector2d boundUpper;
    private ArrayList<Animal> animals = new ArrayList<>();      // animals positions

    public RectangularMap(int width, int height){
        this.boundUpper = new Vector2d(width-1,height-1);
    }

    public String toString(IWorldMap map){
//        MapVisualizer visualize = new MapVisualizer(map);
//        return visualize.draw(boundLower, boundUpper);

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
    public boolean canMoveTo(Vector2d position) {
        if(position.precedes(boundUpper) && position.follows(boundLower) && !isOccupied(position)){
            return true;
        }

        return false;
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal, this.animals);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position, this.animals);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return super.objectAt(position, this.animals);
    }
}