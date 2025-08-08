package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create canvas for drawing
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Input field for fractal order
        Label label = new Label("Enter an order: ");
        TextField orderField = new TextField("0");
        HBox controlBox = new HBox(10, label, orderField);

        // Layout setup
        BorderPane pane = new BorderPane();
        pane.setCenter(canvas);
        pane.setBottom(controlBox);

        // Draw fractal on input
        orderField.setOnAction(e -> {
            int order;
            try {
                order = Integer.parseInt(orderField.getText());
                if (order < 0) throw new NumberFormatException(); // Only allow non-negative orders
            } catch (NumberFormatException ex) {
                order = 0; // Default to 0 if input is invalid
            }
            drawKochSnowflake(gc, order);
        });

        // Initial fractal drawing
        drawKochSnowflake(gc, 0);

        // Set stage
        Scene scene = new Scene(pane, 600, 650);
        primaryStage.setTitle("Koch Snowflake Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawKochSnowflake(GraphicsContext gc, int order) {
        // Clear canvas
        gc.clearRect(0, 0, 600, 600);

        // Define initial triangle vertices
        double width = 400;
        double height = Math.sqrt(3) / 2 * width;
        double x1 = 100, y1 = 400;
        double x2 = x1 + width, y2 = y1;
        double x3 = x1 + width / 2, y3 = y1 - height;

        // Draw three edges of the Koch snowflake
        gc.setStroke(Color.BLACK);
        drawKochEdge(gc, x1, y1, x2, y2, order);
        drawKochEdge(gc, x2, y2, x3, y3, order);
        drawKochEdge(gc, x3, y3, x1, y1, order);
    }

    private void drawKochEdge(GraphicsContext gc, double x1, double y1, double x2, double y2, int order) {
        if (order == 0) {
            gc.strokeLine(x1, y1, x2, y2);
        } else {
            // Calculate points dividing the line into thirds
            double dx = x2 - x1;
            double dy = y2 - y1;
            double xA = x1 + dx / 3;
            double yA = y1 + dy / 3;
            double xB = x1 + 2 * dx / 3;
            double yB = y1 + 2 * dy / 3;

            // Calculate peak of the triangle
            double xPeak = xA + (dx / 3 - dy * Math.sqrt(3) / 6);
            double yPeak = yA + (dy / 3 + dx * Math.sqrt(3) / 6);

            // Recursively draw each segment
            drawKochEdge(gc, x1, y1, xA, yA, order - 1);
            drawKochEdge(gc, xA, yA, xPeak, yPeak, order - 1);
            drawKochEdge(gc, xPeak, yPeak, xB, yB, order - 1);
            drawKochEdge(gc, xB, yB, x2, y2, order - 1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

