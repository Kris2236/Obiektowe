package agh.cs.worldSimulation.other;

import agh.cs.worldSimulation.data.Vector2d;

import java.util.Comparator;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary {                                              // Plan for future to optimize elements management
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

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        updateAnimal(oldPosition,newPosition);
    }

    private void updateAnimal(Vector2d oldPosition, Vector2d newPosition) {
    }

    public Vector2d getLowerLeft(){ return new Vector2d(sortedByX.first().x, sortedByY.first().y); }

    public Vector2d getUpperRight(){
        return new Vector2d(sortedByX.last().x, sortedByY.last().y);
    }
}
