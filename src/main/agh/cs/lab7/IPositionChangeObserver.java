package agh.cs.lab7;

/**
 * The interface responsible for managing observers positions in observer pattern.
 */
public interface IPositionChangeObserver {

    /**
     * Update position in maps: GrassField, RectnagularMap
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);

    /**
     * Update position in MapBoundary
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, String type);
}