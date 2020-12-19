package agh.cs.worldSimulation.engine;

import agh.cs.worldSimulation.engine.IDayObserver;

public interface IDaySubject {

    /**
     * Register new observer in lis of observers.
     */
    void register(IDayObserver o);

    /**
     * Delete observer in lis of observers.
     */
    void unregister(IDayObserver o);

    /**
     * Notify observers using lis of observers in class
     */
    void notifyObservers(int day);
}
