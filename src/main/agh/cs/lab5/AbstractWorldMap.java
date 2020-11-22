package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractWorldMap implements IMapElement {

    public abstract boolean canMoveTo(Vector2d position);

    public boolean place(Animal animal) {
        if(canMoveTo(animal.position)){
            // zosrawiam w tej formie do kolejnych labów
            return true;
        }

        return false;
    }

    public boolean isOccupied(Vector2d position) {
        List<Animal> animalsPositions = getAnimalsList();
        // check animalsPositions
        for(Animal animal : animalsPositions){
            if(position.equals(animal.position)){
                return true;
            }
        }
        return false;
    }

    public Object objectAt(Vector2d position) {
        List<Animal> animalsPositions = getAnimalsList();

        // return Animal object as first - display priority
        for(Animal animal : animalsPositions){
            if(animal.position.equals(position)){
                return animal;
            }
        }

        return null;
    }

    public String toString(IWorldMap map){
        MapVisualizer visualize = new MapVisualizer(map);
        return visualize.draw(lowerLeft(), upperRight());
    }

    public abstract Vector2d lowerLeft();

    public abstract Vector2d upperRight();

    public abstract List<Animal> getAnimalsList();
}
