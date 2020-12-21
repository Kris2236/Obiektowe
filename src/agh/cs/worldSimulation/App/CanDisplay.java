package agh.cs.worldSimulation.App;

public class CanDisplay {
    private volatile boolean canDisplay;
    private volatile boolean endSimulation;


    public CanDisplay(boolean canDisplay) {
        this.endSimulation = false;
        this.canDisplay = canDisplay;
    }

    public boolean getState() {
        return canDisplay;
    }

    public boolean simulationFinished() {
        return endSimulation;
    }

    public void setEndSimulation(boolean endSimulation) {
        this.endSimulation = endSimulation;
    }

    public void setCanDisplay(boolean state) {
        canDisplay = state;
    }
}