package agh.cs.worldSimulation.App;

import agh.cs.worldSimulation.data.Vector2d;
import agh.cs.worldSimulation.elements.animal.Animal;
import agh.cs.worldSimulation.elements.plant.Grass;
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
    private boolean followStrongestPet;
    private Animal followedAnimal;
    private int interval = 100;       // form map bigger than 100x100 interval should be bigger
    private final int windowMaxWidth = 600;
    private final int windowMaxHeight = 800;
    private CanDisplay canDisplay;
    private IEngine engineJungleWorld;
    private IWorldMap jungleMap;
    private JsonParser jsonParser;

    // get from engine
    private int width;
    private int height;


    @Override
    public void init() throws IOException {
        // Parsing input data
        // Important information : you can not add to many grass according to map dimension
        jsonParser = new JsonParser();
        jsonParser.jsonParse();

        width = (int) jsonParser.width;
        height = (int) jsonParser.height;

        // run background engine
        this.canDisplay = new CanDisplay(false);
        Thread background;
        background =  new Thread(() -> {
            try{
                this.jungleMap = new Jungle( (int) jsonParser.width, (int) jsonParser.height, (int) jsonParser.initialNumberOfGrass,jsonParser.jungleRatio, (int) jsonParser.plantEnergy,(int) jsonParser.initialAnimalEnergy,(int)  jsonParser.moveEnergy);
                this.engineJungleWorld = new SimulationEngine(jungleMap, this.canDisplay);
                engineJungleWorld.setAnimalEngine(jungleMap.getAnimalEngine(), (int) jsonParser.initialAnimalCount);
                engineJungleWorld.run((int) jsonParser.maxNumberOfDays);
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
        followStrongestPet = false;

        Button buttonPause = new Button("(Start/Pause)");
        Button buttonDominantGenotype = new Button("(Mark/Unmark) Dominant genotype");
        Button buttonSaveStatistics = new Button("Save statistics");
        Button buttonFollowStrongestPet = new Button("Follow strongest pet statistics");

        buttonPause.setStyle("-fx-font-size: 1em; ");
        buttonDominantGenotype.setStyle("-fx-font-size: 1em; ");
        buttonSaveStatistics.setStyle("-fx-font-size: 1em; ");
        buttonFollowStrongestPet.setStyle("-fx-font-size: 1em; ");

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

        buttonSaveStatistics.setOnAction(e -> this.saveStatistics = true);

        buttonFollowStrongestPet.setOnAction(e -> {
            synchronized (this) {
                LinkedList<Animal> animals = null;                              // Getting strongest pet position
                try {
                    animals = engineJungleWorld.getAnimalEngine().getAnimalsList();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                if(animals == null){
                    try {
                        throw new IllegalAccessException("Empty animals list, simulation cant follow any animal.");
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                } else
                    followedAnimal = animals.get(0);
            }
            this.followStrongestPet = true;

        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(buttonPause, buttonDominantGenotype, buttonSaveStatistics, buttonFollowStrongestPet);

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

        if (followStrongestPet){
            Label labelIntro = new Label("Followed animal");
            Label labelFollowedPetEnergy = new Label("Energy: " + followedAnimal.getEnergy());
            if (followedAnimal.getEnergy() < 0)
                labelFollowedPetEnergy = new Label("Energy: 0");
            Label labelFollowedPetGenotype = new Label( "Genotype: " + followedAnimal.getGenotype().getGenotype());
            Label labelFollowedPetNumberOfChildren = new Label( "Number of children: " + followedAnimal.getChildren().size() );
            Label labelFollowedPetDeadDate = new Label("");

            if(followedAnimal.getEnergy() <= 0)
                labelFollowedPetDeadDate = new Label( "Deadth day: " + followedAnimal.getDeathDay() );

            gp.add(labelIntro,1,0);
            gp.add(labelFollowedPetEnergy,1,1);
            gp.add(labelFollowedPetGenotype,1,2);
            gp.add(labelFollowedPetNumberOfChildren,1,3);
            gp.add(labelFollowedPetDeadDate,1,4);
        }

        gp.setPadding(new Insets(5, 0, 5, 10));
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
        return Math.max(windowMaxWidth, windowMaxHeight)/Math.max(width,height);
    }

    private void MapApp(GridPane gp) throws InterruptedException {
        LinkedList<Animal> animals = engineJungleWorld.getAnimalEngine().getAnimalsList();
        HashMap<Vector2d, Grass> grass = jungleMap.getGrassEngine().getGrassMap();
        LinkedList<Vector2d> markdownPos = engineJungleWorld.getStatistics().getAnimalsPosWithDominantGenotype();

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

    private String typeOfCell(Vector2d position, HashMap<Vector2d, Animal> animals, HashMap<Vector2d, Grass> grass, LinkedList<Vector2d> markdownPos) {
        String color;   // savanna

        if (!jungleMap.isInStep(position))
            color = "#A8D09A";
        else
            color = "#D2D0CA";

        if (animals.containsKey(position)) {

            if (followStrongestPet && (followedAnimal.getEnergy() > 0) && ( followedAnimal.getPosition().equals(position) || followedAnimal.equals(animals.get(position)) )) {                        // followed animal
                color = "#661525";
            } else if (markdownGenotype && markdownPos.contains(position)){            // Markdown
                color = "#0000FF";
            } else if(animals.get(position).getEnergy() > jsonParser.initialAnimalEnergy * 4/5 )   // Full energy
                color = "#55FF22";
            else if (animals.get(position).getEnergy() <= jsonParser.initialAnimalEnergy * 4/5 && animals.get(position).getEnergy() > jsonParser.initialAnimalEnergy/3)       // Medium energy
                color = "#FEB100";
            else                    // Low energy
                color = "#EA0606";
        } else if (grass.containsKey(position)) // grass
            color = "#58A35E";

        return  color;
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        if(saveStatistics){
            WriteToFile writeToFile = new WriteToFile(engineJungleWorld.getStatistics(), jsonParser.statisticsFilePath);
            writeToFile.writeToFile();
        }
    }
}