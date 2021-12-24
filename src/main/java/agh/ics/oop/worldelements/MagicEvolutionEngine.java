//package agh.ics.oop.worldelements;
//
//import agh.ics.oop.gui.App;
//import javafx.scene.layout.GridPane;
//
//public class MagicEvolutionEngine extends AbstractEngine {
//    int numOfMagicRefills;
//
//    public MagicEvolutionEngine(IWorldMap map, App simulationObserver, GridPane pane, int moveDelay, int startEnergy, int moveEnergy, int plantEnergy, int jungleRatio, int initAnimalsNumber) {
//        super(map, simulationObserver, pane, moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber);
//        this.numOfMagicRefills = 0;
//        this.animals = super.animals;
//    }
//
//    @Override
//    public void singleRun(){
//        super.singleRun();
//        if(numOfAnimals() == 5){
//            if (numOfMagicRefills < 3){
//                numOfMagicRefills++;
//                addInitialAnimals(this.map,this.startEnergy,5,false);
//
//            }
//        }
//
//    }
//}