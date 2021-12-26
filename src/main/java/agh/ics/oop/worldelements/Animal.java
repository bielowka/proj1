package agh.ics.oop.worldelements;

import agh.ics.oop.basics.MapDirection;
import agh.ics.oop.basics.MoveDirection;
import agh.ics.oop.basics.Vector2d;

import java.util.*;

public class Animal implements IMapElement{
    IWorldMap map;
    private int Energy;
    private MapDirection currentDirection;
    private Vector2d currentPosition;
    private ArrayList<Integer> genome;
    private List<IPositionChangedObserver> observers = new ArrayList<>();
    private int birthdate;
    private int offspringNum = 0;


    public Animal(Vector2d initialPosition, int initialEnergy, IWorldMap map,ArrayList<Integer> genome){
        this.genome = genome;
        this.map = map;
        this.currentPosition = initialPosition;
        Random rand = new Random();
        this.currentDirection = MapDirection.intToMapDirection(rand.nextInt(8));
        this.Energy = initialEnergy;
        this.birthdate = 0;
    }

    public Animal(Vector2d initialPosition, Animal parent1, Animal parent2, IWorldMap map, int birthdate){ //przypadek kiedy jest dzieckiem
        this.map = map;
        this.currentPosition = parent1.getPosition();
        this.Energy = parent1.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.genome = new ArrayList<Integer>();
        Random rand = new Random();
        this.currentDirection = MapDirection.intToMapDirection(rand.nextInt(8));
        this.birthdate = birthdate;

        boolean leftOrRight = rand.nextBoolean();
        int sumOfParentsEnergy = parent1.getEnergy() + parent2.getEnergy();
        if (leftOrRight) {
            if (parent1.getEnergy() < parent2.getEnergy()) {
                int p = (int) ((double) parent1.getEnergy() / (double)sumOfParentsEnergy * 32);
                for (int i = 0; i < 32; i++) {
                    if (i < p) {
                        this.genome.add(parent1.getGenome().get(i));
                    } else {
                        this.genome.add(parent2.getGenome().get(i));
                    }
                }
            }

           else {
                int p = (int) ((double) parent2.getEnergy() / (double)sumOfParentsEnergy * 32);
                for (int i = 0; i < 32; i++) {
                    if (i < p) {
                        this.genome.add(parent2.getGenome().get(i));
                    } else {
                        this.genome.add(parent1.getGenome().get(i));
                    }
                }
            }
        }
        else {
            if (parent1.getEnergy() < parent2.getEnergy()) {
                int p = (int) ((double) parent1.getEnergy() / (double)sumOfParentsEnergy * 32);
                for (int i = 0; i < 32; i++) {
                    if (i < 32 - p) {
                        this.genome.add(parent2.getGenome().get(i));
                    } else {
                        this.genome.add(parent1.getGenome().get(i));
                    }
                }
            }

            else {
                int p = (int) ((double) parent2.getEnergy() / (double)sumOfParentsEnergy * 32);
                for (int i = 0; i < 32; i++) {
                    if (i < 32 - p) {
                        this.genome.add(parent1.getGenome().get(i));
                    } else {
                        this.genome.add(parent2.getGenome().get(i));
                    }
                }
            }
        }

        Collections.sort(this.genome);

        parent1.setEnergy(parent1.getEnergy()*3/4);
        parent2.setEnergy(parent2.getEnergy()*3/4);
    }

    public MapDirection getDirection() {
        return currentDirection;
    }

    public Vector2d getPosition() {
        return currentPosition;
    }

    public int getEnergy() {
        return Energy;
    }

    public void setEnergy(int energy) {
        Energy = energy;
    }

    public ArrayList<Integer> getGenome() {
        return genome;
    }

    public boolean isAt(Vector2d position) {
        return position.x == currentPosition.x && position.y == currentPosition.y;
    }

    public void move(MoveDirection that) {
        int xz = currentPosition.x;
        int yz = currentPosition.y;
        int x = currentPosition.x;
        int y = currentPosition.y;
        switch (that) {
            case FORWARD:
                Vector2d newPosition;
                newPosition = currentPosition.add(currentDirection.toUnitVector());
                if (map.canMoveTo(newPosition)) {
                    currentPosition = map.newPosition(newPosition);
                }
                break;

            case UPPPERRIGHT:
                currentDirection = currentDirection.next();
                break;

            case RIGHT:
                for (int i = 0; i < 2; i++) {
                    currentDirection = currentDirection.next();
                }
                break;

            case LOWERRIGHT:
                for (int i = 0; i < 3; i++) {
                    currentDirection = currentDirection.next();
                }
                break;
            case BACKWARD:
                Vector2d newPosition1;
                newPosition1 = currentPosition.substract(currentDirection.toUnitVector());
                if (map.canMoveTo(newPosition1)) {
                    currentPosition = map.newPosition(newPosition1);
                }
                break;

            case LOWERLEFT:
                for (int i = 0; i < 5; i++) {
                    currentDirection = currentDirection.next();
                }
                break;

            case LEFT:
                for (int i = 0; i < 6; i++) {
                    currentDirection = currentDirection.next();
                }
                break;
            case UPPERLEFT:
                for (int i = 0; i < 7; i++) {
                    currentDirection = currentDirection.next();
                }
                break;
            case DIFFERENT:
                break;
        }
    }

    public void addObserver(IPositionChangedObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangedObserver observer){
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangedObserver observer : observers) observer.positionChanged(this,oldPosition, newPosition);
    }

    public int getBirthdate() {
        return birthdate;
    }

    public int getOffspringNum() {
        return offspringNum;
    }

    public void setOffspringNum(int offspringNum) {
        this.offspringNum = offspringNum;
    }

    @Override
    public String Visualize() {
        return "" + this.Energy;
    }

    @Override
    public String toString() {
        return "" + this.Energy;
    }
}




