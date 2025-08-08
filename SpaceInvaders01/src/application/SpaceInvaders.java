package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvaders extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private double spaceshipX = WIDTH / 2 - 20;
    private final double spaceshipY = HEIGHT - 60;
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Alien> aliens = new ArrayList<>();
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Space Invaders");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) moveLeft = true;
            if (e.getCode() == KeyCode.RIGHT) moveRight = true;
            if (e.getCode() == KeyCode.SPACE) bullets.add(new Bullet(spaceshipX + 15, spaceshipY));
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) moveLeft = false;
            if (e.getCode() == KeyCode.RIGHT) moveRight = false;
        });

        initializeAliens();

        setupGameLoop(gc);

        primaryStage.show();
    }

    private void setupGameLoop(GraphicsContext gc) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    update();
                    render(gc);
                } else {
                    renderGameOver(gc);
                }
            }
        }.start();
    }

    private void update() {
        if (moveLeft && spaceshipX > 0) spaceshipX -= 5;
        if (moveRight && spaceshipX < WIDTH - 40) spaceshipX += 5;

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (bullet.getY() < 0) bulletIterator.remove();
        }

        for (Alien alien : aliens) {
            alien.update();
            if (alien.getY() > HEIGHT - 50) gameOver = true;
        }

        Iterator<Alien> alienIterator = aliens.iterator();
        while (alienIterator.hasNext()) {
            Alien alien = alienIterator.next();
            for (Bullet bullet : bullets) {
                if (bullet.intersects(alien)) {
                    alienIterator.remove();
                    bullets.remove(bullet);
                    break;
                }
            }
        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw spaceship
        gc.setFill(Color.BLUE);
        gc.fillRect(spaceshipX, spaceshipY, 40, 20);

        // Draw bullets
        gc.setFill(Color.RED);
        for (Bullet bullet : bullets) {
            gc.fillRect(bullet.getX(), bullet.getY(), 5, 10);
        }

        // Draw aliens
        gc.setFill(Color.GREEN);
        for (Alien alien : aliens) {
            gc.fillRect(alien.getX(), alien.getY(), 40, 20);
        }
    }

    private void renderGameOver(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 50));
        gc.fillText("Game Over", WIDTH / 2 - 150, HEIGHT / 2);
    }

    private void initializeAliens() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                aliens.add(new Alien(80 + j * 60, 50 + i * 40));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Inner classes for Bullet and Alien
    class Bullet {
        private double x, y;

        public Bullet(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            y -= 10;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean intersects(Alien alien) {
            return x < alien.getX() + 40 && x + 5 > alien.getX() && y < alien.getY() + 20 && y + 10 > alien.getY();
        }
    }

    class Alien {
        private double x, y;
        private double speed = 1;

        public Alien(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            x += speed;
            if (x > WIDTH - 40 || x < 0) {
                speed = -speed;
                y += 20;
            }
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
