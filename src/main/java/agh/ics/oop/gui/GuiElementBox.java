package agh.ics.oop.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GuiElementBox{
    int startEnergy;
    int size;
    Image image;

    public GuiElementBox(int size,Image image) {
        this.startEnergy = startEnergy;
        this.size = size;
        this.image = image;
    }
    public VBox MakeBox() throws FileNotFoundException{

        ImageView imageView = new ImageView(image);


        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        VBox box = new VBox(imageView);
        return box;

    }





}
