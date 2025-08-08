package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class SortingAnimation extends Application {

    private static final int ARRAY_SIZE = 50;
    private static final int BAR_WIDTH = 10;
    private static final int SLEEP_TIME = 500;

    @Override
    public void start(Stage primaryStage) {
        // Create three panes for the animations
        Pane selectionPane = new Pane();
        Pane insertionPane = new Pane();
        Pane bubblePane = new Pane();

        // Create random arrays
        List<Integer> array1 = generateRandomArray();
        List<Integer> array2 = new ArrayList<>(array1);
        List<Integer> array3 = new ArrayList<>(array1);

        // Display the arrays in the panes
        displayArray(selectionPane, array1);
        displayArray(insertionPane, array2);
        displayArray(bubblePane, array3);

        // Create sorting threads
        new Thread(() -> selectionSort(array1, selectionPane)).start();
        new Thread(() -> insertionSort(array2, insertionPane)).start();
        new Thread(() -> bubbleSort(array3, bubblePane)).start();

        // Create an HBox to hold the panes
        HBox hBox = new HBox(10, selectionPane, insertionPane, bubblePane);

        // Set up the stage
        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sorting Animation");
        primaryStage.show();
    }

    private List<Integer> generateRandomArray() {
        List<Integer> array = new ArrayList<>();
        for (int i = 1; i <= ARRAY_SIZE; i++) {
            array.add(i);
        }
        Collections.shuffle(array);
        return array;
    }

    private void displayArray(Pane pane, List<Integer> array) {
        pane.getChildren().clear();
        for (int i = 0; i < array.size(); i++) {
            Rectangle bar = new Rectangle(i * BAR_WIDTH, 0, BAR_WIDTH - 1, array.get(i) * 5);
            bar.setFill(Color.BLACK);
            bar.setTranslateY(250 - array.get(i) * 5);
            pane.getChildren().add(bar);
        }
    }

    private void selectionSort(List<Integer> array, Pane pane) {
        for (int i = 0; i < array.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.size(); j++) {
                if (array.get(j) < array.get(minIndex)) {
                    minIndex = j;
                }
            }
            Collections.swap(array, i, minIndex);
            displayArray(pane, array);
            sleep();
        }
    }

    private void insertionSort(List<Integer> array, Pane pane) {
        for (int i = 1; i < array.size(); i++) {
            int current = array.get(i);
            int j = i - 1;
            while (j >= 0 && array.get(j) > current) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, current);
            displayArray(pane, array);
            sleep();
        }
    }

    private void bubbleSort(List<Integer> array, Pane pane) {
        for (int i = 0; i < array.size() - 1; i++) {
            for (int j = 0; j < array.size() - 1 - i; j++) {
                if (array.get(j) > array.get(j + 1)) {
                    Collections.swap(array, j, j + 1);
                }
            }
            displayArray(pane, array);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
