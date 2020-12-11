package agh.cs.worldSimulation;

import java.util.HashMap;
import java.util.Random;

public class Jungle extends AbstractWorldMap implements IWorldMap {
    private final Vector2d boundStepLower = new Vector2d(0,0);
    private final Vector2d boundStepUpper;
    private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
    private final HashMap<Vector2d, Animal> animalsPositionMap = new HashMap<>();
    private final int numberOfGrass;
    private Vector2d boundJungleLower;
    private Vector2d boundJungleUpper;
    private Vector2d mapCenter;


    public Jungle(int width, int height, int numberOfGrass) {
        this.boundStepUpper = new Vector2d(width-1,height-1);
        this.numberOfGrass = numberOfGrass;
        createJungle();
        placeGrass(numberOfGrass);
    }

    private void createJungle() {
        // it will be parameter !!!
        int xSqrtSideLength = (int) Math.sqrt(boundStepUpper.x + 1);
        int ySqrtSideLength = (int) Math.sqrt(boundStepUpper.y+ 1 );
        this.mapCenter = new Vector2d(boundStepUpper.x/2,boundStepUpper.y/2);
        this.boundJungleUpper = new Vector2d(mapCenter.x + xSqrtSideLength/2,mapCenter.y + ySqrtSideLength/2);

        if (xSqrtSideLength % 2 == 0)
            this.boundJungleLower = new Vector2d(mapCenter.x - xSqrtSideLength/2 - 1,mapCenter.y - ySqrtSideLength/2 - 1);
        else
            this.boundJungleLower = new Vector2d(mapCenter.x - xSqrtSideLength/2,mapCenter.y - ySqrtSideLength/2);
    }

    public void placeGrass(int numberOfGrassToPlace) {
        placeGrassAt(numberOfGrassToPlace/2, boundStepLower, boundStepUpper);

        if(numberOfGrassToPlace % 2 == 0)
            placeGrassAt(numberOfGrassToPlace/2, boundJungleLower, boundJungleUpper);
        else
            placeGrassAt(numberOfGrassToPlace/2 + 1, boundJungleLower, boundJungleUpper);
    }

    private void placeGrassAt(int numberOfGrassToPlace,Vector2d boundFieldLower ,Vector2d boundFieldUpper) {
        for(int i=0; i<numberOfGrassToPlace; i++) {
            Random generator = new Random();

            int x = generator.nextInt(boundFieldUpper.x - boundFieldLower.x + 1) + boundFieldLower.x;
            int y = generator.nextInt(boundFieldUpper.y - boundFieldLower.y + 1) + boundFieldLower.y;
            boolean uniquePosition = true;
            Grass newGrass = new Grass(new Vector2d(x,y));

            // check if new random grass position equals other existing or animal
            if(grassPositionMap.containsKey(newGrass.getPosition()) ||
                    animalsPositionMap.containsKey(newGrass.getPosition())) {
                i--;
                uniquePosition = false;
            }

            if(uniquePosition) {
                grassPositionMap.put(newGrass.getPosition(), newGrass);
                newGrass.register(this);
                positionGrassAdd(newGrass.getPosition());
            }
        }
    }

    private Vector2d northWrap(Vector2d position) {
        return new Vector2d(position.x, boundStepLower.y);
    }

    private Vector2d northEastWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).add(new Vector2d(1,0))))
            return northWrap(position).add(new Vector2d(1,0));
        return eastWrap(northWrap(position));
    }

    private Vector2d eastWrap(Vector2d position) {
        return new Vector2d(boundStepLower.x, position.y);
    }

    private Vector2d southEastWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).add(new Vector2d(1,0))))
            return southWrap(position).add(new Vector2d(1,0));
        return eastWrap(southWrap(position));
    }

    private Vector2d southWrap(Vector2d position) {
        return new Vector2d(position.x, boundStepUpper.y);
    }

    private Vector2d southWestWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).substract(new Vector2d(1,0))))
            return southWrap(position).substract(new Vector2d(1,0));
        return westWrap(southWrap(position));
    }

    private Vector2d westWrap(Vector2d position) {
        return new Vector2d(boundStepUpper.x, position.y);
    }

    private Vector2d northWestWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).substract(new Vector2d(1,0))))
            return northWrap(position).substract(new Vector2d(1,0));
        return westWrap(northWrap(position));
    }

    public Vector2d wrapEdge(Vector2d oldPosition, Vector2d position, MoveDirection direction) {
        Animal animal = animalsPositionMap.get(oldPosition);

        Vector2d wrappedPosition = switch (animal.toString()) {
            case "^" -> northWrap(position);
            case "1" -> northEastWrap(position);
            case ">" -> eastWrap(position);
            case "3" -> southEastWrap(position);
            case "v" -> southWrap(position);
            case "5" -> southWestWrap(position);
            case "<" -> westWrap(position);
            case "7" -> northWestWrap(position);
            default -> null;
        };
        System.out.println(position + " " + animal.toString() + " " + wrappedPosition);

        return wrappedPosition;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(objectAt(position) instanceof Grass) {
            grassPositionMap.remove(position);
            positionGrassDied(position);
            // unregister + tests
        }

        return !isOccupied(position) && position.precedes(boundStepUpper) && position.follows(boundStepLower);
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {

        // check animals in AbstractWorldMap
        boolean result = super.isOccupied(position);

        if(result) {
            return true;
        }

        // check grass
        return grassPositionMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {

        // return Animal object as first - display priority in AbstractWorldMap
        Object object = super.objectAt(position);

        if(object != null) {
            return object;
        }

        // return Grass object
        if(grassPositionMap.containsKey(position)) {
            return grassPositionMap.get(position);
        }

        // return no object
        return null;
    }

    @Override
    public String toString(IWorldMap map) {
        return super.toString(map);
    }

    @Override
    public Vector2d lowerLeft() {
        return boundStepLower;
    }

    @Override
    public Vector2d upperRight() {
        return boundStepUpper;
    }

    @Override
    public HashMap<Vector2d,Animal> getAnimalsHashMap() {
        return this.animalsPositionMap;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}