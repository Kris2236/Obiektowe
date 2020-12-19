package agh.cs.worldSimulation.data;

import agh.cs.worldSimulation.data.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    public MoveDirection[] parse(String[] args) throws IllegalArgumentException {
        List<MoveDirection> commands = new ArrayList<>();

        for(String arg : args){
            switch (arg){
                case "0", "f", "forward" -> commands.add(MoveDirection.FORWARD);
                case "1", "fr", "forward-right" -> commands.add(MoveDirection.FORWARD_RIGHT);
                case "2", "r", "right" -> commands.add(MoveDirection.RIGHT);
                case "3", "br", "backward-right" -> commands.add(MoveDirection.BACKWARD_RIGHT);
                case "4", "b", "backward" -> commands.add(MoveDirection.BACKWARD);
                case "5", "bl", "backward-left" -> commands.add(MoveDirection.BACKWARD_LEFT);
                case "6", "l", "left" -> commands.add(MoveDirection.LEFT);
                case "7", "fl", "forward-left" -> commands.add(MoveDirection.FORWARD_LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        System.out.println(commands);

        return commands.toArray(new MoveDirection[0]);
    }
}
