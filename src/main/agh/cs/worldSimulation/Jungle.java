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

    public Jungle(int width, int height, int numberOfGrass) {
        this.boundStepUpper = new Vector2d(width-1,height-1);
        this.numberOfGrass = numberOfGrass;

        createJungle();
        placeGrass(numberOfGrass);
    }

    private void createJungle() {
        int xMapCenter = boundStepUpper.x/2;
        int yMapCenter = boundStepUpper.y/2;
        int xSqrtSideLength = (int) Math.sqrt(boundStepUpper.x + 1);
        int ySqrtSideLength = (int) Math.sqrt(boundStepUpper.y+ 1 );

        if (xSqrtSideLength % 2 == 0) {
            this.boundJungleLower = new Vector2d(xMapCenter - xSqrtSideLength/2 - 1,yMapCenter - ySqrtSideLength/2 - 1);
        } else {
            this.boundJungleLower = new Vector2d(xMapCenter - xSqrtSideLength/2,yMapCenter - ySqrtSideLength/2);
        }
        this.boundJungleUpper = new Vector2d(xMapCenter + xSqrtSideLength/2,yMapCenter + ySqrtSideLength/2);
    }

    public void placeGrass(int numberOfGrassToPlace) {
        if(numberOfGrassToPlace % 2 == 0) {
            placeGrassAt(numberOfGrassToPlace/2, boundJungleLower, boundJungleUpper);
        } else {
            placeGrassAt(numberOfGrassToPlace/2 + 1, boundJungleLower, boundJungleUpper);
        }
        placeGrassAt(numberOfGrassToPlace/2, boundStepLower, boundStepUpper);
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

    public Vector2d wrapEdge(Vector2d oldPosition, Vector2d position, MoveDirection direction) {
        Animal animal = animalsPositionMap.get(oldPosition);

        Vector2d wrappedPosition = switch (animal.toString()) {
            case "^" -> new Vector2d(position.x, boundStepLower.y);     // N
            case ">" -> new Vector2d(boundStepLower.x, position.y);     // E
            case "v" -> new Vector2d(position.x, boundStepUpper.y);     // S
            case "<" -> new Vector2d(boundStepUpper.x, position.y);     // W
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