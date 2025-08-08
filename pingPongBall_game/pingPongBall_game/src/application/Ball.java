package application;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Ball extends Application {

    // Variables
    private static final double width = 800;
    private static final double height = 600;
    private static final double PLAYER_HEIGHT = 100;
    private static final double PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private double playerOneXPos = 0;
    private double playerOneYPos = height / 2;
    private double playerTwoXPos = width - PLAYER_WIDTH;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private boolean gameStarted;
    private int ballXSpeed = 1;
    private int ballYSpeed = 1;
    private int scoresP1 = 0;
    private int scoresP2 = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Primary stage settings
        primaryStage.setTitle("Ping-Pong Game");
        primaryStage.setResizable(false);

        // Layout for scene 1
        BorderPane borderPane = new BorderPane();
        Canvas canvas = new Canvas(width, height);
        borderPane.setCenter(canvas);
        HBox hbButtons = new HBox();
        hbButtons.setSpacing(33.0);
        borderPane.setBottom(hbButtons);
        HBox hbTitle = new HBox();
        borderPane.setTop(hbTitle);
        Scene scene1 = new Scene(new StackPane(borderPane));

        // Set title
        Label titleText = new Label("PING-PONG GAME");
        titleText.setId("titleText");

        hbTitle.getChildren().addAll(titleText);
        hbTitle.setAlignment(Pos.CENTER);

        // Set start button
        Button startButton = new Button("START GAME");
        startButton.setId("startButton");

        // Set reset button
        Button resetButton = new Button("RESET GAME");
        resetButton.setId("resetButton");

        hbButtons.getChildren().addAll(startButton, resetButton);
        hbButtons.setAlignment(Pos.CENTER);

        // Set graphics
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // JavaFX Timeline - free form animation defined by KeyFrames and their duration
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> runGame(graphicsContext, canvas)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Button "start" control
        startButton.setOnAction(e -> {
            gameStarted = true;
            if (scoresP1 == 5 || scoresP2 == 5) {
                scoresP1 = 0;
                scoresP2 = 0;
            }
        });

        // Button "reset" control
        resetButton.setOnAction(e -> {
            gameStarted = false;
            scoresP1 = 0;
            scoresP2 = 0;
        });

        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene1);
        primaryStage.show();
        timeline.play();
    }

    private void runGame(GraphicsContext graphicsContext, Canvas canvas) {
        // Set background color
        graphicsContext.setFill(Color.FORESTGREEN);
        graphicsContext.fillRect(0, 0, width, height);

        // Set text color and font
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font("Verdana", 25));

        // Draw "User" and "Computer" labels with their scores
        graphicsContext.fillText("User: " + scoresP1, width / 4, 50); // User score
        graphicsContext.fillText("Computer: " + scoresP2, 3 * width / 4, 50); // Computer score

        // Draw player 1 & 2 paddles
        graphicsContext.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        graphicsContext.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);

        if (gameStarted) {
            // Draw center line
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeLine(width / 2, 0, width / 2, height);

            // Player 1 paddle controlled by mouse
            canvas.setOnMouseMoved(e -> playerOneYPos = e.getY());

            // Computer paddle logic
            if (ballXPos < width - width / 4) {
                playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
            } else {
                playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ? playerTwoYPos + 1 : playerTwoYPos - 1;
            }

            // Draw ball
            graphicsContext.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

            // Ball movement
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;
        } else if (scoresP1 == 5 || scoresP2 == 5) {
            gameStarted = false;
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("Verdana", 30));
            graphicsContext.setTextAlign(TextAlignment.CENTER);

            // Randomly determine the winner or draw
            Random rand = new Random();
            int outcome = rand.nextInt(3); // 0 = Draw, 1 = User wins, 2 = Computer wins
            if (outcome == 0) {
                graphicsContext.fillText("Game Over: It's a Draw!", width / 2, height / 2);
            } else if (outcome == 1) {
                graphicsContext.fillText("Game Over: User Wins!", width / 2, height / 2);
            } else {
                graphicsContext.fillText("Game Over: Computer Wins!", width / 2, height / 2);
            }
        } else {
            // Initial start message
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.setFont(new Font("Verdana", 30));
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.fillText("Click the button to start game", width / 2, height / 2);
        }

        // Ball boundary collision detection
        if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;

        // Ball paddle collision detection
        if (((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
            ((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        // Scoring logic
        if (ballXPos < playerOneXPos - PLAYER_WIDTH) {
            scoresP2++; // Computer scores
            gameStarted = false;
        }

        if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            scoresP1++; // User scores
            gameStarted = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
