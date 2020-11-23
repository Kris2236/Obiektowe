package agh.cs.lab5;

import java.util.HashMap;

abstract public class AbstractWorldMap implements IMapElement, IPositionChangeObserver {

    public abstract boolean canMoveTo(Vector2d position);

    public boolean place(Animal animal) {
        if(canMoveTo(animal.position)){
            getAnimalsHashMap().put(animal.position, animal);
            return true;
        }

        return false;
    }

    public boolean isOccupied(Vector2d position) {

        // check animalsPositions
        return getAnimalsHashMap().containsKey(position);
    }

    public Object objectAt(Vector2d position) {

        // return Animal object as first - display priority
        if(getAnimalsHashMap().containsKey(position)) {
            return getAnimalsHashMap().get(position);
        }

        return null;
    }

    public String toString(IWorldMap map){
        MapVisualizer visualize = new MapVisualizer(map);
        return visualize.draw(lowerLeft(), upperRight());
    }

    public abstract Vector2d lowerLeft();

    public abstract Vector2d upperRight();

    public abstract HashMap<Vector2d, Animal> getAnimalsHashMap();

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        final HashMap<Vector2d, Animal> animalsMap = getAnimalsHashMap();
        Animal animal = animalsMap.get(oldPosition);
        animalsMap.remove(oldPosition);
        animalsMap.put(newPosition, animal);
    }
}
