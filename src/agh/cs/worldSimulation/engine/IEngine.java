package agh.cs.worldSimulation.engine;

/**
 * The interface responsible for managing the moves of the animals.
 * Assumes that Vector2d and MoveDirection classes are defined.
 */
public interface IEngine {
    /**
     * Automatically move the animal on the map according to the genotype.
     */
    void run(int maxDay) throws InterruptedException;

    Statistics getStatistics() throws InterruptedException;

    AnimalEngine getAnimalEngine() throws InterruptedException;
}
