package agh.cs.worldSimulation;

import java.util.ArrayList;

public class Grass implements ISubject {
    private final Vector2d position;
    private final ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();

    Grass(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String toString(){
        return "*";
    }

    // It will be useful when grass can change position/ be 'eaten'
    @Override
    public void register(IPositionChangeObserver o) {
        observerList.add(o);
    }

    @Override
    public void unregister(IPositionChangeObserver o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver o : observerList){
            o.positionChanged(oldPosition, newPosition);
        }
    }
}