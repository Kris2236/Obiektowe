package agh.cs.worldSimulation;

public interface IDayObserver {
    /**
     * Update date, necesery to add animals
     */
    void dayChanged(int day);
}
