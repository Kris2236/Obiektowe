package agh.cs.worldSimulation.App;

import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.elements.plant.Grass;
import agh.cs.worldSimulation.engine.AnimalEngine;
import agh.cs.worldSimulation.engine.IEngine;
import agh.cs.worldSimulation.engine.SimulationEngine;
import agh.cs.worldSimulation.map.IWorldMap;
import agh.cs.worldSimulation.map.Jungle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class SimulationWorldApp extends Application {
    private boolean simulationPaused;
    private boolean markdownGenotype;
    private boolean saveStatistics;
    private final int interval = 200;       // form map bigger than 100x100 interval should be bigger
    private final int maxWidth = 600;
    private final int maxHeight = 800;
    private CanDisplay canDisplay;
    private IEngine engineJungleWorld;
    private IWorldMap jungleMap;
    private JsonParser jsonParser;

    // get from engine
    private int width;
    private int height;


    @Override
    public void init() throws IOException {
        this.canDisplay = new CanDisplay(true);

        // Parsing input data
        jsonParser = new JsonParser();
        jsonParser.jsonParse();

        width = (int) jsonParser.width;
        height = (int) jsonParser.height;

        // run background engine
        Thread background =  new Thread(() -> {
            try{
                AnimalEngine animalEngine = new AnimalEngine(new Jungle(), (int) jsonParser.initialAnimalEnergy,(int)  jsonParser.moveEnergy);
                this.jungleMap = new Jungle( (int) jsonParser.width, (int) jsonParser.height, (int) jsonParser.initialNumberOfGrass,jsonParser.jungleRatio, (int) jsonParser.plantEnergy, animalEngine);
                this.engineJungleWorld = new SimulationEngine(jungleMap, 15, animalEngine, this.canDisplay);
                engineJungleWorld.run(200);
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
        background.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        simulationPaused = false;
        markdownGenotype = false;
        saveStatistics = false;

        Button buttonPause = new Button("Pause");
        Button buttonDominantGenotype = new Button("Dominant genotype");
        Button buttonSaveStatistics = new Button("Save statistics");

        buttonPause.setStyle("-fx-font-size: 1em; ");
        buttonDominantGenotype.setStyle("-fx-font-size: 1em; ");
        buttonSaveStatistics.setStyle("-fx-font-size: 1em; ");

        buttonPause.setOnAction(e -> {
            this.simulationPaused = !simulationPaused;

            if (this.simulationPaused)
                System.out.println("Paused...");
            else
                System.out.println("Simulation is working...");
        });

        buttonDominantGenotype.setOnAction(e -> {
            this.markdownGenotype = !markdownGenotype;

            if (this.markdownGenotype)
                System.out.println("Markdown...");
            else
                System.out.println("Normal Printing...");
        });

        buttonSaveStatistics.setOnAction(e -> {
            this.saveStatistics = true;
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(buttonPause, buttonDominantGenotype, buttonSaveStatistics);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        root.setBottom(hbox);

        while (jungleMap == null){
            Thread.sleep(interval);
        }

        width = jungleMap.getMapSize().x;
        height = jungleMap.getMapSize().y;

        primaryStage.setTitle("Simulation");
        primaryStage.setScene(new Scene(root, width * matchScale() + 20, height * matchScale() + 30 +100 ));
        primaryStage.show();

        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!canDisplay.simulationFinished()){
                    synchronized (this) {
                        while (simulationPaused  || !canDisplay.getState()) {   // waiting for changes
                            try {
                                Thread.sleep(interval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Platform.runLater(new Runnable() {      // Update map and statistics
                            @Override
                            public void run() {
                                try {
                                    root.setTop(updateStatistics());
                                    root.setCenter(updateMap());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        canDisplay.setCanDisplay(false);
                    }
                }
            }
        });
        taskThread.start();
    }

    private GridPane updateStatistics() throws InterruptedException {
        GridPane gp = new GridPane();

        Label labelDay = new Label( engineJungleWorld.getStatistics().getDay());
        Label labelNumberOfAnimals = new Label(engineJungleWorld.getStatistics().getNumberOfAnimals());
        Label labelAvgAnimalEnergy = new Label(engineJungleWorld.getStatistics().getAvgAnimalEnergy());
        Label labelNumberOfGrass = new Label(engineJungleWorld.getStatistics().getNumberOfGrass());
        Label labelAvgLifespan =new Label(engineJungleWorld.getStatistics().getAvgLifespan());
        Label labelAvgNumberOfChildren = new Label(engineJungleWorld.getStatistics().getAvgNumberOfChildren());
        Label labelDominantGenotype = new Label(engineJungleWorld.getStatistics().getDominantGenotype());

        gp.add(labelDay, 0,1);
        gp.add(labelNumberOfAnimals, 0,2);
        gp.add(labelAvgAnimalEnergy, 0,3);
        gp.add(labelNumberOfGrass, 0,4);
        gp.add(labelAvgLifespan, 0,5);
        gp.add(labelAvgNumberOfChildren, 0,6);
        gp.add(labelDominantGenotype, 0,7);

        gp.setPadding(new Insets(5, 0, 5, 100));
        return gp;
    }

    private GridPane updateMap() throws InterruptedException {
        GridPane gp = new GridPane();
        MapApp(gp);
        gp.setGridLinesVisible(true);
        gp.setVgap(0.1);
        gp.setHgap(0.1);
        gp.setPadding(new Insets(0, 0, 20, 0));
        return gp;
    }

    private int matchScale() {
        return Math.max(maxWidth, maxHeight)/Math.max(width,height);
    }

    private void MapApp(GridPane gp) throws InterruptedException {
        LinkedList<Animal> animals = engineJungleWorld.getAnimalEngine().getAnimalsList();
        HashMap<Vector2d, Grass> grass = jungleMap.getGrassEngine().getGrassMap();
        LinkedList<Vector2d> markdownPos = engineJungleWorld.getStatistics().getAnimalsPosWithDominantGenotype();

        // sortuj po energii - optymalizacja - plany na przyszłość :)
        HashMap<Vector2d, Animal> animalsMap = new HashMap<>();
        for(Animal animal:animals)
            animalsMap.put(animal.getPosition(), animal);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                StackPane square = new StackPane();
                String color = typeOfCell(new Vector2d(x,y), animalsMap, grass, markdownPos);
                square.setStyle("-fx-background-color: " + color + ";");
                gp.add(square, x, height-1-y);
            }
        }

        for (int i = 0; i < height; i++) {
            gp.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }

        for (int i = 0; i < width; i++) {
            gp.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
        }
    }

    private String typeOfCell(Vector2d position, HashMap<Vector2d, Animal> animals, HashMap<Vector2d, Grass> grass, LinkedList<Vector2d> markdownPos) throws InterruptedException {
        String color;   // savanna

        if (!jungleMap.isInStep(position))
            color = "#A8D09A";
        else
            color = "#D2D0CA";

        if (animals.containsKey(position)) {   // jeśli jest w liście zwierzaków + life energy
            if (markdownGenotype && markdownPos.contains(position)){            // Markdown
                color = "#0000FF";
            } else if(animals.get(position).getEnergy() > jsonParser.initialAnimalEnergy * 4/5 )   // Full energy
                color = "#70F050";
            else if (animals.get(position).getEnergy() <= jsonParser.initialAnimalEnergy * 4/5 && animals.get(position).getEnergy() > jsonParser.initialAnimalEnergy/3)       // Medium energy
                color = "#FEB100";
            else                    // Low energy
                color = "#EA0606";
        } else if (grass.containsKey(position)) // grass
            color = "#439030";

        return  color;
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        if(saveStatistics){
            WriteToFile writeToFile = new WriteToFile(engineJungleWorld.getStatistics(), "statistics.txt");
            writeToFile.writeToFile();
        }
    }
}