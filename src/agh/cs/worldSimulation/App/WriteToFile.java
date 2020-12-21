package agh.cs.worldSimulation.App;

import agh.cs.worldSimulation.engine.Statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteToFile {
    private final Statistics statistics;
    private String fileName;

    WriteToFile(Statistics statistics, String fileName) {
        this.fileName = fileName;
        this.statistics = statistics;
    }

    public void writeToFile() throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf("{" + "\n");
        printWriter.printf(statistics.getGlobalStatistics());
        printWriter.printf("}" + "\n");
        printWriter.close();
    }
}