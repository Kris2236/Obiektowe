
import agh.cs.worldSimulation.App.CanDisplay;
import agh.cs.worldSimulation.App.SimulationWorldApp;
import agh.cs.worldSimulation.data.MoveDirection;
import agh.cs.worldSimulation.data.OptionsParser;
import agh.cs.worldSimulation.engine.AnimalEngine;
import agh.cs.worldSimulation.engine.IEngine;
import agh.cs.worldSimulation.engine.SimulationEngine;
import agh.cs.worldSimulation.map.IWorldMap;
import agh.cs.worldSimulation.map.Jungle;
import javafx.application.Application;


public class Main {
    public static void main(String[] args) {
        CanDisplay canDisplay = new CanDisplay(true);


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
