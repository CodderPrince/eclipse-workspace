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
import javafx.scene.shape.Rectangle;

import java.util.Optional;
import java.util.Stack;

public class TowerOfHanoi9 extends Application {
    private int numDisks; // Number of disks, set dynamically by user
    private static final int ROD_SPACING = 300;
    private static final int BASE_ROD_X = 200; // Increased left space for the source rod
    private static final int ROD_HEIGHT = 400;

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

        // Add rod labels with more distance
        root.getChildren().add(createRodLabel(BASE_ROD_X - 50, "Source"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + ROD_SPACING - 50, "Helper"));
        root.getChildren().add(createRodLabel(BASE_ROD_X + 2 * ROD_SPACING - 50, "Destination"));

     // Initialize disks
        for (int i = numDisks; i >= 1; i--) {
            Ellipse disk = createDisk(i);
            rods[0].push(disk); // Add disk to the first rod stack
            disk.setTranslateX(BASE_ROD_X); // Horizontal position on the first rod
            disk.setTranslateY(650 - (numDisks - i) * 30); // Vertical position aligned to the rod bottom
            root.getChildren().add(disk); // Add the disk to the scene
        }


        // Add step label
        stepLabel = new Text(200, 50, "Step: ");
        stepLabel.setFill(Color.BLACK);
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(stepLabel);

     // Add Run Again button
        Button runAgainButton = new Button("Run Again");

        // Modify button position
        runAgainButton.setLayoutX(450); // Set custom X position
        runAgainButton.setLayoutY(750); // Set custom Y position

        // Modify button style
        runAgainButton.setStyle(
            "-fx-background-color: #4CAF50; " + // Green fill color
            "-fx-text-fill: white; " +         // White text color
            "-fx-font-size: 16px; " +          // Font size
            "-fx-font-weight: bold; " +        // Font weight
            "-fx-border-radius: 10px; " +      // Rounded border
            "-fx-background-radius: 10px;"    // Rounded background
        );

        // Add action to restart animation
        runAgainButton.setOnAction(e -> restartAnimation());

        // Add button to the root pane
        root.getChildren().add(runAgainButton);


        // Start Tower of Hanoi animation
        new Thread(() -> towerOfHanoi(numDisks, 0, 1, 2)).start();

        // Set up scene and stage
        Scene scene = new Scene(root, 1050, 800); // Increased width for more space
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer - Updated");
        primaryStage.show();
    }

    private Rectangle createRod(double x) {
        double rodBottom = 650; // Bottom of the window
        double rodTop = rodBottom - ROD_HEIGHT; // Top of the rod
        double rodWidth = 15; // Width of the rod
        Rectangle rod = new Rectangle(x - rodWidth / 2, rodTop, rodWidth, ROD_HEIGHT); // Create a rectangle rod
        rod.setFill(Color.BROWN); // Fill color
        rod.setArcWidth(10); // Rounded corners (width)
        rod.setArcHeight(10); // Rounded corners (height)
        return rod;
    }




    private Text createRodLabel(double x, String label) {
        Text text = new Text(label);
        text.setX(x + 18); // Custom offset for X-axis (adjust as needed)
        text.setY(ROD_HEIGHT + 320); // Set Y-axis position below the rod
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return text;
    }




    private Ellipse createDisk(int size) {
        double radiusX = 40 + size * 10; // Average disk size
        double radiusY = 15 + size * 2;
        Ellipse disk = new Ellipse(radiusX, radiusY);

        // Apply a vibrant color for the disk's fill
        disk.setFill(Color.hsb(size * 45, 0.7, 0.9)); // Unique vibrant fill colors
        disk.setStroke(Color.BLACK);
        disk.setStrokeWidth(2);

        return disk;
    }

    private void moveDisk(int from, int to) {
        Ellipse disk = rods[from].pop(); // Remove disk from the source rod
        rods[to].push(disk); // Add disk to the destination rod

        // Animate disk movement
        double startX = disk.getTranslateX();
        double startY = disk.getTranslateY();
        double endX = BASE_ROD_X + to * ROD_SPACING;
        double endY = 650 - rods[to].size() * 30; // Calculate Y position based on the stack size

        // Breaking movement into three parts: lifting, moving horizontally, and lowering
        Line liftPath = new Line(startX, startY, startX, 200); // Lift up to a safe height
        Line horizontalPath = new Line(startX, 200, endX, 200); // Move horizontally to the destination rod
        Line lowerPath = new Line(endX, 200, endX, endY); // Lower down to the calculated position

        // Path transitions for the animations
        PathTransition liftTransition = new PathTransition(Duration.seconds(1), liftPath, disk); // Lift
        PathTransition horizontalTransition = new PathTransition(Duration.seconds(1), horizontalPath, disk); // Horizontal move
        PathTransition lowerTransition = new PathTransition(Duration.seconds(1), lowerPath, disk); // Lower

        // Sequentially play the animations
        liftTransition.setOnFinished(e -> horizontalTransition.play());
        horizontalTransition.setOnFinished(e -> lowerTransition.play());
        lowerTransition.setOnFinished(e -> {
            disk.setTranslateX(endX); // Final X position
            disk.setTranslateY(endY); // Final Y position
            updateStepLabel(from, to); // Update step label
        });

        liftTransition.play();

        // Pause to synchronize steps (optional, based on threading)
        try {
            Thread.sleep(2500); // Total time for lift, move, and lower animations
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
