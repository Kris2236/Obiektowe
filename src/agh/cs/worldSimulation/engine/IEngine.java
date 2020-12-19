package agh.cs.worldSimulation.engine;

/**
 * The interface responsible for managing the moves of the animals.
 * Assumes that Vector2d and MoveDirection classes are defined.
 */
public interface IEngine {

    /**
     * Move the animal on the map according to the provided move directions. Every
     * n-th direction should be sent to the n-th animal on the map.
     */
    void run();

    /**
     * Automatically move the animal on the map according to the genotype.
     */
    void run(int maxDay);
}
