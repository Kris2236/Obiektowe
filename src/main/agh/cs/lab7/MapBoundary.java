package agh.cs.lab7;

import java.util.Comparator;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {
    private final HashSet<Vector2d> grassPositions = new HashSet<>();

    SortedSet<Vector2d> sortedByX = new TreeSet<Vector2d>(new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if(o1.x == o2.x){
                return o1.y - o2.y;
            }

            return o1.x - o2.x;
        }
    });

    SortedSet<Vector2d> sortedByY = new TreeSet<Vector2d>(new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if(o1.y == o2.y){
                return o1.x - o2.x;
            }

            return o1.y - o2.y;
        }
    });

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, String type) {

        // dodajemy obserwatorów do map boundry

        if(type == "Grass"){        // grass can be only added - no "eating" grasss by animal
            grassPositions.add(newPosition);
            sortedByX.add(newPosition);
            sortedByY.add(newPosition);
        } else {
            if(grassPositions.contains(oldPosition)){   // (Animal and Grass) 2 objects on old positioin
                sortedByX.add(newPosition);
                sortedByX.add(newPosition);
            } else {

                // Remove old animal position in both sets
                sortedByX.remove(oldPosition);
                sortedByY.remove(oldPosition);

                // Add new position
                sortedByX.add(newPosition);
                sortedByX.add(newPosition);
            }
        }

        for(Vector2d grass : grassPositions){
            System.out.println(grass);
        }
    }
}
