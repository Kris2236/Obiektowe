package agh.cs.lab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class Animal implements Subject{
    protected Vector2d position;
    protected MapDirection direction;
    protected IWorldMap mapCurrentWorld;
    private ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();


    public Animal(IWorldMap map){
        this.mapCurrentWorld = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.mapCurrentWorld = map;
        this.position = initialPosition;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String toString(){
        switch(direction){
            case NORTH: return "^";
            case EAST: return ">";
            case SOUTH: return "v";
            case WEST: return "<";
            default: return null;
        }
    }

    public void move(MoveDirection direction) {
        switch (direction){
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            default -> changeDirection(direction);
        }
    }

    private void changeDirection(MoveDirection direction){
        Vector2d pos = switch (direction){
            case FORWARD -> this.position.add(this.direction.toUnitVector());
            case BACKWARD -> this.position.substract(this.direction.toUnitVector());
            default -> position;
        };

        if(mapCurrentWorld.canMoveTo(pos)){
            // notify observers
            notifyObservers(this.position, pos);
            this.position = pos;
        }
    }

    // set visibility !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    void addObserver(IPositionChangeObserver observer){
//        observerList.add(observer);
//    }
//
//    void removeObserver(IPositionChangeObserver observer){
//        observerList.remove(observer);
//    }
//
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        for (Subject o : observerList){
//            //o.update(Vector2d oldPosition, Vector2d newPosition);
//            // other positionChanged function
//            o.notifyObservers(oldPosition, newPosition);
//        }
//    }

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
