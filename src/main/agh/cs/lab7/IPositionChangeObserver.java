package agh.cs.lab7;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);   // update position
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, String type);   // update position in MapBoundary
}