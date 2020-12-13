package agh.cs.worldSimulation;

import java.util.*;

public class Jungle extends AbstractWorldMap implements IWorldMap {
    private final Vector2d mapBoundLower = new Vector2d(0,0);
    private final Vector2d mapBoundUpper;
    private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
    private final HashMap<Vector2d, Animal> animalsPositionMap = new HashMap<>();
    private Vector2d jungleBoundLower;
    private Vector2d jungleBoundUpper;
    private final double jungleRatio;          // number 0.0 ... 1.0


    public Jungle(int width, int height, int initialNumberOfGrass, double jungleRatio) {
        this.mapBoundUpper = new Vector2d(width-1,height-1);
        this.jungleRatio = jungleRatio;
        createJungle(this.jungleRatio);
        placeGrass(initialNumberOfGrass);
    }

    private void createJungle(double jungleRatio) {
        if(jungleRatio <= 0 || jungleRatio > 1)
            throw new IllegalArgumentException(jungleRatio + " is not legal. JungleRatio have to be in range (0.0 < jungleRatio <= 1.0).\n");

        int x = (int) (mapBoundUpper.x*jungleRatio);
        int y = (int) (mapBoundUpper.y*jungleRatio);

        if(jungleRatio != 0) {          // Set min jungle size
            if(x == 0)
                x = 1;
            if(y == 0)
                y = 1;
        }

        Vector2d jungleDimensions = new Vector2d(x,y);
        Vector2d mapCenter = new Vector2d(mapBoundUpper.x/2,mapBoundUpper.y/2);

        if(jungleRatio == 1) {          // Always the map must be divided into 2 types (jungle and step)
            this.jungleBoundUpper = mapBoundUpper.substract(new Vector2d(1,1));
            this.jungleBoundLower = mapBoundLower.add(new Vector2d(1,1));
        } else {
            this.jungleBoundUpper = new Vector2d(mapCenter.x + jungleDimensions.x/2,mapCenter.y + jungleDimensions.y/2);
            this.jungleBoundLower = new Vector2d(mapCenter.x - jungleDimensions.x/2,mapCenter.y - jungleDimensions.y/2);
        }
    }

    private void printJungle(){
        for(int y=0; y<=mapBoundUpper.y; y++){
            for(int x=0; x<=mapBoundUpper.x; x++){
                if(isInJungle(new Vector2d(x,y)))
                    System.out.print("T");
                else
                    System.out.print("F");
            }
            System.out.println();
        }
    }

    public void placeGrass(int numberOfGrassToPlace) {
        placeGrassAtStep(numberOfGrassToPlace/2, mapBoundLower, mapBoundUpper);

        if(numberOfGrassToPlace % 2 == 0)
            placeGrassAt(numberOfGrassToPlace/2, jungleBoundLower, jungleBoundUpper);
        else
            placeGrassAt(numberOfGrassToPlace/2 + 1, jungleBoundLower, jungleBoundUpper);
    }

    private void placeGrassAt(int numberOfGrassToPlace,Vector2d boundFieldLower ,Vector2d boundFieldUpper) {
        int counterFailedTrial = 0;
        Grass newGrass;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            boolean uniquePosition = true;
            newGrass = new Grass(generateRandomPositionAt(boundFieldLower, boundFieldUpper));

            if(counterFailedTrial > 2 * numberOfGrassToPlace) {       // Check if are there any empty positions !!!! you can check how many position in field are empty
                Vector2d[] emptyPositions = emptyPositionsAt(boundFieldLower, boundFieldUpper);
                if(emptyPositions.length == 0)
                    return;                     // stop function - all positions are occupied
                else
                    newGrass = new Grass(emptyPositions[generateRandomNumber(0, emptyPositions.length-1)]);     // Choose random empty position

            } else if(isOccupied(newGrass.getPosition())) {
                i--;
                uniquePosition = false;
                counterFailedTrial++;
            }

            if(uniquePosition)
                addGrass(newGrass);
        }
    }

    private Vector2d[] emptyPositionsAt(Vector2d boundFieldLower, Vector2d boundFieldUpper) {
        List<Vector2d> emptyPositions = new ArrayList<>();

        for(int x=boundFieldLower.x; x<=boundFieldUpper.x; x++) {
            for(int y=boundFieldLower.y; y<=boundFieldUpper.y; y++) {
                if(!isOccupied(new Vector2d(x,y)))
                    emptyPositions.add(new Vector2d(x,y));
            }
        }

        return emptyPositions.toArray(new Vector2d[0]);
    }

    private void addGrass(Grass newGrass) {
        grassPositionMap.put(newGrass.getPosition(), newGrass);
        newGrass.register(this);
        positionGrassAdd(newGrass.getPosition());
    }

    private boolean isInJungle(Vector2d position) {
        return position.follows(jungleBoundLower) && position.precedes(jungleBoundUpper);
    }

    private boolean isInStep(Vector2d position) {
        return position.follows(mapBoundLower) && position.precedes(mapBoundUpper) && !isInJungle(position);
    }

    private void placeGrassAtStep(int numberOfGrassToPlace,Vector2d boundFieldLower ,Vector2d boundFieldUpper) {
        Grass newGrass;
        int counterFailedTrial = 0;

        for(int i=0; i<numberOfGrassToPlace; i++) {
            newGrass = new Grass(generateRandomPositionAt(boundFieldLower, boundFieldUpper));

            // Do zoptymalizowania!!!
            while(isOccupied(newGrass.getPosition()) || !isInStep(newGrass.getPosition())){
                counterFailedTrial++;
                newGrass = new Grass(generateRandomPositionAt(boundFieldLower, boundFieldUpper));
            }

            // Będzie warotść informująca ile procent mapt jest zajęte !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if(counterFailedTrial > 10*numberOfGrassToPlace)    // jeśli 100% mapy jest zajęte
                return;

            addGrass(newGrass);
        }
    }

    private Vector2d generateRandomPositionAt(Vector2d boundFieldLower, Vector2d boundFieldUpper) {
        Random generator = new Random();
        int x = generator.nextInt(boundFieldUpper.x - boundFieldLower.x + 1) + boundFieldLower.x;
        int y = generator.nextInt(boundFieldUpper.y - boundFieldLower.y + 1) + boundFieldLower.y;
        return new Vector2d(x,y);
    }

    private int generateRandomNumber(int min, int max) {
        Random generator = new Random();
        return generator.nextInt(max - min + 1) + min;
    }

    @Override
    public ArrayList<Animal> generateAnimals(int numberOfAnimalsToAdd) {
        int counterFailedTrial = 0;
        Vector2d boundFieldLower = mapBoundLower;
        Vector2d boundFieldUpper = mapBoundUpper;
        ArrayList<Animal> animalsList = new ArrayList<>();

        for(int i=0; i<numberOfAnimalsToAdd; i++) {
            boolean uniquePosition = true;

            if(counterFailedTrial > numberOfAnimalsToAdd) {         // Enlarge the adding area
                if(!boundFieldLower.precedes(mapBoundLower))
                    boundFieldLower.substract(new Vector2d(1,1));
                if(boundFieldUpper.follows(mapBoundUpper))
                    boundFieldUpper.add(new Vector2d(1,1));
            }

            Animal newAnimal = new Animal(this, generateRandomPositionAt(boundFieldLower, boundFieldUpper));

            if(animalsPositionMap.containsKey(newAnimal.position)) {        // Check if new random animal position equals other existing
                i--;
                counterFailedTrial++;
                uniquePosition = false;
            }

            if(grassPositionMap.containsKey(newAnimal.position)) {      // Move grass if is collision
                placeGrassAt(1, mapBoundLower, mapBoundUpper);        // Add grass in all field because animals are adding in savanna
                grassPositionMap.remove(newAnimal.position);
                positionGrassDied(newAnimal.position);
            }

            if(uniquePosition) {
                this.place(newAnimal);
                animalsList.add(newAnimal);
            }
        }

        return animalsList;
    }


    private final Vector2d vX = new Vector2d(1,0);
    private final Vector2d vY = new Vector2d(0,1);

    private Vector2d northWrap(Vector2d position) {
        if(canMoveTo(position.add(vY)))
            return position.add(vY);
        return new Vector2d(position.x, mapBoundLower.y);
    }

    private Vector2d northEastWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).add(vX)))
            return northWrap(position).add(vX);
        return eastWrap(northWrap(position));
    }

    private Vector2d eastWrap(Vector2d position) {
        if(canMoveTo(position.add(vX)))
            return position.add(vX);
        return new Vector2d(mapBoundLower.x, position.y);
    }

    private Vector2d southEastWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).add(vX)))
            return southWrap(position).add(vX);
        return eastWrap(southWrap(position));
    }

    private Vector2d southWrap(Vector2d position) {
        if(canMoveTo(position.substract(vY)))
            return position.substract(vY);
        return new Vector2d(position.x, mapBoundUpper.y);
    }

    private Vector2d southWestWrap(Vector2d position) {
        if(canMoveTo(southWrap(position).substract(vX)))
            return southWrap(position).substract(vX);
        return westWrap(southWrap(position));
    }

    private Vector2d westWrap(Vector2d position) {
        if(canMoveTo(position.substract(vX)))
            return position.substract(vX);
        return new Vector2d(mapBoundUpper.x, position.y);
    }

    private Vector2d northWestWrap(Vector2d position) {
        if(canMoveTo(northWrap(position).substract(vX)))
            return northWrap(position).substract(vX);
        return westWrap(northWrap(position));
    }

    public Vector2d wrapEdge(Vector2d oldPosition, Vector2d position, MoveDirection direction) {
        Animal animal = animalsPositionMap.get(oldPosition);

        Vector2d wrappedPosition = switch (animal.toString()) {
            case "^" -> northWrap(position);
            case "1" -> northEastWrap(oldPosition);
            case ">" -> eastWrap(position);
            case "3" -> southEastWrap(oldPosition);
            case "v" -> southWrap(position);
            case "5" -> southWestWrap(oldPosition);
            case "<" -> westWrap(position);
            case "7" -> northWestWrap(oldPosition);
            default -> null;
        };

        return wrappedPosition;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(objectAt(position) instanceof Grass) {
            grassPositionMap.remove(position);
            positionGrassDied(position);
        }
        return !isOccupied(position) && position.precedes(mapBoundUpper) && position.follows(mapBoundLower);
    }

    @Override
    public boolean place(Animal animal) { return super.place(animal); }

    public boolean placeWithDirection(Animal animal, MapDirection direction) {
        if(super.place(animal)) {
            animal.direction = direction;
            return true;
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        boolean result = super.isOccupied(position);        // check animals in AbstractWorldMap

        if(result) {
            return true;
        }

        return grassPositionMap.containsKey(position);      // check grass
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);        // return Animal object as first - display priority in AbstractWorldMap

        if(object != null) {
            return object;
        }

        if(grassPositionMap.containsKey(position)) {        // return Grass object
            return grassPositionMap.get(position);
        }

        return null;        // return no object
    }

    @Override
    public String toString(IWorldMap map) { return super.toString(map); }

    @Override
    public Vector2d lowerLeft() { return mapBoundLower; }

    @Override
    public Vector2d upperRight() { return mapBoundUpper; }

    @Override
    public HashMap<Vector2d,Animal> getAnimalsHashMap() { return this.animalsPositionMap; }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}