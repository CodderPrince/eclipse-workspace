package application;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Stack;

public class TowerOfHanoi4 extends Application {
    private int numDisks; // Number of disks, set dynamically by user
    private static final int ROD_SPACING = 300;
    private static final int BASE_ROD_X = 100;

    private Stack<Ellipse>[] rods = new Stack[3];
    private Pane root = new Pane();
    private Text stepLabel;
    private int stepCount = 1;

    @Override
    public void start(Stage primaryStage) {
        // Ask user for the number of disks
        TextInputDialog inputDialog = new TextInputDialog("4");
        inputDialog.setTitle("Tower of Hanoi Setup");
        inputDialog.setHeaderText("Enter the number of disks:");
        inputDialog.setContentText("Number of disks:");
        Optional<String> result = inputDialog.showAndWait();
        numDisks = result.map(Integer::parseInt).orElse(4);

        // Set up the root pane and background
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e0eafc, #cfdef3);");

        // Initialize rods with increased spacing
        for (int i = 0; i < 3; i++) {
            rods[i] = new Stack<>();
            double x = BASE_ROD_X + i * ROD_SPACING;
            root.getChildren().add(createRod(x));
        }

        // Add rod labels
        root.getChildren().add(createRodLabel(BASE_ROD_X, "Source"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + ROD_SPACING, "Helper"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + 2 * ROD_SPACING, "Destination"));

        // Initialize disks
        for (int i = numDisks; i >= 1; i--) {
            Ellipse disk = createDisk(i);
            rods[0].push(disk);
            disk.setTranslateX(BASE_ROD_X); // Place on the first rod
            disk.setTranslateY(400 - (numDisks - i) * 35); // Proper vertical spacing
            root.getChildren().add(disk);
        }

        // Add step label
        stepLabel = new Text(200, 50, "Step: ");
        stepLabel.setFill(Color.BLACK);
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(stepLabel);

        // Add Run Again button
        Button runAgainButton = new Button("Run Again");
        runAgainButton.setLayoutX(250);
        runAgainButton.setLayoutY(450);
        runAgainButton.setOnAction(e -> restartAnimation());
        root.getChildren().add(runAgainButton);

        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(numDisks, 0, 1, 2)).start();

        // Set up scene and stage
        Scene scene = new Scene(root, 950, 500); // Increase width for more spacing
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer - With Holes");
        primaryStage.show();
    }

    private Line createRod(double x) {
        Line rod = new Line(x, 200, x, 400);
        rod.setStroke(Color.BROWN);
        rod.setStrokeWidth(15);
        return rod;
    }

    private Text createRodLabel(double x, String label) {
        Text text = new Text(x - 30, 430, label);
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return text;
    }

    private Ellipse createDisk(int size) {
        double radiusX = 30 + size * 12; // Smaller width for the disk
        double radiusY = 15 + size * 2;  // Fatter disk for better visuals
        Ellipse disk = new Ellipse(radiusX, radiusY);

        // Apply a vibrant color for the disk's fill
        disk.setFill(Color.hsb(size * 45, 0.7, 0.9)); // Unique vibrant fill colors

        // Add a darker border stroke for better definition
        disk.setStroke(Color.BLACK);
        disk.setStrokeWidth(2);

        return disk;
    }


    private void moveDisk(int from, int to) {
        Ellipse disk = rods[from].pop();
        rods[to].push(disk);

        // Animate disk movement
        double startX = disk.getTranslateX();
        double startY = disk.getTranslateY();
        double endX = BASE_ROD_X + to * ROD_SPACING;
        double endY = 400 - rods[to].size() * 35;

        // Breaking movement into three parts: lifting, moving horizontally, and lowering
        Line path = new Line(startX, startY - 50, startX, 200); // Lift up
        Line path2 = new Line(startX, 200, endX, 200); // Move horizontally
        Line path3 = new Line(endX, 200, endX, endY); // Lower down

        // Slow down animations by increasing their durations
        PathTransition transition1 = new PathTransition(Duration.seconds(1), path, disk); // Lift
        PathTransition transition2 = new PathTransition(Duration.seconds(1), path2, disk); // Horizontal
        PathTransition transition3 = new PathTransition(Duration.seconds(1), path3, disk); // Lower

        transition1.setOnFinished(e -> transition2.play());
        transition2.setOnFinished(e -> transition3.play());
        transition3.setOnFinished(e -> {
            disk.setTranslateX(endX);
            disk.setTranslateY(endY);
            updateStepLabel(from, to);
        });

        transition1.play();

        // Pause to synchronize steps
        try {
            Thread.sleep(3000); // Pause the thread for 3 seconds between steps
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
        start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
