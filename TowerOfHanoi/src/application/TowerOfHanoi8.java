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

public class TowerOfHanoi8 extends Application {
    private int numDisks; // Number of disks, set dynamically by user
    private static final int ROD_SPACING = 300;
    private static final int BASE_ROD_X = 200; // Adjusted for left spacing
    private static final int BASE_ROD_Y = 300; // Adjusted rods to sit lower
    private static final int ROD_HEIGHT = 150; // Shorter rods to match new positioning

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

        // Initialize rods with adjusted positions
        for (int i = 0; i < 3; i++) {
            rods[i] = new Stack<>();
            double x = BASE_ROD_X + i * ROD_SPACING;
            root.getChildren().add(createRod(x));
        }

        // Add rod labels with more spacing
        root.getChildren().add(createRodLabel(BASE_ROD_X - 50, "Source"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + ROD_SPACING - 50, "Helper"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + 2 * ROD_SPACING - 50, "Destination"));

        // Initialize disks
        for (int i = numDisks; i >= 1; i--) {
            Ellipse disk = createDisk(i);
            rods[0].push(disk);
            disk.setTranslateX(BASE_ROD_X); // Place on the first rod
            disk.setTranslateY(BASE_ROD_Y - ROD_HEIGHT - (numDisks - i) * 20); // Proper vertical spacing
            root.getChildren().add(disk);
        }

        // Add step label (keep it at the top)
        stepLabel = new Text(200, 50, "Step: ");
        stepLabel.setFill(Color.BLACK);
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(stepLabel);

        // Add Run Again button
        Button runAgainButton = new Button("Run Again");
        runAgainButton.setLayoutX(250);
        runAgainButton.setLayoutY(550); // Positioned lower to match new layout
        runAgainButton.setOnAction(e -> restartAnimation());
        root.getChildren().add(runAgainButton);

        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(numDisks, 0, 1, 2)).start();

        // Set up scene and stage
        Scene scene = new Scene(root, 1050, 700); // Increased height for better spacing
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer - Updated");
        primaryStage.show();
    }

    private Line createRod(double x) {
        Line rod = new Line(x, BASE_ROD_Y - ROD_HEIGHT, x, BASE_ROD_Y); // Adjusted rod position
        rod.setStroke(Color.BROWN);
        rod.setStrokeWidth(15);
        return rod;
    }

    private Text createRodLabel(double x, String label) {
        Text text = new Text(x - 20, BASE_ROD_Y + 40, label); // Adjusted label position below rods
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return text;
    }

    private Ellipse createDisk(int size) {
        double radiusX = 30 + size * 7; // Reduced disk size
        double radiusY = 10 + size * 2; // Maintain proportional thickness
        Ellipse disk = new Ellipse(radiusX, radiusY);

        // Apply vibrant colors
        disk.setFill(Color.hsb(size * 45, 0.7, 0.9));
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
        double endY = BASE_ROD_Y - ROD_HEIGHT - rods[to].size() * 20;

        // Breaking movement into three parts: lifting, moving horizontally, and lowering
        Line path = new Line(startX, startY - 50, startX, BASE_ROD_Y - ROD_HEIGHT - 50); // Lift up
        Line path2 = new Line(startX, BASE_ROD_Y - ROD_HEIGHT - 50, endX, BASE_ROD_Y - ROD_HEIGHT - 50); // Move horizontally
        Line path3 = new Line(endX, BASE_ROD_Y - ROD_HEIGHT - 50, endX, endY); // Lower down

        PathTransition transition1 = new PathTransition(Duration.seconds(0.8), path, disk); // Lift
        PathTransition transition2 = new PathTransition(Duration.seconds(0.8), path2, disk); // Horizontal
        PathTransition transition3 = new PathTransition(Duration.seconds(0.8), path3, disk); // Lower

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
            Thread.sleep(2400); // Adjusted pause for smoother animations
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateStepLabel(int from, int to) {
        String[] rodNames = {"Source", "Helper", "Destination"};
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
