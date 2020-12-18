package agh.cs.worldSimulation;

import agh.cs.worldSimulation.data.Vector2d;

public class MapWrap {
    private final IWorldMap map;
    private final Vector2d vX = new Vector2d(1,0);
    private final Vector2d vY = new Vector2d(0,1);

    public MapWrap(IWorldMap map) {
        this.map = map;
    }

    public Vector2d wrapEdge(Animal animal, Vector2d position) {

//        Vector2d wrappedPosition = switch (animal.direction) {
//            case MapDirection.NORTH -> northWrap(position);
//            case MapDirection.NORTH_EAST -> northEastWrap(animal.getPosition());
//            case MapDirection.EAST -> eastWrap(position);
//            case MapDirection.SOUTH_EAST -> southEastWrap(animal.getPosition());
//            case MapDirection.SOUTH -> southWrap(position);
//            case MapDirection.SOUTH_WEST -> southWestWrap(animal.getPosition());
//            case MapDirection.WEST -> westWrap(position);
//            case MapDirection.NORTH_WEST -> northWestWrap(animal.getPosition());
//            default -> null;
//        };

        Vector2d wrappedPosition;
        if (animal.direction.equals(MapDirection.NORTH))
            wrappedPosition = northWrap(position);
        else if (animal.direction.equals(MapDirection.NORTH_EAST))
            wrappedPosition = northEastWrap(animal.getPosition());
        else if (animal.direction.equals(MapDirection.EAST))
            wrappedPosition = eastWrap(position);
        else if (animal.direction.equals(MapDirection.SOUTH_EAST))
            wrappedPosition = southEastWrap(animal.getPosition());
        else if (animal.direction.equals(MapDirection.SOUTH))
            wrappedPosition = southWrap(position);
        else if (animal.direction.equals(MapDirection.SOUTH_WEST))
            wrappedPosition = southWestWrap(animal.getPosition());
        else if (animal.direction.equals(MapDirection.WEST))
            wrappedPosition = westWrap(position);
        else if (animal.direction.equals(MapDirection.NORTH_WEST))
            wrappedPosition = northWestWrap(animal.getPosition());
        else
            throw new IllegalArgumentException("Animal direction is not legal.");

        return wrappedPosition;
    }


    // Optymalizacja
    private Vector2d northWrap(Vector2d position) {
        if(map.canMoveTo(position.add(vY)))
            return position.add(vY);
        return new Vector2d(position.x, map.lowerLeft().y);
    }

    private Vector2d northEastWrap(Vector2d position) {
        if(map.canMoveTo(northWrap(position).add(vX)))
            return northWrap(position).add(vX);
        return eastWrap(northWrap(position));
    }

    private Vector2d eastWrap(Vector2d position) {
        if(map.canMoveTo(position.add(vX)))
            return position.add(vX);
        return new Vector2d(map.lowerLeft().x, position.y);
    }

    private Vector2d southEastWrap(Vector2d position) {
        if(map.canMoveTo(southWrap(position).add(vX)))
            return southWrap(position).add(vX);
        return eastWrap(southWrap(position));
    }

    private Vector2d southWrap(Vector2d position) {
        if(map.canMoveTo(position.substract(vY)))
            return position.substract(vY);
        return new Vector2d(position.x, map.upperRight().y);
    }

    private Vector2d southWestWrap(Vector2d position) {
        if(map.canMoveTo(southWrap(position).substract(vX)))
            return southWrap(position).substract(vX);
        return westWrap(southWrap(position));
    }

    private Vector2d westWrap(Vector2d position) {
        if(map.canMoveTo(position.substract(vX)))
            return position.substract(vX);
        return new Vector2d(map.upperRight().x, position.y);
    }

    private Vector2d northWestWrap(Vector2d position) {
        if(map.canMoveTo(northWrap(position).substract(vX)))
            return northWrap(position).substract(vX);
        return westWrap(northWrap(position));
    }
}
