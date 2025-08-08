/*
 * Vertically Text Show
 * Exercise: 14.4
 * Developed by PRINCE
 * ID: 12105007
 */

package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class VerticalTextApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        HBox hbox = new HBox(10); // Horizontal layout with spacing of 10 pixels
        hbox.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Add padding and center alignment

        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Text text = new Text("Java");
            text.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 22));

            // Set random color and opacity
            Color color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
            text.setFill(color);

            // Rotate the text to make it vertical
            text.setRotate(90);

            hbox.getChildren().add(text);
        }

        Scene scene = new Scene(hbox, 300, 150); // Set the window size
        primaryStage.setTitle("Exercise14_04");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
