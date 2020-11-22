package agh.cs.lab4;


import java.util.ArrayList;

public class RectangularMap implements IWorldMap {
    private final Vector2d boundLower = new Vector2d(0,0);
    private final Vector2d boundUpper;
    private ArrayList<Animal> animalsPositions = new ArrayList<>();      // animals positions

    public RectangularMap(int width, int height){
        this.boundUpper = new Vector2d(width-1,height-1);
    }

    public String toString(IWorldMap map){
        MapVisualizer visualize = new MapVisualizer(map);
        return visualize.draw(boundLower, boundUpper);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(position.precedes(boundUpper) && position.follows(boundLower) && !isOccupied(position)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean place(Animal animal) {
        if(!isOccupied(animal.position) && canMoveTo(animal.position)){
            animalsPositions.add(animal);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        for(Animal animal : animalsPositions){
            if(animal.position.equals(position)){
                return animal;
            }
        }

        return null;
    }
}