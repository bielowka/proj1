package agh.ics.oop.gui;

import agh.ics.oop.basics.Vector2d;
import agh.ics.oop.worldelements.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {

    Task<Void> task;
    Stage window;
    Scene inputs, animation;

    GridPane mapWithWall = new GridPane();
    GridPane periodicMap = new GridPane();

    Label plot1 = new Label(); //change to real plot
    Label plot2 = new Label();

    Label daysCount = new Label();
    Label animalsNum = new Label();
    Label grassNum = new Label();
    Label genotypes = new Label();
    Label avgLifeSpan = new Label();
    Label avgDeadLifeSpan = new Label();
    Label avgOffsprNum = new Label();
    VBox statistics = new VBox(daysCount,animalsNum,grassNum,genotypes,avgLifeSpan,avgDeadLifeSpan,avgOffsprNum);
    HBox statAndPlot = new HBox(statistics, plot1);

    Label daysCount2 = new Label();
    Label animalsNum2 = new Label();
    Label grassNum2 = new Label();
    Label genotypes2 = new Label();
    Label avgLifeSpan2 = new Label();
    Label avgDeadLifeSpan2 = new Label();
    Label avgOffsprNum2 = new Label();
    VBox statistics2 = new VBox(daysCount2,animalsNum2,grassNum2,genotypes2,avgLifeSpan2,avgDeadLifeSpan2,avgOffsprNum2);
    HBox statAndPlot2 = new HBox(statistics2, plot2);

    public void plotVisual(IWorldMap map){

    }

    public void statisticsVisual(){

    }

    public void mapVisual(IWorldMap map, GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(20); // width in pixels
//            columnConstraints.setPercentWidth(200.0 / map.getWidth()); // percentage of total width
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(20);
//            rowConstraints.setPercentHeight(200.0 / map.getHeight());
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(x, y)));
                    VBox box = null;
                    try {
                        box = Box.MakeBox((IMapElement) map.objectAt(new Vector2d(x, y)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    GridPane.setConstraints(box, x, y);
                    GridPane.setHalignment(box, HPos.CENTER);
                    pane.add(box, x, y);
                }

            }
    }


    public void start(Stage primaryStage) throws Exception { //DLA WPROWADZONYCH PARAMETROW DOPISAC WYJATKI, NP LICZBA ZWIERZAT NA POCZATKU < ILOSC POL NA MAPIE

        window = primaryStage;


        Label widthLabel = new Label("Width:");
        TextField widthTxtField = new TextField("20");

        Label heightLabel = new Label("Height:");
        TextField heightTxtField = new TextField("20");

        Label startEnergyLabel = new Label("Start energy:");
        TextField startEnergyTxtField = new TextField("100");

        Label moveEnergyLabel = new Label("Move energy:");
        TextField moveEnergyTxtField = new TextField("5");

        Label plantEnergyLabel = new Label("Plant energy:");
        TextField plantEnergyTxtField = new TextField("30");

        Label jungleRatioLabel = new Label("Jungle ratio (_ times smaller):");
        TextField jungleRatioTxtField = new TextField("4");

        Label leftMapIsMagicLabel = new Label("Is map on the left magic? (Y/N)");
        TextField leftMapIsMagicTxtField = new TextField("N");

        Label rightMapIsMagicLabel = new Label("Is map on the right magic? (Y/N)");
        TextField rightMapIsMagicTxtField = new TextField("N");

        Label initialNumOfAnimalsLabel = new Label("Initial number of animals:");
        TextField initialNumOfAnimalsField = new TextField("20");

        Label moveDelayLabel = new Label("Move delay:");
        TextField moveDelayField = new TextField("300");


        VBox leftLabels = new VBox(widthLabel, startEnergyLabel, plantEnergyLabel, leftMapIsMagicLabel, moveDelayLabel);
        leftLabels.setSpacing(20);
        VBox leftFields = new VBox(widthTxtField, startEnergyTxtField, plantEnergyTxtField, leftMapIsMagicTxtField, moveDelayField);
        leftFields.setSpacing(10);
        Separator separator = new Separator(Orientation.VERTICAL);
        VBox rightLabels = new VBox(heightLabel, moveEnergyLabel, jungleRatioLabel, rightMapIsMagicLabel, initialNumOfAnimalsLabel);
        rightLabels.setSpacing(20);
        VBox rightFields = new VBox(heightTxtField, moveEnergyTxtField, jungleRatioTxtField, rightMapIsMagicTxtField, initialNumOfAnimalsField);
        rightFields.setSpacing(10);

        HBox inputBox = new HBox(leftLabels, leftFields, separator, rightLabels, rightFields);
        Button startstop = new Button("Start/Stop");
        Button startstop2 = new Button("Start/Stop");


        Button button = new Button("Confirm");
        button.setPrefWidth(200);
        button.setOnAction(event -> {
            int width = Integer.parseInt(widthTxtField.getText());
            int height = Integer.parseInt(heightTxtField.getText());
            int startEnergy = Integer.parseInt(startEnergyTxtField.getText());
            int moveEnergy = Integer.parseInt(moveEnergyTxtField.getText());
            int plantEnergy = Integer.parseInt(plantEnergyTxtField.getText());
            int jungleRatio = Integer.parseInt(jungleRatioTxtField.getText());
            String leftMapIsMagic = leftMapIsMagicTxtField.getText();
            String rightMapIsMagic = rightMapIsMagicTxtField.getText();
            int initAnimalsNumber = Integer.parseInt(initialNumOfAnimalsField.getText());
            int moveDelay = Integer.parseInt(moveDelayField.getText());
            //scene with maps
            IWorldMap mapwithwall = new MapWithWall(width, height);
            IWorldMap periodicmap = new PeriodicBoundaryMap(width, height);
            mapVisual(mapwithwall,mapWithWall);
            mapVisual(periodicmap,periodicMap);
            //tu powinien byc if magic
            IEngine leftEngine = new EvolutionEngine(mapwithwall, this,mapWithWall, moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber);
            IEngine rightEngine = new EvolutionEngine(periodicmap, this,periodicMap, moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber);


                Thread leftEngineThread = new Thread((Runnable) leftEngine);
                leftEngineThread.start();
                startstop.setOnAction((event2) -> {
                    ((EvolutionEngine) leftEngine).switchPausing();
                });


                Thread rightEngineThread = new Thread((Runnable) rightEngine);
                rightEngineThread.start();
                startstop2.setOnAction((event2) -> {
                    ((EvolutionEngine) rightEngine).switchPausing();
                });

            VBox leftMap = new VBox(mapWithWall,startstop,statAndPlot);
            VBox rightMap = new VBox(periodicMap,startstop2,statAndPlot2);
            leftMap.setAlignment(Pos.TOP_CENTER);
            rightMap.setAlignment(Pos.TOP_CENTER);

            HBox box = new HBox(leftMap,rightMap);


            System.out.println(mapwithwall.getWidth());
            animation = new Scene(box, mapwithwall.getWidth()*40+40, periodicmap.getHeight()*20+300);

            window.setScene(animation);
        });


        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        Separator separator3 = new Separator(Orientation.HORIZONTAL);
        VBox inputsLayout = new VBox(separator1, inputBox, separator2, button, separator3);
        inputsLayout.setAlignment(Pos.CENTER);
        inputs = new Scene(inputsLayout, 650, 210);


//        VBox leftMap = new VBox(mapWithWall,startstop,statAndPlot);
//        VBox rightMap = new VBox(periodicMap,startstop2,statAndPlot2);
//
//        HBox box = new HBox(leftMap,rightMap);
//
//        animation = new Scene(box, mapwithwall.getWidth()*2+10, periodicmap.getHeight()+10);


        primaryStage.setTitle("Game of Life");

        primaryStage.setScene(inputs);
        window.show();

    }

}
