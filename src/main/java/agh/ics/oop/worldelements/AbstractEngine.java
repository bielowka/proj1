package agh.ics.oop.worldelements;

import agh.ics.oop.basics.MoveDirection;
import agh.ics.oop.basics.Vector2d;
import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;

abstract public class AbstractEngine implements IEngine,Runnable{

    private IWorldMap map;
    private App simulationObserver;
    private GridPane pane;
    private int moveDelay;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int jungleRatio;
    private int initAnimalsNumber;
    public boolean pausing = false;

    final List<Animal> animals = new ArrayList<Animal>();

    public AbstractEngine(IWorldMap map, App simulationObserver,GridPane pane, int moveDelay, int startEnergy, int moveEnergy, int plantEnergy, int jungleRatio, int initAnimalsNumber){
        this.map = map;
        this.simulationObserver = simulationObserver;
        this.pane = pane;
        this.moveDelay = moveDelay;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.initAnimalsNumber = initAnimalsNumber;


        int i = 0;
        Random rand = new Random();
        while (i < initAnimalsNumber){
            int x = rand.nextInt(map.getWidth());
            int y = rand.nextInt(map.getHeight());
            if (map.canMoveTo(new Vector2d(x,y)) && map.objectAt(new Vector2d(x,y)) == null){
                ArrayList<Integer> genome = new ArrayList<Integer>();
                for (int j = 0; j < 32; j++){
                    genome.add(rand.nextInt(8));
                }
                Collections.sort(genome);
                Animal sampleAnimal = new Animal(new Vector2d(x, y), startEnergy, map, genome);
                if (map.place(sampleAnimal)){
                    this.animals.add(sampleAnimal);
                    i++;
                }
            }
        }
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

    @Override
    public void run() {
        boolean animalsLeft = true;
        while(animalsLeft) {
            try {
                Thread.sleep(moveDelay/2);
            } catch (InterruptedException e) {
                e.printStackTrace();}

            if (!pausing) singleRun();
            if (numOfAnimals() == 0) animalsLeft = false;
        }
    }

    public void switchPausing(){
        pausing = !pausing;
    }

    public void singleRun() {
        Platform.runLater(() -> {
            simulationObserver.mapVisual(map,pane);
        });


        Random rand = new Random();

        //removing if dead
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                map.delete(animals.get(i));
            }
        }

        for (Animal animal : animals){
            if (animal.getEnergy() <= 0) map.delete(animal);
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
                            if ((parent1.getEnergy() >= startEnergy/2) && (parent2.getEnergy() >= startEnergy/2)){
                                Animal kid = new Animal(new Vector2d(x,y),parent1,parent2,map);
                                map.place(kid);
                                this.animals.add(kid);
                            }
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
                            if ((parent1.getEnergy() >= startEnergy/2) && (parent2.getEnergy() >= startEnergy/2)){
                                Animal kid = new Animal(new Vector2d(x,y),parent1,parent2,map);
                                map.place(kid);
                                this.animals.add(kid);
                            }

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

    public int numOfAnimals() {return animals.size();}

    public int avgEnergy(ArrayList<Animal> animals){
        int sum = 0;
        for (Animal animal : animals) sum = sum + animal.getEnergy();

        return sum/numOfAnimals();
    }


}
