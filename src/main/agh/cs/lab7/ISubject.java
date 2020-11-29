package agh.cs.lab7;

public interface ISubject {

    void register(IPositionChangeObserver o);
    void unregister(IPositionChangeObserver o);
    void notifyObservers(Vector2d oldPosition, Vector2d newPosition);
}
