package application;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;

public class TowerOfHanoi2 extends Application {
    private static final int NUM_DISKS = 4;
    private static final int ROD_HEIGHT = 200;

    private Stack<Ellipse>[] rods = new Stack[3];
    private Pane root = new Pane();
    private Text stepLabel;
    private int stepCount = 1;

    @Override
    public void start(Stage primaryStage) {
        // Set up the root pane and background
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");

        // Initialize rods
        for (int i = 0; i < 3; i++) {
            rods[i] = new Stack<>();
            double x = 150 + i * 200;
            root.getChildren().add(createRod(x));
        }

        // Add rod labels
        root.getChildren().add(createRodLabel(150, "Source"));
        root.getChildren().add(createRodLabel(350, "Helper"));
        root.getChildren().add(createRodLabel(550, "Destination"));

        // Initialize disks
        for (int i = NUM_DISKS; i >= 1; i--) {
            Ellipse disk = createDisk(i);
            rods[0].push(disk);
            disk.setTranslateX(150);
            disk.setTranslateY(400 - (NUM_DISKS - i) * 40);
            root.getChildren().add(disk);
        }

        // Add step label
        stepLabel = new Text(200, 50, "Step: ");
        stepLabel.setFill(Color.WHITE);
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(stepLabel);

        // Add Run Again button
        Button runAgainButton = new Button("Run Again");
        runAgainButton.setLayoutX(250);
        runAgainButton.setLayoutY(450);
        runAgainButton.setOnAction(e -> restartAnimation());
        root.getChildren().add(runAgainButton);

        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(NUM_DISKS, 0, 1, 2)).start();

        // Set up scene and stage
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer - Enhanced");
        primaryStage.show();
    }

    private Line createRod(double x) {
        Line rod = new Line(x, 200, x, 400);
        rod.setStroke(Color.BROWN);
        rod.setStrokeWidth(10);
        return rod;
    }

    private Text createRodLabel(double x, String label) {
        Text text = new Text(x - 30, 430, label);
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return text;
    }

    private Ellipse createDisk(int size) {
        double radiusX = 50 + size * 20; // Wider disks
        double radiusY = 15;
        Ellipse disk = new Ellipse(radiusX, radiusY);
        disk.setFill(Color.hsb(size * 60, 0.8, 0.9)); // Unique vibrant colors
        disk.setStroke(Color.DARKGRAY);
        disk.setStrokeWidth(2);
        return disk;
    }

    private void moveDisk(int from, int to) {
        Ellipse disk = rods[from].pop();
        rods[to].push(disk);

        // Animate disk movement
        double startX = disk.getTranslateX();
        double startY = disk.getTranslateY();
        double endX = 150 + to * 200;
        double endY = 400 - rods[to].size() * 40;

        Line path = new Line(startX, startY - 50, startX, 200); // Lift up
        Line path2 = new Line(startX, 200, endX, 200); // Move horizontally
        Line path3 = new Line(endX, 200, endX, endY); // Lower down

        PathTransition transition1 = new PathTransition(Duration.seconds(0.5), path, disk);
        PathTransition transition2 = new PathTransition(Duration.seconds(0.5), path2, disk);
        PathTransition transition3 = new PathTransition(Duration.seconds(0.5), path3, disk);

        transition1.setOnFinished(e -> transition2.play());
        transition2.setOnFinished(e -> transition3.play());
        transition3.setOnFinished(e -> {
            disk.setTranslateX(endX);
            disk.setTranslateY(endY);
            updateStepLabel(from, to);
        });

        transition1.play();

        // Pause for animation effect
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateStepLabel(int from, int to) {
        String rodNames[] = {"Source", "Helper", "Destination"};
        stepLabel.setText("Step " + stepCount++ + ": " + rodNames[from] + " to " + rodNames[to]);
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

    private void restartAnimation() {
        // Clear rods and reset the root Pane
        root.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            rods[i].clear(); // Clear each rod's stack
        }
        stepCount = 1;

        // Reinitialize rods
        for (int i = 0; i < 3; i++) {
            double x = 150 + i * 200;
            root.getChildren().add(createRod(x));
        }

        // Add rod labels
        root.getChildren().add(createRodLabel(150, "Source"));
        root.getChildren().add(createRodLabel(350, "Helper"));
        root.getChildren().add(createRodLabel(550, "Destination"));

        // Reinitialize disks
        for (int i = NUM_DISKS; i >= 1; i--) {
            Ellipse disk = createDisk(i);
            rods[0].push(disk);
            disk.setTranslateX(150);
            disk.setTranslateY(400 - (NUM_DISKS - i) * 40);
            root.getChildren().add(disk);
        }

        // Add step label
        stepLabel = new Text(200, 50, "Step: ");
        stepLabel.setFill(Color.WHITE);
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(stepLabel);

        // Add Run Again button
        Button runAgainButton = new Button("Run Again");
        runAgainButton.setLayoutX(250);
        runAgainButton.setLayoutY(450);
        runAgainButton.setOnAction(e -> restartAnimation());
        root.getChildren().add(runAgainButton);

        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(NUM_DISKS, 0, 1, 2)).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
