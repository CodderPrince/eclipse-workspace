/*
 * Characters Around Circle(Color Version)
 * Exercise: 14.5
 * Developed by PRINCE
 * ID: 12105007
 */

package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class CircleTextApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane(); // Center-align the content
        Pane pane = new Pane(); // Container for the text
        root.getChildren().add(pane);

        String message = "WELCOME TO JAVA";
        double centerX = 185; // X-coordinate of circle center
        double centerY = 200; // Y-coordinate of circle center
        double radius = 100; // Radius of the circle
        double angleStep = 360.0 / message.length(); // Angle between each character

        Random random = new Random(); // Random generator for colors

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);

            // Calculate the angle in radians
            double angle = Math.toRadians(i * angleStep);

            // Calculate the position for the character
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            // Create and style the text
            Text text = new Text(String.valueOf(c));
            text.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));

            // Set a random color for the text
            /*
             * By default Color is RGB Format
             * RED	GREEN	BLUE
             * for three double
             */
            Color randomColor = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
            text.setFill(randomColor);

            text.setX(x);
            text.setY(y);

            // Rotate the text to align it around the circle
            text.setRotate(i * angleStep + 90); // Adjust rotation for better alignment

            pane.getChildren().add(text);
        }

        Scene scene = new Scene(root, 400, 400); // Larger scene to accommodate centering
        primaryStage.setTitle("Exercise14_05");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
