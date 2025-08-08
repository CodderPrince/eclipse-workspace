package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import java.util.Collections;
import java.util.ArrayList;

public class MemoryMatchGame extends Application {

    private static final int GRID_SIZE = 4; // 4x4 grid
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private String[][] values = new String[GRID_SIZE][GRID_SIZE];
    private Button firstSelected = null;
    private Button secondSelected = null;
    private boolean waiting = false;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        initializeValues();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                button.setFont(Font.font(20));
                button.setStyle("-fx-background-color: lightblue;");

                final int r = row, c = col;
                button.setOnAction(e -> handleButtonClick(button, r, c));

                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Match Game");
        primaryStage.show();
    }

    private void initializeValues() {
        ArrayList<String> cardValues = new ArrayList<>();

        for (int i = 1; i <= (GRID_SIZE * GRID_SIZE) / 2; i++) {
            cardValues.add(String.valueOf(i));
            cardValues.add(String.valueOf(i));
        }

        Collections.shuffle(cardValues);

        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                values[row][col] = cardValues.get(index);
                index++;
            }
        }
    }

    private void handleButtonClick(Button button, int row, int col) {
        if (waiting || button.getText().length() > 0) {
            return;
        }

        button.setText(values[row][col]);
        button.setStyle("-fx-background-color: white; -fx-border-color: black;");

        if (firstSelected == null) {
            firstSelected = button;
        } else {
            secondSelected = button;
            waiting = true;

            if (firstSelected.getText().equals(secondSelected.getText())) {
                firstSelected.setDisable(true);
                secondSelected.setDisable(true);
                resetSelection();
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    javafx.application.Platform.runLater(() -> {
                        firstSelected.setText("");
                        firstSelected.setStyle("-fx-background-color: lightblue;");
                        secondSelected.setText("");
                        secondSelected.setStyle("-fx-background-color: lightblue;");
                        resetSelection();
                    });
                }).start();
            }
        }
    }

    private void resetSelection() {
        firstSelected = null;
        secondSelected = null;
        waiting = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
