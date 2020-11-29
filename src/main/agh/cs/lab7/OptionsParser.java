package agh.cs.lab7;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    public MoveDirection[] parse(String[] args) throws IllegalArgumentException {
        List<MoveDirection> comands = new ArrayList<>();

        for(String arg : args){
            switch (arg){
                case "f", "forward" -> comands.add(MoveDirection.FORWARD);
                case "b", "backward" -> comands.add(MoveDirection.BACKWARD);
                case "r", "right" -> comands.add(MoveDirection.RIGHT);
                case "l", "left" -> comands.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        System.out.println(comands);

        return comands.toArray(new MoveDirection[0]);
    }
}
