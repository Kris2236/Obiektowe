package agh.cs.worldSimulation.engine;

public interface IDayObserver {
    /**
     * Update date, necesery to add animals
     */
    void dayChanged(int day);
}
