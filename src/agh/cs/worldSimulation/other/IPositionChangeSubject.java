package agh.cs.worldSimulation.other;

import agh.cs.worldSimulation.data.Vector2d;

/**
 * The interface responsible for managing subjects in observer pattern.
 * Class implementing should contain a lis of observers.
 */
public interface IPositionChangeSubject {

    /**
     * Register new observer in lis of observers.
     */
    void register(IPositionChangeObserver o);

    /**
     * Delete observer in lis of observers.
     */
    void unregister(IPositionChangeObserver o);

    /**
     * Notify observers using lis of observers in class
     */
    void notifyObservers(Vector2d oldPosition, Vector2d newPosition);
}