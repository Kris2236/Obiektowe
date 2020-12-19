package agh.cs.worldSimulation.elements.animal;

import agh.cs.worldSimulation.other.IPositionChangeObserver;
import agh.cs.worldSimulation.other.IPositionChangeSubject;
import agh.cs.worldSimulation.map.IWorldMap;
import agh.cs.worldSimulation.data.MapDirection;
import agh.cs.worldSimulation.data.MoveDirection;
import agh.cs.worldSimulation.data.Vector2d;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Animal implements IPositionChangeSubject,Comparable {
    private final LinkedList<IPositionChangeObserver> observerList = new LinkedList<>();
    Set<Animal> children = new HashSet<>();
    int birthDay;
    int deathDay = 0;
    protected MapDirection direction;
    protected Vector2d position;
    protected IWorldMap map;
    protected int lifeEnergy = 1000000000;
    protected Genotype genotype;
    private int moveEnergy = 0;

    public Animal(IWorldMap map){
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, MapDirection InitialDirection){
        this.map = map;
        this.position = initialPosition;
        this.direction = InitialDirection;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int lifeEnergy, Genotype genotype, int moveEnergy, int birthDay){
        this.map = map;
        this.position = initialPosition;
        this.lifeEnergy = lifeEnergy;
        this.moveEnergy = moveEnergy;
        this.genotype = genotype;
        this.birthDay = birthDay;
        this.direction = generateRandomDirection();
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int lifeEnergy, Genotype genotype, int moveEnergy, int birthDay, MapDirection InitialDirection){
        this.map = map;
        this.position = initialPosition;
        this.lifeEnergy = lifeEnergy;
        this.moveEnergy = moveEnergy;
        this.genotype = genotype;
        this.birthDay = birthDay;
        this.direction = InitialDirection;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public MapDirection getDirection(){ return this.direction; }

    public int getBirthDay(){ return this.birthDay; }

    public int getEnergy(){ return this.lifeEnergy; }

    public int getQuaterEnergy(){
        int result = this.lifeEnergy/4;
        this.lifeEnergy -= result;
        return result;
    }

    public Genotype getGenotype(){ return this.genotype; }  // zastąp .genotype getterem

    public int getDeathDay(){ return this.deathDay; }

    public Set<Animal> getChildren(){ return this.children; }

    public void dateOfDeath(int deathDay) {
        this.deathDay = deathDay;
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

    private MapDirection generateRandomDirection() {
        Random generator = new Random();
        int randomNumber = generator.nextInt(8);

        return switch (randomNumber) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber + "is not legal direction. Animal can not generate random direction, 8 possible directions. (AbstractMap)\n");
        };
    }

    public void moveAccordingGenotype() {
        move(parseGeneDirection(genotype.getGeneAccordingGenotype()));
    }

    private MoveDirection parseGeneDirection(int gene) {
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
        boolean makeSmallAimal = false;

        if(map.objectAt(pos) instanceof Animal)   // Check if the wrapped position is occupied by another animal and make small animals ...
            makeSmallAimal = true;

        if(!map.canMoveTo(pos)){
            pos = map.mapWrap(this, pos);
        }

        updatePosition(pos);

        if(this.lifeEnergy <= 0 ) {      // Do it in simulation engine
            this.map.animalDied(this);
            return;
        }

        if(makeSmallAimal) {
            map.reproduce(position);        // reproduce
        }
    }

    private void updatePosition(Vector2d position) {
        this.lifeEnergy = this.lifeEnergy - moveEnergy + map.getEnergyFrom(position);
        notifyObservers(this.position, position);
        this.position = position;
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
            o.positionChanged(oldPosition, newPosition, this);
        }
    }

    @Override
    public int compareTo(Object o) {
        return this.lifeEnergy;
    }
}