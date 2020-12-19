package agh.cs.worldSimulation.other;


import agh.cs.worldSimulation.data.Vector2d;

/**
 *   Grass life cycle.
 * */
public interface ILifeCycleObserver {

    /**
     * positionGrassAdd add new grass position in mapBoundary
     * @param newPosition - new grass position required to add
     */
    void positionGrassAdd(Vector2d newPosition);

    /**
     * positionGrassDied delete grass position in mapBoundary
     * @param oldPosition - position to delete
     */
    void positionGrassDied(Vector2d oldPosition);

    /**
     * positionGrassChanged delete old and add new grass position
     * @param oldPosition
     * @param newPosition
     */
    void positionGrassChanged(Vector2d oldPosition, Vector2d newPosition);
}
