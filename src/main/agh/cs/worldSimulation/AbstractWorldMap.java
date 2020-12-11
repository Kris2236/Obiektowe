package agh.cs.worldSimulation;

import java.util.HashMap;

abstract public class AbstractWorldMap extends MapBoundary {

    public abstract boolean canMoveTo(Vector2d position);

    private MapDirection generateRandomDirection() {
        // ...
        return MapDirection.NORTH;
    }

    public boolean place(Animal animal) throws IllegalArgumentException {
        if(canMoveTo(animal.position)){
            getAnimalsHashMap().put(animal.position, animal);
            animal.direction = generateRandomDirection();
            animal.register(this);
            animal.notifyObservers(animal.position, animal.position);
            return true;
        } else {
            throw new IllegalArgumentException(animal.position + " is not legal. Off the map or position is occupated.\n");
        }
    }

    public boolean isOccupied(Vector2d position) {

        // check animalsPositions
        return objectAt(position) != null;
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

    public Vector2d lowerLeft(){
        return getLowerLeft();
    }

    public  Vector2d upperRight(){
        return getUpperRight();
    }

    public abstract HashMap<Vector2d, Animal> getAnimalsHashMap();

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        final HashMap<Vector2d, Animal> animalsMap = getAnimalsHashMap();
        Animal animal = animalsMap.get(oldPosition);
        animalsMap.remove(oldPosition);
        animalsMap.put(newPosition, animal);

        // Notify changes in MapBoundary
        super.positionChanged(oldPosition, newPosition);
    }
}
