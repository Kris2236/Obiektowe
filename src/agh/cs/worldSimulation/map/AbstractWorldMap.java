package agh.cs.worldSimulation.map;

import agh.cs.worldSimulation.other.MapBoundary;
import agh.cs.worldSimulation.other.MapVisualizer;
import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;

import java.util.LinkedList;

abstract public class AbstractWorldMap extends MapBoundary {

    public abstract boolean canMoveTo(Vector2d position);

    public boolean place(Animal animal) throws IllegalArgumentException {
        if(canMoveTo(animal.getPosition())){
            getAnimalsList().add(animal);       //
            //animal.direction = generateRandomDirection();
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

    public abstract LinkedList<Animal> getAnimalsList();

    public void positionChanged(Animal animal, Vector2d newPosition) {   // przekaż animal
        // final HashMap<Vector2d, Animal> animalsMap = getAnimalsHashMap();
        // Animal animal = animalsMap.get(oldPosition);
        // LinkedList<Animal> animalsList = getAnimalsList();
        System.out.println("position changed");
        // animalsMap.put(newPosition, animal);
        // super.positionChanged(oldPosition, newPosition);            // Notify changes in MapBoundary
    }
}
