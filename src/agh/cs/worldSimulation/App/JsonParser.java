package agh.cs.worldSimulation.App;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonParser {
    public double width;
    public double height;
    public double moveEnergy;
    public double plantEnergy;
    public double initialNumberOfGrass;
    public double initialAnimalEnergy;
    public double initialAnimalCount;
    public double jungleRatio;
    public double maxNumberOfDays;
    public String statisticsFilePath;


    public void jsonParse() throws IOException {
        Gson gson = new Gson();

        Reader reader = Files.newBufferedReader(Paths.get("parameters.json"));

        Map<?, ?> map = gson.fromJson(reader, Map.class);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());

            if ("width".equals(entry.getKey())) {
                this.width = (double) entry.getValue();
            } else if ("height".equals(entry.getKey())) {
                this.height = (double) entry.getValue();
            } else if ("moveEnergy".equals(entry.getKey())) {
                this.moveEnergy = (double) entry.getValue();
            } else if ("plantEnergy".equals(entry.getKey())) {
                this.plantEnergy = (double) entry.getValue();
            } else if ("initialNumberOfGrass".equals(entry.getKey())) {
                this.initialNumberOfGrass = (double) entry.getValue();
            } else if ("initialAnimalEnergy".equals(entry.getKey())) {
                this.initialAnimalEnergy = (double) entry.getValue();
            } else if ("jungleRatio".equals(entry.getKey())) {
                this.jungleRatio = (double) entry.getValue();
            } else if ("maxNumberOfDays".equals(entry.getKey())) {
                this.maxNumberOfDays = (double) entry.getValue();
            } else if ("initialAnimalCount".equals(entry.getKey())) {
                this.initialAnimalCount = (double) entry.getValue();
            } else if ("statisticsFilePath".equals(entry.getKey())) {
                this.statisticsFilePath = (String) entry.getValue();
            }
        }

        reader.close();
    }
}
