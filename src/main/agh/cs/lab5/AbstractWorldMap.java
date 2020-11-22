package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractWorldMap implements IMapElement {
    //protected ArrayList<Animal> animals = new ArrayList<>();    //1!!!!!!!!!!!!!!!!!!!!!!


    public abstract boolean canMoveTo(Vector2d position);

    public boolean place(Animal animal, ArrayList<Animal> animalsPositions) {
        if(canMoveTo(animal.position)){
            if(!isOccupied(animal.position, animalsPositions) || objectAt(animal.position, animalsPositions) instanceof Grass) {
                return true;
            }
        }

        return false;
    }

    public boolean isOccupied(Vector2d position, ArrayList<Animal> animalsPositions) {
        // check animalsPositions
        for(Animal animal : animalsPositions){
            if(position.equals(animal.position)){
                return true;
            }
        }
        return false;
    }

    public Object objectAt(Vector2d position, ArrayList<Animal> animalsPositions) {
        // return Animal object as first - display priority
        for(Animal animal : animalsPositions){
            if(animal.position.equals(position)){
                return animal;
            }
        }

        return null;
    }

    public String toString(IWorldMap map){
        MapVisualizer visualize = new MapVisualizer(map);   // map -> this i toString()
        return visualize.draw(lowerLeft(), upperRight());
    }

    public abstract Vector2d lowerLeft();

    public abstract Vector2d upperRight();
}
