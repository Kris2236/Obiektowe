package agh.cs.worldSimulation;

import java.util.HashMap;
import java.util.Random;

abstract public class AbstractWorldMap extends MapBoundary {

    public abstract boolean canMoveTo(Vector2d position);

    private MapDirection generateRandomDirection() {
        Random generator = new Random();
        int randomNumber = generator.nextInt(8);

        return switch (randomNumber) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
        };
    }

    public boolean place(Animal animal) throws IllegalArgumentException {
        if(canMoveTo(animal.getPosition())){
            getAnimalsHashMap().put(animal.getPosition(), animal);
            animal.direction = generateRandomDirection();
            animal.register(this);
            animal.notifyObservers(animal.getPosition(), animal.getPosition());
            return true;
        } else {
            throw new IllegalArgumentException(animal.getPosition() + " is not legal. Off the map or position is occupated.\n");
        }
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;          // check animalsPositions
    }

    public Object objectAt(Vector2d position) {
        if(getAnimalsHashMap().containsKey(position)) {
            return getAnimalsHashMap().get(position);           // return Animal object as first - display priority
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
        super.positionChanged(oldPosition, newPosition);            // Notify changes in MapBoundary
    }
}
