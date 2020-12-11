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
            case NORTH_EAST -> "1";
            case EAST -> ">";
            case SOUTH_EAST -> "3";
            case SOUTH -> "v";
            case SOUTH_WEST -> "5";
            case WEST -> "<";
            case NORTH_WEST -> "7";
            default -> null;
        };
    }

    public void move(MoveDirection direction) {
        // If animal decided to change direction
        switch (direction){
            case FORWARD -> this.direction = this.direction;
            case FORWARD_RIGHT -> this.direction = this.direction.next();
            case RIGHT -> this.direction = this.direction.next().next();
            case BACKWARD_RIGHT -> this.direction = this.direction.next().next().next();
            case BACKWARD -> this.direction = this.direction.next().next().next().next();
            case BACKWARD_LEFT -> this.direction = this.direction.previous().previous().previous();
            case LEFT -> this.direction = this.direction.previous().previous();
            case FORWARD_LEFT -> this.direction = this.direction.previous();
        }

        makeMove(direction);
    }

    private void makeMove(MoveDirection direction){
        Vector2d pos = this.position.add(this.direction.toUnitVector());

        if(map.canMoveTo(pos)){
            notifyObservers(this.position, pos);
            this.position = pos;
        } else if(!(map.objectAt(pos) instanceof Animal)) {
            pos = map.wrapEdge(this.position, pos, direction);
            // Check if the wrapped position is occupied by another animal and make small animals

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