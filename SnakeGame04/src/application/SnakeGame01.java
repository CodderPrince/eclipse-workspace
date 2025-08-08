package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame01 extends Application {

    private static final int TILE_SIZE = 30;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;
    private static final int GAME_SPEED = 150;

    private Direction currentDirection = Direction.RIGHT;
    private boolean gameOver = false;

    private List<Point> snake = new ArrayList<>();
    private Point food;

    private Random random = new Random();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(GAME_SPEED), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Group root = new Group(canvas);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this::handleKeyPress);

        stage.setScene(scene);
        stage.setTitle("Snake Game");
        stage.show();

        startGame();
        timeline.play();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        spawnFood();
        currentDirection = Direction.RIGHT;
        gameOver = false;
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over!", WIDTH * TILE_SIZE / 3.0, HEIGHT * TILE_SIZE / 2.0);
            return;
        }

        update();
        draw(gc);
    }

    private void update() {
        Point head = snake.get(0);
        Point newHead = head.move(currentDirection);

        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= WIDTH || newHead.y >= HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Draw food with gradient
        gc.setFill(new javafx.scene.paint.RadialGradient(
                0, 0.5, 
                food.x * TILE_SIZE + TILE_SIZE / 2.0, 
                food.y * TILE_SIZE + TILE_SIZE / 2.0, 
                TILE_SIZE / 2.0, 
                false, 
                javafx.scene.paint.CycleMethod.NO_CYCLE, 
                new javafx.scene.paint.Stop(0, Color.RED), 
                new javafx.scene.paint.Stop(1, Color.DARKRED)
        ));
        gc.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw snake with gradient effect
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);

            // Use a different gradient for the snake head and body
            if (i == 0) { // Head
                gc.setFill(new javafx.scene.paint.RadialGradient(
                        0, 0.5, 
                        p.x * TILE_SIZE + TILE_SIZE / 2.0, 
                        p.y * TILE_SIZE + TILE_SIZE / 2.0, 
                        TILE_SIZE / 2.0, 
                        false, 
                        javafx.scene.paint.CycleMethod.NO_CYCLE, 
                        new javafx.scene.paint.Stop(0, Color.LIMEGREEN), 
                        new javafx.scene.paint.Stop(1, Color.GREEN)
                ));
            } else { // Body
                gc.setFill(new javafx.scene.paint.LinearGradient(
                        p.x * TILE_SIZE, p.y * TILE_SIZE, 
                        (p.x + 1) * TILE_SIZE, (p.y + 1) * TILE_SIZE, 
                        false, 
                        javafx.scene.paint.CycleMethod.NO_CYCLE, 
                        new javafx.scene.paint.Stop(0, Color.LIGHTGREEN), 
                        new javafx.scene.paint.Stop(1, Color.DARKGREEN)
                ));
            }

            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }


    private void spawnFood() {
        do {
            food = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        } while (snake.contains(food));
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();

        switch (key) {
            case UP:
                if (currentDirection != Direction.DOWN) currentDirection = Direction.UP;
                break;
            case DOWN:
                if (currentDirection != Direction.UP) currentDirection = Direction.DOWN;
                break;
            case LEFT:
                if (currentDirection != Direction.RIGHT) currentDirection = Direction.LEFT;
                break;
            case RIGHT:
                if (currentDirection != Direction.LEFT) currentDirection = Direction.RIGHT;
                break;
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point move(Direction direction) {
            return switch (direction) {
                case UP -> new Point(x, y - 1);
                case DOWN -> new Point(x, y + 1);
                case LEFT -> new Point(x - 1, y);
                case RIGHT -> new Point(x + 1, y);
            };
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Point other)) return false;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x * 31 + y;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
