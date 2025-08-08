/*
 * Display a Checkerboard(Color Version)
 * Exercise: 14.6
 * Developed by PRINCE
 * ID: 12105007
 */

package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CheckerboardApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        int rows = 8; // Number of rows
        int cols = 8; // Number of columns
        double tileSize = 50; // Size of each tile

        GridPane gridPane = new GridPane(); // Layout container

        // Loop to create the checkerboard pattern
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Create a rectangle for each cell
                Rectangle rectangle = new Rectangle(tileSize, tileSize);

                // Set the fill color alternately black and white
                if ((row + col) % 2 == 0) {
                    rectangle.setFill(Color.WHITE);
                } else {
                    rectangle.setFill(Color.GREEN);
                }

                // Add the rectangle to the grid
                gridPane.add(rectangle, col, row);
            }
        }

        Scene scene = new Scene(gridPane, rows * tileSize, cols * tileSize); // Set scene size
        primaryStage.setTitle("Exercise14_06"); // Set the title of the stage
        primaryStage.setScene(scene); // Attach the scene to the stage
        primaryStage.show(); // Display the stage
    }

    public static void main(String[] args) {
        launch(args);
    }
}
