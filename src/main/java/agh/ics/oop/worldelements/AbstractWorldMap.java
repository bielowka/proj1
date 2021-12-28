package agh.ics.oop.worldelements;

import agh.ics.oop.basics.MapVisualizer;
import agh.ics.oop.basics.Vector2d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap,IPositionChangedObserver{
    protected int width;
    protected int height;
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;


    Comparator<Animal> compareByEnergy = new Comparator<Animal>() {
        @Override
        public int compare(Animal o1, Animal o2) {
            if (o1.getEnergy() > o2.getEnergy()) return 1;
            if (o2.getEnergy() > o1.getEnergy()) return -1;
            return 0;
        }
    };

    protected Map<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<>();
    protected Map<Vector2d, Grass> grasses = new LinkedHashMap<>();

    public AbstractWorldMap(int width, int height){
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                animals.put(new Vector2d(x, y),new ArrayList<>());
            }
    }

    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    public Vector2d newPosition(Vector2d movePossibility) {
        return movePossibility;
    }

    public boolean place(Animal animal) {
        Vector2d newAnimalPosition = animal.getPosition();
        if (newAnimalPosition.follows(lowerLeft) && newAnimalPosition.precedes(upperRight)){
            this.animals.get(newAnimalPosition).add(animal);
            this.animals.get(newAnimalPosition).sort(compareByEnergy.reversed());
            animal.addObserver(this);
            return true;
        }
        return false;
    }

    public boolean delete(Animal animal){
        for (int i = 0; i < animals.get(animal.getPosition()).size(); i++) {
            if (animals.get(animal.getPosition()).get(i) == animal) {
                animals.get(animal.getPosition()).get(i).removeObserver(this);
                animals.get(animal.getPosition()).remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isOccupied(Vector2d position) {
        return !(animals.get(position).isEmpty() && grasses.get(position) == null);
    }

    public Object objectAt(Vector2d position) {
        if (!animals.get(position).isEmpty() && animals.get(position).size() > 0){
            ArrayList<Animal> animalsOnThisField = animals.get(position);
            animalsOnThisField.sort(compareByEnergy.reversed());
            return animalsOnThisField.get(0);}

        else if (grasses.get(position) != null) return grasses.get(position);
        else return null;
    }

    public Map<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses(){
        return grasses;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        ArrayList<Animal> animalsOnCurrentPosition = animals.get(oldPosition);
        for (int i = 0; i< animalsOnCurrentPosition.size(); i++){
            if (animalsOnCurrentPosition.get(i) == animal){
                animals.get(oldPosition).remove(i);
                animals.get(newPosition).add(animal);
                animals.get(newPosition).sort(compareByEnergy.reversed());
            }
        }
    }

    public String toString(){
        MapVisualizer mapVisualisation = new MapVisualizer(this);
        return mapVisualisation.draw(lowerLeft, upperRight);
    }

    public void addGrass(Vector2d position){
        grasses.put(position,new Grass(position));
    }

    public void removeGrass(Vector2d position){
        grasses.remove(position);
    }

    public int getNumOfAnimals(){
        int sum = 0;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++){
                sum = sum + animals.get(new Vector2d(x,y)).size();
            }
        return sum;
    }

    public int getNumOfGrasses(){
        return grasses.size();
    }
}
