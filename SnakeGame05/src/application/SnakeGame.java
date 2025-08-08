package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends Application {

    private static final int TILE_SIZE = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int GAME_SPEED = 130; // Milliseconds per frame
    private static final String[] FOODS_IMAGE = new String[]{
        "/resources/ic_orange.png", "/resources/ic_apple.png", "/resources/ic_cherry.png",
        "/resources/ic_berry.png", "/resources/ic_coconut_.png", "/resources/ic_peach.png",
        "/resources/ic_watermelon.png", "/resources/ic_pomegranate.png"
    };

    private Direction currentDirection = Direction.RIGHT;
    private boolean gameOver = false;

    private List<Point> snake = new ArrayList<>();
    private Point food;

    private Image foodImage;

    private int score = 0;
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
        score = 0;
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over!", WIDTH * TILE_SIZE / 3.5, HEIGHT * TILE_SIZE / 2.0);
            return;
        }

        update();
        draw(gc);
    }

    private void update() {
        Point head = snake.get(0);
        Point newHead = head.move(currentDirection);

        // Check for collisions
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= WIDTH || newHead.y >= HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        snake.add(0, newHead);

        // Check if food is eaten
        if (newHead.equals(food)) {
            spawnFood();
            score += 5;
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void draw(GraphicsContext gc) {
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore(gc);
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void spawnFood() {
        do {
            food = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        } while (snake.contains(food));

        foodImage = loadImage(FOODS_IMAGE[random.nextInt(FOODS_IMAGE.length)]);
    }

    private Image loadImage(String path) {
        try {
            System.out.println("Loading image: " + path);
            return new Image(SnakeGame.class.getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path + " Error: " + e.getMessage());
            return null;
        }
    }

    private void drawFood(GraphicsContext gc) {
        if (foodImage != null) {
            gc.drawImage(foodImage, food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0) {
                gc.fillRoundRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1, 35, 35);
            } else {
                gc.fillRoundRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1, 20, 20);
            }
        }
    }

    private void drawScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
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
