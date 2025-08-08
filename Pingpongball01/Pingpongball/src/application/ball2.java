package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.Random;

public class ball2 extends Application {

    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final int BALL_R = 15;

    private double playerOneYPos = height / 2.0;
    private double playerTwoYPos = height / 2.0;
    private double ballXPos = width / 2.0;
    private double ballYPos = height / 2.0;
    private double ballXSpeed = 1;
    private double ballYSpeed = 1;

    private int scoresP1 = 0;
    private int scoresP2 = 0;

    private boolean gameStarted;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        
        // Run the game loop
        new Thread(() -> {
            while (true) {
                runGame(graphicsContext, canvas);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        canvas.setOnMouseClicked(e -> gameStarted = true);

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) playerOneYPos -= 10;
            if (e.getCode() == KeyCode.DOWN) playerOneYPos += 10;
        });

        stage.setTitle("Pong Game");
        stage.setScene(scene);
        stage.show();
    }

    private void runGame(GraphicsContext graphicsContext, Canvas canvas) {
        // Set background color
        graphicsContext.setFill(Color.DARKSLATEGRAY); // Changed background color
        graphicsContext.fillRect(0, 0, width, height);

        // Set text
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font("Verdana", 25));

        // Draw player 1 & 2
        graphicsContext.fillRect(0, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        graphicsContext.fillRect(width - PLAYER_WIDTH, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Draw scores
        graphicsContext.fillText(scoresP1 + "\t\t\t\t\t\t" + scoresP2, width / 2, 50);

        if (gameStarted) {
            // Draw line in the middle
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeLine(width / 2, 0, width / 2, height);

            // Mouse control on move
            canvas.setOnMouseMoved(e -> playerOneYPos = e.getY());

            // Set computer opponent following the ball
            if (ballXPos < width - width / 4) {
                playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
            } else {
                playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ? playerTwoYPos += 1 : playerTwoYPos - 1;
            }

            // Draw the ball
            graphicsContext.setFill(Color.RED); // Changed ball color
            graphicsContext.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

            // Set ball movement
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            // Set reflection effect to null
            graphicsContext.setEffect(null);
        } else if (scoresP1 == 5 || scoresP2 == 5) {
            gameStarted = false;
            // Set the start text
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("Verdana", 30));
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.fillText("Game over", width / 2, height / 2);

            // Set reflection effect
            Reflection r = new Reflection();
            r.setFraction(0.7f);
            graphicsContext.setEffect(r);

            // Reset the ball start position
            ballXPos = width / 2;
            ballYPos = height / 2;

            // Reset the ball speed and the direction
            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;

            // Mouse control on move stopped
            canvas.setOnMouseMoved(null);
        } else {
            // Set the start text
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.setFont(new Font("Verdana", 30));
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.fillText("Click the button to start game", width / 2, height / 2);

            // Set reflection effect
            Reflection r = new Reflection();
            r.setFraction(0.7f);
            graphicsContext.setEffect(r);

            // Reset the ball start position
            ballXPos = width / 2;
            ballYPos = height / 2;

            // Reset the ball speed and the direction
            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;

            // Mouse control on move stopped
            canvas.setOnMouseMoved(null);
        }

        // Set that the ball has to stay in canvas / on the "screen"
        if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;

        // Increase speed of the ball after player hits it
        if (((ballXPos + BALL_R > width - PLAYER_WIDTH) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
                ((ballXPos < PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        // If player misses the ball, computer gets a point + reset game
        if (ballXPos < 0) {
            scoresP2++;
            gameStarted = false;
        }

        // If computer misses the ball, player gets a point + reset game
        if (ballXPos > width) {
            scoresP1++;
            gameStarted = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
