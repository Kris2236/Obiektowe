package agh.cs.worldSimulation.map;

import agh.cs.worldSimulation.engine.AnimalEngine;
import agh.cs.worldSimulation.engine.GrassEngine;
import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;

import java.util.*;

public class Jungle extends AbstractWorldMap implements IWorldMap {
    private final Vector2d mapBoundLower = new Vector2d(0,0);
    private final Vector2d mapBoundUpper;
    private Vector2d jungleBoundLower;
    private Vector2d jungleBoundUpper;
    private final double jungleRatio;
    private final GrassEngine grassEngine;
    private MapWrap mapWrap;
    private AnimalEngine animalEngine;
    private final int initialEnergy;
    private final int moveEnergy;


    public Jungle(int width, int height, int initialNumberOfGrass, double jungleRatio, int plantEnergy, int initialEnergy, int moveEnergy) {
        this.initialEnergy = initialEnergy;
        this.moveEnergy = moveEnergy;
        this.mapBoundUpper = new Vector2d(width-1,height-1);
        this.jungleRatio = jungleRatio;
        createJungle(this.jungleRatio);
        this.grassEngine = new GrassEngine(plantEnergy, this);
        grassEngine.placeGrass(initialNumberOfGrass);
        this.mapWrap = new MapWrap(this);
    }

    public AnimalEngine getAnimalEngine() {
        return this.animalEngine;
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

    @Override
    public Vector2d getMapSize() {
        return this.mapBoundUpper.substract(this.mapBoundLower).add(new Vector2d(1,1));
    }

    public LinkedList<Vector2d> getEmptyPositions() {
        LinkedList<Vector2d> emptyPositions = new LinkedList<>();
        for (int x=mapBoundLower.x; x<=mapBoundUpper.x; x++) {
            for (int y=mapBoundLower.y; y<=mapBoundUpper.y; y++) {
                if (!isOccupied(new Vector2d(x, y)))
                    emptyPositions.add(new Vector2d(x, y));
            }
        }

        return emptyPositions;
    }

    public LinkedList<Vector2d> getEmptyJunglePositions() {
        LinkedList<Vector2d> emptyPositions = getEmptyPositions();
        LinkedList<Vector2d> emptyJunglePositions = new LinkedList<>();
        for (Vector2d position : emptyPositions) {
            if (isInJungle(position))
                emptyJunglePositions.add(position);
        }

        return emptyJunglePositions;
    }

    public LinkedList<Vector2d> getEmptyStepPositions() {
        LinkedList<Vector2d> emptyPositions = getEmptyPositions();
        LinkedList<Vector2d> emptyStepPositions = new LinkedList<>();
        for (Vector2d position : emptyPositions) {
            if (isInStep(position))
                emptyStepPositions.add(position);
        }

        return emptyStepPositions;
    }

    // GrassEngine
    public void addDailyGrass() {
        grassEngine.placeGrass(2);
    }

    public int getEnergyFrom(Vector2d position) {
        return grassEngine.getEnergyFrom(position);
    }

    public int getGrassMapSize() {
        return grassEngine.getGrassMap().size();
    }

    public GrassEngine getGrassEngine() {
        return this.grassEngine;
    }

    //MapWrap
    public Vector2d mapWrap(Animal animal, Vector2d position){
        return mapWrap.wrapEdge(animal, position);
    }

    private boolean isInMap(Vector2d position) {
    return position.precedes(mapBoundUpper) && position.follows(mapBoundLower);
    }

    private boolean isInJungle(Vector2d position) {
        return position.follows(jungleBoundLower) && position.precedes(jungleBoundUpper);
    }

    public boolean isInStep(Vector2d position) {
        return isInMap(position) && !isInJungle(position);
    }

    // AnimalEngine
    public LinkedList<Animal> getAnimalsList() {
        if (this.animalEngine == null)
            this.animalEngine = new AnimalEngine(this, initialEnergy, moveEnergy);

        return this.animalEngine.getAnimalsList();
    }

    @Override
    public LinkedList<Animal> getDeadAnimalsList() {
        return this.animalEngine.getDeadAnimalsList();
    }

    public void reproduce(Vector2d position) {
        this.animalEngine.reproduce(position);
    }

    public void animalDied(Animal animal) {
        animalEngine.animalDied(animal);
    }

    @Override
    public boolean place(Animal animal) {
        return super.place(animal);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return isInMap(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        boolean result = super.isOccupied(position);                // check animals in AbstractWorldMap
        if(result)
            return true;

        return grassEngine.getGrassMap().containsKey(position);     // check grass
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);                   // return Animal object as first - display priority in AbstractWorldMap
        if(object != null)
            return object;

        if(grassEngine.getGrassMap().containsKey(position))         // return Grass object
            return grassEngine.getGrassMap().get(position);

        return null;
    }

    @Override
    public String toString(IWorldMap map) { return super.toString(map); }

    @Override
    public Vector2d lowerLeft() { return mapBoundLower; }

    @Override
    public Vector2d upperRight() { return mapBoundUpper; }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition, newPosition);
    }
}