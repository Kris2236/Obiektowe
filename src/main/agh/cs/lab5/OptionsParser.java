package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    public agh.cs.lab5.MoveDirection[] parse(String[] args){

        List<agh.cs.lab5.MoveDirection> comands = new ArrayList<>();

        for(String arg : args){
            switch (arg){
                case "f", "forward" -> comands.add(agh.cs.lab5.MoveDirection.FORWARD);
                case "b", "backward" -> comands.add(agh.cs.lab5.MoveDirection.BACKWARD);
                case "r", "right" -> comands.add(agh.cs.lab5.MoveDirection.RIGHT);
                case "l", "left" -> comands.add(agh.cs.lab5.MoveDirection.LEFT);
            }
        }
        System.out.println(comands);

        return comands.toArray(new agh.cs.lab5.MoveDirection[0]);
    }
}
