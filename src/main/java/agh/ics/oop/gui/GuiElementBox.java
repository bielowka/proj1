package agh.ics.oop.gui;

import agh.ics.oop.worldelements.IMapElement;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GuiElementBox{
    public GuiElementBox(IMapElement object) {
    }
    public VBox MakeBox(IMapElement object) throws FileNotFoundException{
//        Image image = new Image(new FileInputStream(object.Visualize()));
//        ImageView imageView = new ImageView(image);
//        imageView.setFitWidth(30);
//        imageView.setFitHeight(30);
//
//        Label label = new Label(object.toString());
//
//        VBox box = new VBox(imageView,label);
//        box.setAlignment(Pos.CENTER);
//        return box;

        Label label = new Label(object.Visualize());
        VBox box = new VBox(label);
        return box;

    }



}
