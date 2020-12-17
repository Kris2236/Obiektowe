package agh.cs.worldSimulation;

/**
 * The interface responsible for managing observers positions in observer pattern.
 */
public interface IPositionChangeObserver {

    /**
     * Update position in maps: GrassField, RectnagularMap
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);

    /**
     * Update position in MapBoundary
     */
    //void positionChanged(Vector2d oldPosition, Vector2d newPosition, String type);
}