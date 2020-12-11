package agh.cs.worldSimulation;

import java.util.ArrayList;

public class Animal implements ISubject {
    protected Vector2d position;
    protected MapDirection direction;
    protected IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();

    public Animal(IWorldMap map){
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String toString(){
        return switch (direction) {
            case NORTH -> "^";
            case EAST -> ">";
            case SOUTH -> "v";
            case WEST -> "<";
            default -> null;
        };
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

        if(map.canMoveTo(pos)){

            notifyObservers(this.position, pos);
            this.position = pos;
        } else if( !(map.objectAt(pos) instanceof Animal) ) {

            pos = map.wrapEdge(this.position, pos, direction);
            // Check if the wrapped position is occupied !!!!

            notifyObservers(this.position, pos);
            this.position = pos;
        }
    }

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