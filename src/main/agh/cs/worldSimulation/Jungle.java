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

    private void placeGrass(int numberOfGrassToPlace) {
        for(int i=0; i<numberOfGrassToPlace; i++) {
            Random generator = new Random();

            int x = generator.nextInt(boundStepUpper.x + 1);
            int y = generator.nextInt(boundStepUpper.y + 1);
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

    public Vector2d wrapEdge(Vector2d position) {
        Vector2d wrappedPosition;

        // All 8 possible wrapping
        if(position.x > boundStepUpper.x && position.y < boundStepUpper.y && position.y > boundStepLower.y) {
            wrappedPosition = new Vector2d(boundStepLower.x, position.y);
        } else if(position.x < boundStepLower.x && position.y < boundStepUpper.y && position.y > boundStepLower.y) {
            wrappedPosition = new Vector2d(boundStepUpper.x, position.y);
        } else if(position.y > boundStepUpper.y && position.x < boundStepUpper.x && position.x > boundStepLower.x) {
            wrappedPosition = new Vector2d(position.x, boundStepLower.y);
        } else if(position.y < boundStepLower.y && position.x < boundStepUpper.x && position.x > boundStepLower.x) {
            wrappedPosition = new Vector2d(position.x, boundStepUpper.y);
        } else if(position.x > boundStepUpper.x && position.y > boundStepUpper.y) {
            wrappedPosition = boundStepLower;
        } else if(position.x < boundStepLower.x && position.y < boundStepLower.y) {
            wrappedPosition = boundStepUpper;
        } else if(position.x > boundStepUpper.x && position.y < boundStepLower.y) {
            wrappedPosition = new Vector2d(boundStepLower.x, boundStepUpper.y);
        } else if(position.x < boundStepLower.x && position.y > boundStepUpper.y) {
            wrappedPosition = new Vector2d(boundStepUpper.x, boundStepLower.y);
        } else {
                return null;
        }

        return wrappedPosition;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(objectAt(position) instanceof Grass) {
            grassPositionMap.remove(position);
            positionGrassDied(position);
            // unregister + tests
        }

        return !isOccupied(position) && position.precedes(boundStepUpper) || !position.follows(boundStepLower);
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