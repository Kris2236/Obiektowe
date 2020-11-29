package agh.cs.lab7;

import java.util.Comparator;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {
    private final HashSet<Vector2d> grassPositions = new HashSet<>();

    SortedSet<Vector2d> sortedByX = new TreeSet<>(new Comparator<>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if (o1.x == o2.x) {
                return o1.y - o2.y;
            }

            return o1.x - o2.x;
        }
    });

    SortedSet<Vector2d> sortedByY = new TreeSet<>(new Comparator<>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if (o1.y == o2.y) {
                return o1.x - o2.x;
            }

            return o1.y - o2.y;
        }
    });

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {}

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, String type) {
        if(type.equals("Animal")){
            updateAnimal(oldPosition, newPosition);
        } else if(type.equals("Grass")) {
            updateGrass(oldPosition, newPosition);
        }
    }

    private void updateGrass(Vector2d oldPosition, Vector2d newPosition) {

        // grass can be only added - no "eating" grass by animal
        grassPositions.add(newPosition);
        sortedByX.add(newPosition);
        sortedByY.add(newPosition);
    }

    private void updateAnimal(Vector2d oldPosition, Vector2d newPosition) {
        if(grassPositions.contains(oldPosition)){

            // (Animal and Grass) 2 objects on old position
            sortedByX.add(newPosition);
            sortedByY.add(newPosition);
        } else {

            // Remove old animal position in both sets
            sortedByX.remove(oldPosition);
            sortedByY.remove(oldPosition);

            // Add new position
            sortedByX.add(newPosition);
            sortedByY.add(newPosition);
        }
    }

    public Vector2d getLowerLeft(){
        return new Vector2d(sortedByX.first().x, sortedByY.first().y);
    }

    public Vector2d getUpperRight(){
        return new Vector2d(sortedByX.last().x, sortedByY.last().y);
    }
}
