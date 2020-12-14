package agh.cs.worldSimulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 */

public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    boolean placeWithDirection(Animal animal, MapDirection direction);

        /**
         * Return true if given position on the map is occupied. Should not be
         * confused with canMove since there might be empty positions where the animal
         * cannot move.
         *
         * @param position
         *            Position to check.
         * @return True if the position is occupied.
         */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);

    Object toString(IWorldMap map);

    Vector2d wrapEdge(Vector2d oldPosition, Vector2d pos, MoveDirection direction);

    void placeGrass(int numberOfGrassToPlace);

    ArrayList<Animal> generateAnimals(int numberOfAnimals, int startEnergy, int genotypeLength, int moveEnergy);

    int getEnergyFrom(Vector2d pos);

    void animalDied(Vector2d positnion);

    HashMap<Vector2d,Animal> getAnimalsHashMap();

    int randomNumberBetween(int min, int max);

}
