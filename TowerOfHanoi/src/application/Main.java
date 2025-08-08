package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Stack;

public class Main extends Application {

    private static final int DISK_HEIGHT = 20;
    private static final int DISK_WIDTH = 100;
    private static final int PEG_WIDTH = 10;
    private static final int NUM_DISKS = 4; // Change this number for more disks
    private static final int ANIMATION_DELAY = 3000;

    private Pane pane;
    private Stack<Rectangle>[] pegs;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        Scene scene = new Scene(pane, 600, 400);

        pegs = new Stack[3];
        for (int i = 0; i < 3; i++) {
            pegs[i] = new Stack<>();
        }

        drawPegs();
        initializeDisks(NUM_DISKS);

        new Thread(() -> towerOfHanoi(NUM_DISKS, 0, 1, 2)).start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi Visualizer");
        primaryStage.show();
    }

    private void drawPegs() {
        int pegSpacing = 200;
        for (int i = 0; i < 3; i++) {
            Rectangle peg = new Rectangle(pegSpacing * i + 100, 100, PEG_WIDTH, 200);
            peg.setFill(Color.GRAY);
            pane.getChildren().add(peg);
        }
    }

    private void initializeDisks(int numDisks) {
        int pegSpacing = 200;
        for (int i = 0; i < numDisks; i++) {
            int width = DISK_WIDTH - i * 20;
            Rectangle disk = new Rectangle(width, DISK_HEIGHT);
            disk.setFill(Color.hsb(i * 360.0 / numDisks, 0.8, 0.8));
            disk.setX(100 + (DISK_WIDTH - width) / 2);
            disk.setY(300 - i * DISK_HEIGHT);
            pane.getChildren().add(disk);
            pegs[0].push(disk);
        }
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

    private void moveDisk(int src, int dest) {
        Rectangle disk = pegs[src].pop();
        pegs[dest].push(disk);

        double destX = 100 + dest * 200 + (DISK_WIDTH - disk.getWidth()) / 2;
        double destY = 300 - (pegs[dest].size() - 1) * DISK_HEIGHT;

        javafx.application.Platform.runLater(() -> {
            disk.setX(destX);
            disk.setY(destY);
        });

        try {
            Thread.sleep(ANIMATION_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
