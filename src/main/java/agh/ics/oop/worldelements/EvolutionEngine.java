package agh.ics.oop.worldelements;

import agh.ics.oop.gui.App;
import javafx.scene.layout.GridPane;

public class EvolutionEngine extends AbstractEngine{

    public EvolutionEngine(IWorldMap map, App simulationObserver, GridPane pane, int moveDelay, int startEnergy, int moveEnergy, int plantEnergy, int jungleRatio, int initAnimalsNumber) {
        super(map, simulationObserver,pane, moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber);
    }
}
