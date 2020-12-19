package agh.cs.worldSimulation.elements.plant;

import agh.cs.worldSimulation.other.IPositionChangeObserver;
import agh.cs.worldSimulation.other.IPositionChangeSubject;
import agh.cs.worldSimulation.data.Vector2d;

import java.util.ArrayList;

public class Grass implements IPositionChangeSubject {
    private final ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();  // ma być ILifeCycleObserver
    private final Vector2d position;
    private int plantEnergy;        // In future we can for each plant generate random energy

    public Grass(Vector2d position, int plantEnergy){
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public int getEnergy() {return this.plantEnergy; }  // ma informować obserwatorów o usunięciu obiektu + zwracać energię

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
            //o.positionChanged(oldPosition, newPosition, this);
        }
    }
}