package agh.cs.worldSimulation;

import java.util.ArrayList;
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
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber + "is not legal direction. Animal can not generate random direction, 8 possible directions. (AbstractMap)\n");
        };
    }

    public boolean place(Animal animal) throws IllegalArgumentException {
        if(canMoveTo(animal.getPosition())){
            getAnimalsList().add(animal);       //
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
        for(Animal animal : getAnimalsList()) {
            if (animal.getPosition().equals(position))
                return animal;
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

    public abstract ArrayList<Animal> getAnimalsList();

    public void positionChanged(Animal animal, Vector2d newPosition) {   // przekaż animal
        // final HashMap<Vector2d, Animal> animalsMap = getAnimalsHashMap();
        // Animal animal = animalsMap.get(oldPosition);
        // ArrayList<Animal> animalsList = getAnimalsList();
        System.out.println("position changed");
        // animalsMap.put(newPosition, animal);
        // super.positionChanged(oldPosition, newPosition);            // Notify changes in MapBoundary
    }
}
