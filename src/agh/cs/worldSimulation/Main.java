package agh.cs.worldSimulation;

import agh.cs.worldSimulation.App.SimulationWorldApp;
import javafx.application.Application;


public class Main {
    public static void main(String[] args) {

        Application.launch(SimulationWorldApp.class);

//        try{
//            MoveDirection[] directions = {};
//
//            AnimalEngine animalEngine = new AnimalEngine(new Jungle(), canDisplay, 100, 10);
//            IWorldMap jungleMap = new Jungle(4,2,0,0.5, 25, animalEngine, canDisplay);
//
//            IEngine engineJungleWorld = new SimulationEngine(directions, jungleMap, 2, animalEngine, canDisplay);
//            engineJungleWorld.run(20);
//        } catch (IllegalArgumentException | InterruptedException e) {
//            System.out.println(e.getMessage());
//        }

    }
}
