package agh.ics.oop.tests;

import agh.ics.oop.gui.App;
import agh.ics.oop.worldelements.IWorldMap;
import agh.ics.oop.worldelements.PeriodicBoundaryMap;
import org.junit.jupiter.api.Test;

class EvolutionEngineTest {

    @Test
    public void test1(){
        App app = new App();
        IWorldMap map = new PeriodicBoundaryMap(20,20);
        IWorldMap map1 = new PeriodicBoundaryMap(20,20);
//        IEngine engine = new EvolutionEngine(false,map,app,new GridPane(),300,100,5,50,6,30, new Label(), new LineChart<String,Number>(), XYChart.Series<> series);
//
//        for (int i = 0; i < 20; i++) {
//            System.out.println(map);
//            engine.run();
//        }

    }

}