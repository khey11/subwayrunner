package com.example.neasubwayrunner;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class HelloApplication extends Application {

    // Global Variables
    private boolean isPaused = false; // Tracks if the game is paused
    private AnimationTimer trackMovement; // Animation for track movement
    private Timeline scoreUpdater; // Timeline for score updates
    private Group pauseMenu; // Updated to make pauseMenu a global variable
    private int score = 0; // Score tracking
    private double speed = 2; // Track speed

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(menu(stage), 800, 600);

        stage.setTitle("Subway Runner");
        stage.setScene(scene);
        stage.show();
    }

    public Group menu(Stage stage) throws FileNotFoundException {
        // Background
        ImageView menuBackground = new ImageView(new Image(
                new FileInputStream("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\subway-surfers-logo.png")
        ));
        menuBackground.setFitHeight(600);
        menuBackground.setFitWidth(800);

        Group layout = new Group();

        // Buttons
        Button startButton = new Button("Start Game");
        startButton.setLayoutX(350);
        startButton.setLayoutY(200);

        Button settingsButton = new Button("Settings");
        settingsButton.setLayoutX(350);
        settingsButton.setLayoutY(260);

        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(350);
        exitButton.setLayoutY(320);

        // Button Fonts
        startButton.setFont(Font.font(20));
        settingsButton.setFont(Font.font(20));
        exitButton.setFont(Font.font(20));

        // Button Actions
        startButton.setOnAction(e -> {
            try {
                startGame(stage);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        settingsButton.setOnAction(e -> openSettings());
        exitButton.setOnAction(e -> System.exit(0));

        layout.getChildren().addAll(menuBackground, startButton, settingsButton, exitButton);
        return layout;
    }

    private void startGame(Stage stage) throws FileNotFoundException {
        score = 0;
        speed = 2;

        if (trackMovement != null) trackMovement.stop();
        if (scoreUpdater != null) scoreUpdater.stop();

        stage.setScene(gameScreen(stage));
    }

    private void openSettings() {
        System.out.println("Opening settings...");
    }

    public Scene gameScreen(Stage stage) throws FileNotFoundException {
        Group layout = new Group();

        // Track Initialization
        gameTrack tracks = new gameTrack("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\tracks.png", 600, 600);
        gameTrack tracks2 = new gameTrack("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\tracks.png", 600, 600);
        tracks.getTrackView().setLayoutX(100);
        tracks2.getTrackView().setLayoutX(100);
        tracks2.getTrackView().setLayoutY(-600);

        // Track Movement
        trackMovement = new AnimationTimer() {
            @Override
            public void handle(long l) {
                tracks2.getTrackView().setLayoutY(tracks2.getTrackView().getLayoutY() + speed);
                tracks.getTrackView().setLayoutY(tracks.getTrackView().getLayoutY() + speed);

                if (tracks2.getTrackView().getLayoutY() >= 600) tracks2.getTrackView().setLayoutY(-600);
                if (tracks.getTrackView().getLayoutY() >= 600) tracks.getTrackView().setLayoutY(-600);

                ImageView obstacleView = null;
                for (Iterator<Obstacle> iterator = obstacles.iterator(); iterator.hasNext(); ) {
                    Obstacle obstacle = iterator.next();
                    obstacleView = obstacle.getObstacleView();
                    obstacleView.setLayoutY(obstacleView.getLayoutY() + 2);
                    obstacleView.setLayoutY(obstacleView.getLayoutY() + (speed-0.5));


                    if (obstacleView.getLayoutY() > 600) {
                        layout.getChildren().remove(obstacleView);
                        iterator.remove();
                    }

                }


            }
};
        trackMovement.start();


        // Character Initialization
        gameCharacter character = new gameCharacter("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\character.png", 80, 100);
        character.setPosition(350, 400);

        layout.getChildren().addAll(tracks.getTrackView(), tracks2.getTrackView());
        layout.getChildren().add(character.getCharacterView());

        // Score Label
        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(new Font(20));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);

        // Initialize Pause Menu
        pauseMenu = pauseMenu(stage);
        pauseMenu.setVisible(false);
        layout.getChildren().addAll(scoreLabel, pauseMenu);

        // Score Updater
        scoreUpdater = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
            score++;
            scoreLabel.setText("Score: " + score);

            if (score % 50 == 0) {
                speed += 0.5; // Increment speed every 10 points
                System.out.println("Speed increased to: " + speed);
            }
        }));
        scoreUpdater.setCycleCount(Timeline.INDEFINITE);
        scoreUpdater.play();


        startObstacleSpawner();
        spawnObstacle();
        layout.getChildren().addAll(this.layout);
        Scene gamescene = new Scene(layout, 800, 600);
        gamescene.setFill(Color.web("#494949"));
        gamescene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A,LEFT -> {
                    double movementLeft = -210;
                    if (character.getCharacterView().getLayoutX() > 200) {
                        character.getCharacterView().setLayoutX(character.getCharacterView().getLayoutX() + movementLeft);
                    }
                }
                case D, RIGHT -> {
                    double movementRight = 210;
                    if (character.getCharacterView().getLayoutX() < 500) {
                        character.getCharacterView().setLayoutX(character.getCharacterView().getLayoutX() + movementRight);
                    }
                }

                case ESCAPE -> { // Handle Pause/Resume
                    if (!isPaused) {
                        pauseGame(pauseMenu); // Pause the game
                    } else {
                        resumeGame(pauseMenu); // Resume the game
                    }
                }
            }
        });


        return gamescene;
    }

    private Group pauseMenu(Stage stage) {
        Group pauseMenu = new Group();

        // Background
        Rectangle background = new Rectangle(800, 600, Color.BLACK);
        background.setOpacity(0.7);

        // Buttons
        Button resumeButton = new Button("RESUME");
        resumeButton.setLayoutX(370);
        resumeButton.setLayoutY(200);
        resumeButton.setOnAction(e -> resumeGame(pauseMenu)); // Resume Game

        Button restartButton = new Button("RESTART");
        restartButton.setLayoutX(370);
        restartButton.setLayoutY(260);
        restartButton.setOnAction(e -> {
            try {
                startGame(stage); // Restart Game
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        Button mainMenuButton = new Button("MAIN MENU");
        mainMenuButton.setLayoutX(370);
        mainMenuButton.setLayoutY(320);
        mainMenuButton.setOnAction(e -> {
            try {
                stage.setScene(new Scene(menu(stage), 800, 600)); // Return to Main Menu
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        pauseMenu.getChildren().addAll(background, resumeButton, restartButton, mainMenuButton);
        return pauseMenu;
    }

    private void pauseGame(Group pauseMenu) {
        if (!isPaused) {
            isPaused = true;
            trackMovement.stop();
            scoreUpdater.pause();
            obstacleSpawner.pause();
            pauseMenu.setVisible(true);
        }
    }

    private void resumeGame(Group pauseMenu) {
        if (isPaused) {
            isPaused = false;
            trackMovement.start();
            scoreUpdater.play();
            obstacleSpawner.play();
            pauseMenu.setVisible(false);
        }
    }
    private Group layout = new Group();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private Timeline obstacleSpawner;


    private void spawnObstacle() throws FileNotFoundException {
        Random random = new Random();
        int lane = random.nextInt(3);
        double laneX = 125 + lane * 210;

        Obstacle obstacle = new Obstacle("C:\\Users\\kheys\\Documents\\NEA PROJECT\\NEA java\\nea subway runner\\track image 2.png", 50, 50);
        obstacle.setPosition(laneX, -100);
        layout.getChildren().add(obstacle.getObstacleView());
        obstacles.add(obstacle);
    }
    private void startObstacleSpawner() {
        obstacleSpawner = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            try {
                spawnObstacle(); // Spawn a new obstacle every 2 seconds
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }));
        obstacleSpawner.setCycleCount(Timeline.INDEFINITE);
        obstacleSpawner.play();
    }




    public static void main(String[] args) {
        launch();
    }
}
