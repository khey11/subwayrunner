package com.example.neasubwayrunner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Obstacle {
    private ImageView obstacleView;

    public Obstacle(String imagePath, double width, double height) throws FileNotFoundException {
        javafx.scene.image.Image obstacleImage = new Image(new FileInputStream("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\track image 2.png"));
        obstacleView = new ImageView((obstacleImage));
        obstacleView.setFitWidth(100);
        obstacleView.setFitHeight(200);
    }

    public ImageView getObstacleView() {
        return obstacleView;
    }

    public void setPosition(double x, double y) {
        obstacleView.setLayoutX(x);
        obstacleView.setLayoutY(y);
    }


}