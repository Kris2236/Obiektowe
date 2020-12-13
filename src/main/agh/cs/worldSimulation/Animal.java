package agh.cs.worldSimulation;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Animal implements ISubject {
    private final ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();
    protected MapDirection direction;
    protected Vector2d position;
    protected IWorldMap map;
    protected int lifeEnergy;
    protected int[] genotype;

    public Animal(IWorldMap map){
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int lifeEnergy, int[] genotype){
        this.map = map;
        this.position = initialPosition;
        this.lifeEnergy = lifeEnergy;
        if(genotype.length != 32)
            throw new IllegalArgumentException(genotype.length + " is not legal genotype length. Genotype length must be 32 in Animal.\n");
        this.genotype = genotype;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String toString(){
        return switch (direction) {
            case NORTH -> "^";      // 0
            case NORTH_EAST -> "1";
            case EAST -> ">";       // 2
            case SOUTH_EAST -> "3";
            case SOUTH -> "v";      // 4
            case SOUTH_WEST -> "5";
            case WEST -> "<";       // 6
            case NORTH_WEST -> "7";
            default -> null;
        };
    }

    public void moveAccordingGenotype() {
        MoveDirection direction = parseGeneDirection(randomNumberBetween(0,7));
        move(direction);
    }

    private MoveDirection parseGeneDirection(int gene) {
        System.out.println(gene);

        return switch (gene){
            case 0 -> MoveDirection.FORWARD;
            case 1 -> MoveDirection.FORWARD_RIGHT;
            case 2 -> MoveDirection.RIGHT;
            case 3 -> MoveDirection.BACKWARD_RIGHT;
            case 4 -> MoveDirection.BACKWARD;
            case 5 -> MoveDirection.BACKWARD_LEFT;
            case 6 -> MoveDirection.LEFT;
            case 7 -> MoveDirection.FORWARD_LEFT;
            default -> throw new IllegalArgumentException(gene + " is not legal gene. Animal can not move, there is only 8 possible directions.");
        };
    }

    public void move(MoveDirection direction) {
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

    private int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
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