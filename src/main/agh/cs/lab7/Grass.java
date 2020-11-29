package agh.cs.lab7;

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

    // przydatne gdyby trawa mogła zmieniać pozycje
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
            o.positionChanged(oldPosition, newPosition, "Grass");
        }
    }
}
