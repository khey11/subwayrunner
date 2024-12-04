package com.example.neasubwayrunner;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class gameTrack{
    private ImageView trackView;
    public gameTrack(String imagePath, double width, double height) throws FileNotFoundException {
        // Load character image
        Image characterImage = new Image(new FileInputStream("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\tracks.png"));
        trackView = new ImageView(characterImage);
        trackView.setFitWidth(width);
        trackView.setFitHeight(height);

    }
    public ImageView getTrackView() {
        return trackView;
    }
}
