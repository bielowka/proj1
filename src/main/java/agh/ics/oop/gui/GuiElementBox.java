package agh.ics.oop.gui;

import agh.ics.oop.worldelements.Animal;
import agh.ics.oop.worldelements.IMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox{
    int startEnergy;
    int size;
    Image image;

    public GuiElementBox(IMapElement object, int startEnergy,int size,Image image) {
        this.startEnergy = startEnergy;
        this.size = size;
        this.image = image;
    }
    public VBox MakeBox(IMapElement object) throws FileNotFoundException{
        if (object instanceof Animal){
            String suffix = "10";
            float enLvl = (float) (((Animal) object).getEnergy()) / (float) startEnergy * 10;
            int enLvlNorm = (int) enLvl;
            if (enLvlNorm >= 70) suffix = "70";
            else if (enLvlNorm >= 10) suffix = String.valueOf((enLvlNorm / 10)*10);
            else if (enLvlNorm <= 0) suffix = "";
            image = new Image(new FileInputStream(object.Visualize().replace("animal10.png","animal" + suffix + ".png")));

        }
        else{
            image = new Image(new FileInputStream(object.Visualize()));
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        VBox box = new VBox(imageView);
        return box;

    }





}
