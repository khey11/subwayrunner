package com.example.neasubwayrunner;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class gameCharacter {
    private ImageView characterView;


    public gameCharacter(String imagePath, double width, double height) throws FileNotFoundException {
        // Load character image
        Image characterImage = new Image(new FileInputStream("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA " +
                "java\\nea subway runner\\charater .png"));
        characterView = new ImageView(characterImage);
        characterView.setFitWidth(width);
        characterView.setFitHeight(height);
    }

    // Set character position
    public void setPosition(double x, double y) {
        characterView.setLayoutX(x);
        characterView.setLayoutY(y);
    }

    public void setLayoutX(double x){
        characterView.setLayoutX(x);
    }
    public double getlayoutX(){
        return characterView.getLayoutX();

    }
    public ImageView getCharacterView() {
        return characterView;
    }
}
