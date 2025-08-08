package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlappyBird2 extends Application {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PIPE_WIDTH = 50;
    private static final int PIPE_GAP = 300;

    private double birdY = HEIGHT / 2;
    private double birdVelocity = 0;
    private final double gravity = 0.3;
    private final double jumpStrength = -10;
    private boolean gameOver = false;
    private int score = 0;

    private final List<double[]> pipes = new ArrayList<>();
    private long lastPipeTime = 0;

    private Image birdImage;
    private Image backgroundImage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Load images
        birdImage = new Image(getClass().getResource("/resources/bird.png").toString());
        backgroundImage = new Image(getClass().getResource("/resources/background.png").toString());

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !gameOver) {
                birdVelocity = jumpStrength;
            } else if (event.getCode() == KeyCode.R && gameOver) {
                resetGame();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Flappy Bird");
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(gc);
            }
        };
        timer.start();
    }

    private void update(GraphicsContext gc) {
        // Draw background
        gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT);

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font(30));
            gc.fillText("Game Over", WIDTH / 2 - 80, HEIGHT / 2);
            gc.setFont(new Font(20));
            gc.fillText("Press R to Restart", WIDTH / 2 - 90, HEIGHT / 2 + 40);
            return;
        }

        // Update bird position
        birdVelocity += gravity;
        birdY += birdVelocity;

        // Check collision with ground or ceiling
        if (birdY > HEIGHT - birdImage.getHeight() || birdY < 0) {
            gameOver = true;
        }

        // Draw bird
        gc.drawImage(birdImage, 100, birdY, 60, 60);

        // Manage pipes
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeTime > 2500) {
            double pipeY = Math.random() * (HEIGHT - PIPE_GAP - 100) + 50;
            pipes.add(new double[]{WIDTH, pipeY});
            lastPipeTime = currentTime;
        }

        // Update and draw pipes
        Iterator<double[]> iterator = pipes.iterator();
        gc.setFill(Color.BLUE);
        while (iterator.hasNext()) {
            double[] pipe = iterator.next();
            pipe[0] -= 2;

            // Draw top pipe
            gc.fillRect(pipe[0], 0, PIPE_WIDTH, pipe[1]);
            // Draw bottom pipe
            gc.fillRect(pipe[0], pipe[1] + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipe[1] - PIPE_GAP);

            // Check for collisions
            if (pipe[0] < 120 && pipe[0] + PIPE_WIDTH > 100) {
                if (birdY < pipe[1] || birdY + 30 > pipe[1] + PIPE_GAP) {
                    gameOver = true;
                }
            }

            // Remove off-screen pipes
            if (pipe[0] + PIPE_WIDTH < 0) {
                iterator.remove();
                score++;
            }
        }

        // Display score
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(20));
        gc.fillText("Score: " + score, 10, 20);
    }

    private void resetGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        gameOver = false;
        score = 0;
        pipes.clear();
        lastPipeTime = 0;
    }
}
