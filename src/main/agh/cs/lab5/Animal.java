package agh.cs.lab5;

public class Animal{
    protected Vector2d position;
    protected agh.cs.lab5.MapDirection direction;
    protected agh.cs.lab5.IWorldMap mapCurrentWorld;

    public Animal(agh.cs.lab5.IWorldMap map){
        this.mapCurrentWorld = map;
    }

    public Animal(agh.cs.lab5.IWorldMap map, Vector2d initialPosition){
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

    public void move(agh.cs.lab5.MoveDirection direction) {
        switch (direction){
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            default -> changeDirection(direction);
        }
    }

    private void changeDirection(agh.cs.lab5.MoveDirection direction){
        Vector2d pos = switch (direction){
            case FORWARD -> this.position.add(this.direction.toUnitVector());
            case BACKWARD -> this.position.substract(this.direction.toUnitVector());
            default -> position;
        };

        if(mapCurrentWorld.canMoveTo(pos)){
            this.position = pos;
        }
    }
}
