package agh.cs.worldSimulation;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    public MoveDirection[] parse(String[] args) throws IllegalArgumentException {
        List<MoveDirection> commands = new ArrayList<>();

        for(String arg : args){
            switch (arg){
                case "f", "forward" -> commands.add(MoveDirection.FORWARD);
                case "b", "backward" -> commands.add(MoveDirection.BACKWARD);
                case "r", "right" -> commands.add(MoveDirection.RIGHT);
                case "l", "left" -> commands.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        System.out.println(commands);

        return commands.toArray(new MoveDirection[0]);
    }
}
