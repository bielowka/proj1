package agh.ics.oop.worldelements;

import agh.ics.oop.basics.MoveDirection;
import agh.ics.oop.basics.Vector2d;
import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

import static java.lang.Math.sqrt;

public class EvolutionEngine implements IEngine,Runnable{

    protected IWorldMap map;
    protected App simulationObserver;
    protected GridPane pane;
    protected int moveDelay;
    protected int startEnergy;
    protected int moveEnergy;
    protected int plantEnergy;
    protected int jungleRatio;
    protected int initAnimalsNumber;
    public boolean pausing = false;
    public boolean tracked = false;
    public boolean isMagic = false;
    public int numOfMagicRefills = 0;
    private int summedLifeSpan = 0;
    private int deadAnimalsNum = 0;
    public int days;
    public Label daysCount;
    public Label genome;
    public LineChart<String,Number> lineChart;
    public XYChart.Series<String,Number> series;
    public XYChart.Series<String,Number> grass;
    public XYChart.Series<String,Number> avgLifetime;
    public XYChart.Series<String,Number> avgEnergy;
    public XYChart.Series<String,Number> avgOffspringNum;
    Label trackedGenome;
    Label trackedOffspring;
    Label trackedDescendants;
    Label deathDay;

    private Animal trackedAnimal;
    private ArrayList<Animal> trackedAnimalDescendants;

    protected List<Animal> animals = new ArrayList<Animal>();

    public EvolutionEngine(boolean isMagic, IWorldMap map, App simulationObserver, GridPane pane, int moveDelay, int startEnergy, int moveEnergy, int plantEnergy, int jungleRatio, int initAnimalsNumber, Label daysCount,Label genome, LineChart<String, Number> lineChart, XYChart.Series<String, Number> series, XYChart.Series<String, Number> grass,XYChart.Series<String, Number> avgLifetime,XYChart.Series<String,Number> avgEnergy, XYChart.Series<String,Number> avgOffspringNum,Label trackedGenome,Label trackedOffspring,Label trackedDescendants,Label deathDay){
        this.map = map;
        this.simulationObserver = simulationObserver;
        this.pane = pane;
        this.moveDelay = moveDelay;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.initAnimalsNumber = initAnimalsNumber;
        this.isMagic = isMagic;
        this.daysCount = daysCount;
        this.genome = genome;
        this.lineChart = lineChart;
        this.series = series;
        this.grass = grass;
        this.avgLifetime = avgLifetime;
        this.avgEnergy = avgEnergy;
        this.avgOffspringNum = avgOffspringNum;
        this.trackedGenome = trackedGenome;
        this.trackedOffspring = trackedOffspring;
        this.trackedDescendants = trackedDescendants;
        this.deathDay = deathDay;


        addInitialAnimals(map, startEnergy, initAnimalsNumber,true);
    }

    public void addInitialAnimals(IWorldMap map, int startEnergy, int initAnimalsNumber, boolean isGenomeRandom) {
        int i = 0;
        Random rand = new Random();
        while (i < initAnimalsNumber){
            int x = rand.nextInt(map.getWidth());
            int y = rand.nextInt(map.getHeight());
            if (map.canMoveTo(new Vector2d(x,y)) && map.objectAt(new Vector2d(x,y)) == null){
                ArrayList<Integer> genome;
                if (isGenomeRandom) genome = newRandomGenome(rand);
                else genome = animals.get(i).getGenome();
                Collections.sort(genome);
                Animal sampleAnimal = new Animal(new Vector2d(x, y), startEnergy, map, genome);
                if (map.place(sampleAnimal)){
                    this.animals.add(sampleAnimal);
                    i++;
                }
            }
        }
    }

    public ArrayList<Integer> newRandomGenome(Random rand) {
        ArrayList<Integer> genome = new ArrayList<Integer>();
        for (int j = 0; j < 32; j++){
            genome.add(rand.nextInt(8));
        }
        return genome;
    }

    public static boolean isInJungle(Vector2d cord, IWorldMap map, int jungleRatio){
        Vector2d mapCenter = new Vector2d(map.getWidth()/2,map.getHeight()/2);
        int jungleWidth = (int) (map.getWidth()/sqrt(jungleRatio));
        int jungleHeight = (int) (map.getHeight()/sqrt(jungleRatio));
        Vector2d jungleLowerLeft = new Vector2d(mapCenter.x,mapCenter.y);
        jungleLowerLeft = jungleLowerLeft.substract(new Vector2d(jungleWidth/2,jungleHeight/2));
        Vector2d jungleUpperRight = new Vector2d(mapCenter.x,mapCenter.y);
        jungleUpperRight = jungleUpperRight.add(new Vector2d(jungleWidth/2,jungleHeight/2));

        return cord.follows(jungleLowerLeft) && cord.precedes(jungleUpperRight);
    }
    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public void setTrackedAnimal(Animal trackedAnimal) {
        this.trackedAnimal = trackedAnimal;
    }

    public void setTracking(){
        tracked = !tracked;
    }

    @Override
    public void run() {
        this.days=0;
        boolean animalsLeft = true;
        while(animalsLeft) {

            try {
                Thread.sleep(moveDelay/2);
            } catch (InterruptedException e) {
                e.printStackTrace();}

            if (!pausing) {
                singleRun(days);
                this.days++;
            }

            if (tracked){
                Platform.runLater(() -> {
                    simulationObserver.trackedAnimalVisual(trackedAnimal,this,this.map,trackedGenome,trackedOffspring,trackedDescendants,deathDay);
                });
            }

            if (isMagic){
                if(numOfAnimals() == 5){
                    if (numOfMagicRefills < 3){
                        numOfMagicRefills++;
                        System.out.print(numOfMagicRefills + "");
                        System.out.println("Magic refill");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();}
                        addInitialAnimals(this.map,this.startEnergy,5,false);
                    }
                }
            }

            if (this.numOfAnimals() <= 0) animalsLeft = false;
        }
    }

    public void switchPausing(){
        pausing = !pausing;
    }

    public synchronized void singleRun(int days) {
        Platform.runLater(() -> {
            simulationObserver.mapVisual(map,pane);
            simulationObserver.statisticsVisual(this,this.daysCount,this.genome);
            simulationObserver.plotVisual(this.map,this,lineChart, series,grass,avgLifetime,avgEnergy,avgOffspringNum,map.getWidth()*75/20);
        });


        Random rand = new Random();

        //removing if dead
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                summedLifeSpan = summedLifeSpan + days - animals.get(i).getBirthdate();
                deadAnimalsNum++;
                map.delete(animals.get(i));
            }
        }

        for (Animal animal : animals){
            if (animal.getEnergy() <= 0){ map.delete(animal);}
        }

        animals.removeIf(animal -> animal.getEnergy() <= 0);

        for (int i = 0; i < animals.size(); i++){
            //daily energy consumption
            animals.get(i).setEnergy(animals.get(i).getEnergy() - moveEnergy);

            //movement
            int gen = rand.nextInt(32);
            int move = animals.get(i).getGenome().get(gen);

            Vector2d prev = animals.get(i).getPosition();
            animals.get(i).move(MoveDirection.intToMoveDirection(move));
            Vector2d now = animals.get(i).getPosition();
            animals.get(i).positionChanged(prev, now);
        }


        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                if (!map.getAnimals().get(new Vector2d(x, y)).isEmpty()) {
                    int highestEnergy = map.getAnimals().get(new Vector2d(x, y)).get(0).getEnergy();
                    int howManyOfHiEn = 0;
                    for (Animal animal : map.getAnimals().get(new Vector2d(x, y))) {
                        if (animal.getEnergy() == highestEnergy) howManyOfHiEn++;
                    }
                    //eating
                    if (map.getGrasses().get(new Vector2d(x, y)) != null) {
                        if (howManyOfHiEn == 1) map.getAnimals().get(new Vector2d(x,y)).get(0).setEnergy(highestEnergy + plantEnergy);
                        else {
                            for (Animal animal : map.getAnimals().get(new Vector2d(x, y))){
                                if (animal.getEnergy() == highestEnergy) animal.setEnergy(highestEnergy + plantEnergy);
                            }
                        }
                        map.removeGrass(new Vector2d(x,y));
                    }

                    //reproduction
                    if (map.getAnimals().get(new Vector2d(x,y)).size() > 1){
                        if (howManyOfHiEn == 1){
                            Animal parent1 = map.getAnimals().get(new Vector2d(x,y)).get(0);
                            Animal parent2 = map.getAnimals().get(new Vector2d(x,y)).get(1);
                            newAnimal(days, x, y, parent1, parent2);
                        }
                        else{
                            int p1 = 0;
                            int p2 = 0;
                            do {
                                p1 = rand.nextInt(howManyOfHiEn);
                                p2 = rand.nextInt(howManyOfHiEn);
                            } while (p1 != p2);

                            Animal parent1 = map.getAnimals().get(new Vector2d(x,y)).get(p1);
                            Animal parent2 = map.getAnimals().get(new Vector2d(x,y)).get(p2);
                            newAnimal(days, x, y, parent1, parent2);

                        }

                    }
                }
            }

        //checking if planting a new grass is possible
        boolean isJungleFull = true;
        boolean isGrasslandFull = true;
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++){
                if (!map.isOccupied(new Vector2d(x,y))){
                    if (isInJungle(new Vector2d(x,y),map,jungleRatio )){
                        isJungleFull = false;
                    }
                    else {
                        isGrasslandFull = false;
                    }
                }
            }
        //planting
        if (!isJungleFull){
            while (!isJungleFull){
                int x1 = rand.nextInt(map.getWidth());
                int y1 = rand.nextInt(map.getHeight());
                if (isInJungle(new Vector2d(x1,y1), map,jungleRatio) && !map.isOccupied(new Vector2d(x1,y1))){
                    map.addGrass(new Vector2d(x1,y1));
                    isJungleFull = true;
                }
            }
        }

        if (!isGrasslandFull){
            while (!isGrasslandFull){
                int x1 = rand.nextInt(map.getWidth());
                int y1 = rand.nextInt(map.getHeight());
                if (!isInJungle(new Vector2d(x1,y1), map,jungleRatio) && !map.isOccupied(new Vector2d(x1,y1))){
                    map.addGrass(new Vector2d(x1,y1));
                    isGrasslandFull = true;
                }
            }
        }


        try {
            Thread.sleep(moveDelay/2);
        } catch (InterruptedException e) {
            e.printStackTrace();}

    }

    private void newAnimal(int days, int x, int y, Animal parent1, Animal parent2) {
        if ((parent1.getEnergy() >= startEnergy/2) && (parent2.getEnergy() >= startEnergy/2)){
            parent1.setOffspringNum(parent1.getOffspringNum() + 1);
            parent2.setOffspringNum(parent2.getOffspringNum() + 1);
            Animal kid = new Animal(new Vector2d(x,y),parent1,parent2,map,days);
            map.place(kid);
            this.animals.add(kid);

            if (trackedAnimalDescendants != null || tracked) {
                if (trackedAnimalDescendants.contains(parent1) || trackedAnimalDescendants.contains(parent2)) {
                    trackedAnimalDescendants.add(kid);
                }
            }
        }
    }

    public int numOfAnimals() {return animals.size();}

    public int grassNum() {return map.getNumOfGrasses();}

    public int avgEnergy(){
        int sum = 0;
        for (Animal animal : animals) sum = sum + animal.getEnergy();
        if (numOfAnimals() == 0) return 0;
        return sum/numOfAnimals();
    }

    public ArrayList<Animal> animalsWithDominantGenome(){
        Map<ArrayList<Integer>,Integer> genotypes = new LinkedHashMap<>();
        for (Animal animal : animals) {
            if (genotypes.get(animal.getGenome() ) != null) {
                int i = genotypes.get(animal.getGenome());
                genotypes.remove(animal.getGenome());
                genotypes.put(animal.getGenome(),i + 1);
            }
            else genotypes.put(animal.getGenome(),1);
        }
        int val = 0;
        ArrayList<Integer> genome = new ArrayList<>();
        ArrayList<Animal> result = new ArrayList<>();

        for (Map.Entry<ArrayList<Integer>,Integer> el : genotypes.entrySet()){
            if (el.getValue() > val){
                genome = el.getKey();
                val = el.getValue();
            }
        }

        for (Animal animal : animals){
            if (animal.getGenome().equals(genome)) result.add(animal);
        }

        return result;
    }

    public String dominantGenome(){
        if (animalsWithDominantGenome() == null) return "";
        if (animalsWithDominantGenome().size() == 0) return "";
        String result = animalsWithDominantGenome().get(0).getGenome().toString();
        return result.replaceAll(",", "").replaceAll(" ","");
    }

    public int getAvgLifeSpan(){
        if (deadAnimalsNum == 0) return 0;
        return summedLifeSpan / deadAnimalsNum;
    }

    public int avgOffspringNum(){
        int sum = 0;
        int num = numOfAnimals();
        for (Animal animal : animals){
            sum = sum + animal.getOffspringNum();
        }
        if (num == 0) return 0;
        return sum / num;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int numOfDescendants(){
        if (trackedAnimal != null && trackedAnimalDescendants != null) return trackedAnimalDescendants.size() - 1;
        return 0;
    }

    public void setTrackedAnimalDescendants() {
        trackedAnimalDescendants = new ArrayList<Animal>();
        trackedAnimalDescendants.add(trackedAnimal);
    }
}
