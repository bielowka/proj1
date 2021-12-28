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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class App extends Application {

    Task<Void> task;
    Stage window;
    Scene inputs, animation;

    Image image;

    GridPane mapWithWall = new GridPane();
    GridPane periodicMap = new GridPane();


    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final CategoryAxis xAxis2 = new CategoryAxis();
    final NumberAxis yAxis2 = new NumberAxis();
    final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
    final LineChart<String,Number> lineChart2 = new LineChart<>(xAxis2,yAxis2);

    Label daysCount = new Label();
    Label genotype = new Label();

    Label daysCount2 = new Label();
    Label genotype2 = new Label();

    Animal trackedAnimal1;
    Animal trackedAnimal2;

    Button dominantGenome = new Button("Show animals with dominant genome (map on the left)");
    Button toFile = new Button("Generate CSV file");
    ArrayList<String[]> output = new ArrayList<>();
    int[] outputSum = {0,0,0,0,0};

    Button dominantGenome2 = new Button("Show animals with dominant genome (map on the right)");
    Button toFile2 = new Button("Generate CSV file");
    ArrayList<String[]> output2 = new ArrayList<>();
    int[] outputSum2 = {0,0,0,0,0};

    Label magicRefillNotification = new Label();
    Label magicRefillNotification2 = new Label();

    boolean show;
    boolean show2;

    public void outputUpdate(IWorldMap map, EvolutionEngine engine,ArrayList<String[]> output,int[] outputSum){
        String[] dailyOutput = {String.valueOf(engine.numOfAnimals()),String.valueOf(map.getNumOfGrasses()),String.valueOf(engine.avgEnergy()),String.valueOf(engine.getAvgLifeSpan()),String.valueOf(engine.avgOffspringNum())};
        outputSum[0]=outputSum[0]+engine.numOfAnimals();
        outputSum[1]=outputSum[1]+map.getNumOfGrasses();
        outputSum[2]=outputSum[2]+engine.avgEnergy();
        outputSum[3]=outputSum[3]+engine.getAvgLifeSpan();
        outputSum[4]=outputSum[4]+engine.avgOffspringNum();
        output.add(dailyOutput);
    }

    public void plotVisual(IWorldMap map, EvolutionEngine engine, LineChart<String, Number> lineChart, XYChart.Series<String, Number> series,XYChart.Series<String, Number> grass,XYChart.Series<String, Number> avgLifetime,XYChart.Series<String, Number> avgEnergy,XYChart.Series<String, Number> avgOffspringNum, int windowSize){
        series.getData().add(new XYChart.Data<>(String.valueOf(engine.days),engine.numOfAnimals()));
        if (series.getData().size() > windowSize)
            series.getData().remove(0);
        grass.getData().add(new XYChart.Data<>(String.valueOf(engine.days),engine.grassNum()));
        if (grass.getData().size() > windowSize)
            grass.getData().remove(0);
        avgLifetime.getData().add(new XYChart.Data<>(String.valueOf(engine.days),engine.getAvgLifeSpan()));
        if (avgLifetime.getData().size() > windowSize)
            avgLifetime.getData().remove(0);
        avgEnergy.getData().add(new XYChart.Data<>(String.valueOf(engine.days),engine.avgEnergy() / engine.getStartEnergy() * 100));
        if (avgEnergy.getData().size() > windowSize)
            avgEnergy.getData().remove(0);
        avgOffspringNum.getData().add(new XYChart.Data<>(String.valueOf(engine.days),engine.avgOffspringNum()));
        if (avgOffspringNum.getData().size() > windowSize)
            avgOffspringNum.getData().remove(0);
    }


    public void statisticsVisual(EvolutionEngine engine,Label daysCount,Label genotype){
        daysCount.setText("Day: " + String.valueOf(engine.days));
        genotype.setText("Genom: " + engine.dominantGenome());
    }

    public void trackedAnimalVisual(Animal animal,EvolutionEngine engine,IWorldMap map,Label trackedGenome,Label trackedOffspring, Label trackedDescendants, Label deathDay){
        if (animal == null){
            deathDay.setText("Dead on: " + String.valueOf(engine.days) + " day");
            engine.setTrackingVal(false);
            engine.setTrackedAnimal(null);
        }
        else if (animal.getEnergy() <= 0){
            deathDay.setText("Dead on: " + String.valueOf(engine.days) + " day");
            engine.setTrackingVal(false);
            engine.setTrackedAnimal(null);
        }
        else{
            trackedGenome.setText("Genome:" + (animal.getGenome().toString().replace(",", "").replace(" ","")));
            trackedOffspring.setText("Kids: " + String.valueOf(animal.getOffspringNum()));
            trackedDescendants.setText("Descendants: " + String.valueOf(engine.numOfDescendants()));
        }

    }

    public void mapVisual(IWorldMap map, GridPane pane,EvolutionEngine engine) {
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);


        pane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(25); // width in pixels
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(25);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(x, y)),engine.getStartEnergy(),25,image);
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

    public void mapPaused(IWorldMap map, EvolutionEngine engine, GridPane pane, Label trackedGenome,Label trackedOffspring,Label trackedDescendants,Label deathDay,Button thisDominantGenome, Button thisToFile, Boolean show){
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        thisDominantGenome.setVisible(true);
        thisToFile.setVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));


        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(25); // width in pixels
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(25);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box;
                    Box = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(x, y)), engine.getStartEnergy(),25,image);
                    if (show && map.objectAt(new Vector2d(x,y)) instanceof Animal){
                        if (engine.animalsWithDominantGenome().contains(((Animal) map.objectAt(new Vector2d(x, y))))){
                            Box = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(x, y)), engine.getStartEnergy(),60,image);
                        }
                    }
                    VBox box = null;
                    try {
                        box = Box.MakeBox((IMapElement) map.objectAt(new Vector2d(x, y)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    GridPane.setConstraints(box, x, y);
                    GridPane.setHalignment(box, HPos.CENTER);
                    pane.add(box, x, y);
                    final int posX = x;
                    final int posY = y;
                    box.setOnMouseEntered((event) -> {
                        if (map.objectAt(new Vector2d(posX,posY)) instanceof Animal) {
                            if (!engine.tracked) {
                                trackedOffspring.setText("");
                                trackedDescendants.setText("");
                                deathDay.setText("");
                                trackedGenome.setText("Genome:" + ((Animal) map.objectAt(new Vector2d(posX,posY))).getGenome().toString().replace(",", "").replace(" ",""));
                            }
                        }
                    });

                    box.setOnMouseClicked((event) -> {
                        if (map.objectAt(new Vector2d(posX,posY)) instanceof Animal) {
                                engine.setTrackedAnimal((Animal) map.objectAt(new Vector2d(posX,posY)));
                                engine.getTrackedAnimal().setOffspringNum(0);
                                engine.setTrackingVal(true);
                                engine.setTrackedAnimalDescendants();
                        }
                    });

                }

            }
    }

    public String toCSV(String[] output) {
        return Stream.of(output).collect(Collectors.joining(";"));
    }

    public void exportToFile(int day,ArrayList<String[]> thisOutput,int[] thisOutputSum,String side) throws IOException {
        File csvOutputFile = new File("OUTPUTS/"+ side + "engine_output_on_day_" + String.valueOf(day) + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            thisOutput.stream()
                    .map(this::toCSV)
                    .forEach(pw::println);
            pw.println();
            pw.print(String.valueOf(thisOutputSum[0]/thisOutput.size()));pw.print(";");
            pw.print(String.valueOf(thisOutputSum[1]/thisOutput.size()));pw.print(";");
            pw.print(String.valueOf(thisOutputSum[2]/thisOutput.size()));pw.print(";");
            pw.print(String.valueOf(thisOutputSum[3]/thisOutput.size()));pw.print(";");
            pw.print(String.valueOf(thisOutputSum[4]/thisOutput.size()));pw.print(";");

        }
        assertTrue(csvOutputFile.exists());
    }


    public void start(Stage primaryStage) throws Exception { //DLA WPROWADZONYCH PARAMETROW DOPISAC WYJATKI, NP LICZBA ZWIERZAT NA POCZATKU < ILOSC POL NA MAPIE

        window = primaryStage;


        Label widthLabel = new Label("Width:");
        TextField widthTxtField = new TextField("15");

        Label heightLabel = new Label("Height:");
        TextField heightTxtField = new TextField("15");

        Label startEnergyLabel = new Label("Start energy:");
        TextField startEnergyTxtField = new TextField("100");

        Label moveEnergyLabel = new Label("Move energy:");
        TextField moveEnergyTxtField = new TextField("5");

        Label plantEnergyLabel = new Label("Plant energy:");
        TextField plantEnergyTxtField = new TextField("100");

        Label jungleRatioLabel = new Label("Jungle ratio (_ times smaller):");
        TextField jungleRatioTxtField = new TextField("4");

        Label leftMapIsMagicLabel = new Label("Is map on the left magic? (Y/N)");
        TextField leftMapIsMagicTxtField = new TextField("Y");

        Label rightMapIsMagicLabel = new Label("Is map on the right magic? (Y/N)");
        TextField rightMapIsMagicTxtField = new TextField("N");

        Label initialNumOfAnimalsLabel = new Label("Initial number of animals:");
        TextField initialNumOfAnimalsField = new TextField("30");

        Label moveDelayLabel = new Label("Move delay:");
        TextField moveDelayField = new TextField("100");


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

            IWorldMap mapwithwall = new MapWithWall(width, height);
            IWorldMap periodicmap = new PeriodicBoundaryMap(width, height);

            XYChart.Series<String,Number> series = new XYChart.Series<>();
            XYChart.Series<String,Number> grass = new XYChart.Series<>();
            XYChart.Series<String,Number> avgLifeTime = new XYChart.Series<>();
            XYChart.Series<String,Number> avgEnergy = new XYChart.Series<>();
            XYChart.Series<String,Number> avgOffspringNum = new XYChart.Series<>();
            series.setName("Number of animals");
            grass.setName("Number of grass");
            avgLifeTime.setName("Average lifetime");
            avgEnergy.setName("Average energy (% of start energy)");
            avgOffspringNum.setName("Average number of offspring");
            lineChart.getData().add(series);
            lineChart.getData().add(grass);
            lineChart.getData().add(avgLifeTime);
            lineChart.getData().add(avgEnergy);
            lineChart.getData().add(avgOffspringNum);
            xAxis.setAnimated(false);
            yAxis.setAnimated(false);
            lineChart2.setAnimated(false);

            XYChart.Series<String,Number> series2 = new XYChart.Series<>();
            XYChart.Series<String,Number> grass2 = new XYChart.Series<>();
            XYChart.Series<String,Number> avgLifeTime2 = new XYChart.Series<>();
            XYChart.Series<String,Number> avgEnergy2 = new XYChart.Series<>();
            XYChart.Series<String,Number> avgOffspringNum2 = new XYChart.Series<>();
            series2.setName("Number of animals");
            grass2.setName("Number of grass");
            avgLifeTime2.setName("Average lifetime");
            avgEnergy2.setName("Average energy (% of start energy)");
            avgOffspringNum2.setName("Average number of offspring");
            lineChart2.getData().add(series2);
            lineChart2.getData().add(grass2);
            lineChart2.getData().add(avgLifeTime2);
            lineChart2.getData().add(avgEnergy2);
            lineChart2.getData().add(avgOffspringNum2);
            xAxis2.setAnimated(false);
            yAxis2.setAnimated(false);
            lineChart2.setAnimated(false);


            boolean isLeftMapMagic = (Objects.equals(leftMapIsMagic, "Y"));
            boolean isRightMapMagic = (Objects.equals(rightMapIsMagic, "Y"));


            Label trackedGenome = new Label();
            Label trackedOffspring = new Label();
            Label trackedDescendants = new Label();
            Label deathDay = new Label();
            VBox tracking1 = new VBox(trackedGenome,trackedOffspring,trackedDescendants,deathDay);

            Label trackedGenome2 = new Label();
            Label trackedOffspring2 = new Label();
            Label trackedDescendants2 = new Label();
            Label deathDay2 = new Label();
            VBox tracking2 = new VBox(trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2);

            dominantGenome.setVisible(false);
            toFile.setVisible(false);
            dominantGenome2.setVisible(false);
            toFile2.setVisible(false);
            magicRefillNotification.setPrefHeight(5);
            magicRefillNotification.setTextFill(Paint.valueOf("red"));
            magicRefillNotification2.setPrefHeight(5);
            magicRefillNotification2.setTextFill(Paint.valueOf("red"));
            VBox buttons = new VBox(dominantGenome,toFile,dominantGenome2,toFile2);
            buttons.setAlignment(Pos.CENTER);
            buttons.setSpacing(5);

            Separator sep = new Separator(Orientation.HORIZONTAL);
            Separator sep1 = new Separator(Orientation.HORIZONTAL);
            sep1.setPrefHeight(10);
            VBox tracking = new VBox(tracking1,sep,tracking2,sep1,buttons);

            IEngine leftEngine = new EvolutionEngine(isLeftMapMagic,mapwithwall, this,mapWithWall,
                    moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber,
                    daysCount,genotype,lineChart,series,grass,avgLifeTime,avgEnergy,avgOffspringNum,
                    trackedGenome,trackedOffspring,trackedDescendants,deathDay,
                    output,outputSum,
                    magicRefillNotification);
            IEngine rightEngine = new EvolutionEngine(isRightMapMagic,periodicmap, this,periodicMap,
                    moveDelay, startEnergy, moveEnergy, plantEnergy, jungleRatio, initAnimalsNumber,
                    daysCount2,genotype2,lineChart2, series2,grass2,avgLifeTime2,avgEnergy2,avgOffspringNum2
                    ,trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,
                    output2,outputSum2,
                    magicRefillNotification2);

            mapVisual(mapwithwall,mapWithWall,(EvolutionEngine) leftEngine);
            mapVisual(periodicmap,periodicMap,(EvolutionEngine) rightEngine);

            Thread leftEngineThread = new Thread((Runnable) leftEngine);
            leftEngineThread.start();
                startstop.setOnAction((event2) -> {
                    leftEngine.switchPausing();
                    if (((EvolutionEngine) leftEngine).pausing) {
                        this.mapPaused(mapwithwall,(EvolutionEngine) leftEngine,mapWithWall,trackedGenome,trackedOffspring,trackedDescendants,deathDay,dominantGenome,toFile,false);
                        toFile.setOnAction((event3) -> {
                            try {
                                exportToFile(((EvolutionEngine) leftEngine).days,output,outputSum,"left");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        dominantGenome.setOnAction((event3) -> {
                            this.mapPaused(mapwithwall,(EvolutionEngine) leftEngine,mapWithWall,trackedGenome,trackedOffspring,trackedDescendants,deathDay,dominantGenome,toFile, true);
                        });
                    }
                    else {
                        dominantGenome.setVisible(false);
                        toFile.setVisible(false);
                    }
                });


            Thread rightEngineThread = new Thread((Runnable) rightEngine);
            rightEngineThread.start();
                startstop2.setOnAction((event2) -> {
                    rightEngine.switchPausing();
                    if (((EvolutionEngine) rightEngine).pausing) {
                        this.mapPaused(periodicmap,(EvolutionEngine) rightEngine,periodicMap,trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,dominantGenome2,toFile2,false);
                        toFile2.setOnAction((event3) -> {
                            try {
                                exportToFile(((EvolutionEngine) rightEngine).days,output2,outputSum2,"right");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        dominantGenome2.setOnAction((event3) -> {
                            this.mapPaused(periodicmap,(EvolutionEngine) rightEngine,periodicMap,trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,dominantGenome2,toFile2, true);
                        });
                    }
                    else {
                        dominantGenome2.setVisible(false);
                        toFile2.setVisible(false);
                    }
                });


            HBox statBox = new HBox(daysCount,genotype);
            statBox.setSpacing(10);
            HBox statBox2 = new HBox(daysCount2,genotype2);
            statBox2.setSpacing(10);
            mapWithWall.setAlignment(Pos.CENTER);
            periodicMap.setAlignment(Pos.CENTER);
            VBox leftMap = new VBox(mapWithWall,startstop,magicRefillNotification,statBox,lineChart);
            VBox rightMap = new VBox(periodicMap,startstop2,magicRefillNotification2,statBox2,lineChart2);
            leftMap.setAlignment(Pos.TOP_CENTER);
            rightMap.setAlignment(Pos.TOP_CENTER);

            leftMap.setPrefWidth(mapwithwall.getWidth()*30);
            rightMap.setPrefWidth(periodicmap.getWidth()*30);
            leftMap.setMaxWidth(mapwithwall.getWidth()*25 + 40);
            rightMap.setMaxWidth(mapwithwall.getWidth()*25 + 40);
            leftMap.setMinWidth(375);
            rightMap.setMinWidth(375);

            HBox box = new HBox(leftMap,rightMap,tracking);
            box.setAlignment(Pos.CENTER);
            animation = new Scene(box, Math.max(mapwithwall.getWidth()*40+600,1100), periodicmap.getHeight()*20+500);


            window.setScene(animation);
        });


        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        Separator separator3 = new Separator(Orientation.HORIZONTAL);
        VBox inputsLayout = new VBox(separator1, inputBox, separator2, button, separator3);
        inputsLayout.setAlignment(Pos.CENTER);
        inputs = new Scene(inputsLayout, 650, 210);

        primaryStage.setTitle("Game of Life");

        primaryStage.setScene(inputs);
        window.show();

    }

}
