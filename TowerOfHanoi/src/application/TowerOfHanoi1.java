package application;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;

public class TowerOfHanoi1 extends Application {
    private static final int NUM_DISKS = 4; // Change the number of disks here
    private static final int ROD_HEIGHT = 200;

    private Stack<Circle>[] rods = new Stack[3];
    private Pane root = new Pane();

    @Override
    public void start(Stage primaryStage) {
        // Set up background color
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");

        // Set up rods
        for (int i = 0; i < 3; i++) {
            rods[i] = new Stack<>();
            double x = 150 + i * 200;
            root.getChildren().add(createRod(x));
        }

        // Set up disks
        for (int i = NUM_DISKS; i >= 1; i--) {
            Circle disk = createDisk(i);
            rods[0].push(disk);
            disk.setTranslateX(150);
            disk.setTranslateY(400 - (NUM_DISKS - i) * 40);
            root.getChildren().add(disk);
        }

        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(NUM_DISKS, 0, 1, 2)).start();

        // Set up scene and stage
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer - Circular Disks");
        primaryStage.show();
    }

    private Line createRod(double x) {
        Line rod = new Line(x, 200, x, 400);
        rod.setStroke(Color.BROWN);
        rod.setStrokeWidth(10);
        return rod;
    }

    private Circle createDisk(int size) {
        double radius = 20 + size * 10;
        Circle disk = new Circle(radius);
        disk.setFill(Color.hsb(size * 60, 0.8, 0.9)); // Unique vibrant colors
        disk.setStroke(Color.DARKGRAY);
        return disk;
    }

    private void moveDisk(int from, int to) {
        Circle disk = rods[from].pop();
        rods[to].push(disk);

        // Animate disk movement
        double startX = disk.getTranslateX();
        double startY = disk.getTranslateY();
        double endX = 150 + to * 200;
        double endY = 400 - rods[to].size() * 40;

        Line path = new Line(startX, startY, endX, endY);
        PathTransition transition = new PathTransition(Duration.seconds(1), path, disk);
        transition.setOnFinished(e -> {
            disk.setTranslateX(endX);
            disk.setTranslateY(endY);
        });
        transition.play();

        // Pause for animation effect
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void towerOfHanoi(int n, int src, int helper, int dest) {
        if (n == 1) {
            moveDisk(src, dest);
            return;
        }
        towerOfHanoi(n - 1, src, dest, helper);
        moveDisk(src, dest);
        towerOfHanoi(n - 1, helper, src, dest);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
