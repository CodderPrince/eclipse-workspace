package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    float width = 1080;
    float height = 1080;
    //float width = (float) screenBounds.getHeight();
    //float height = (float) screenBounds.getHeight();

    int order = 9;
    int N = (int) Math.pow(2, order);
    float total = N * N;

    Group group = new Group();
    Scene scene = new Scene(group, width, height);
    Line[] lines = new Line[(int) total];
    Vecc2f[] path = new Vecc2f[(int) total];
    //

    @Override
    public void start(Stage stage) throws Exception {

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F:
                    stage.setFullScreen(!stage.isFullScreen());
            }
        });

        for (int i = 0; i < total; i++) {
            path[i] = hilbert(i);
            float len = (width / N);
            path[i].mult(len);
            path[i].add(len / 2, len / 2);
        }
        //
        for (int i = 1; i < path.length; i++) {
            lines[i] = new Line(path[i].x, path[i].y, path[i - 1].x, path[i - 1].y);

            float a = map(i, 0, path.length, 0, 360);

            lines[i].setStroke(Color.hsb(a, 1, 1, 1));
            lines[i].setStrokeWidth(2);

            group.getChildren().addAll(lines[i]);
        }

        stage.setScene(scene);
        stage.show();
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public Vecc2f hilbert(int i) {
        Vecc2f[] points = {
                new Vecc2f(0, 0),
                new Vecc2f(0, 1),
                new Vecc2f(1, 1),
                new Vecc2f(1, 0)
        };
        int index = i & 3;
        Vecc2f v = points[index];
        for (int j = 1; j < order; j++) {

            i = i >>> 2;
            index = i & 3;

            float len = (float) Math.pow(2, j);

            if (index == 0) {
                float temp = v.x;
                v.x = v.y;
                v.y = temp;
            } else if (index == 1) {
                v.y += len;
            } else if (index == 2) {
                v.x += len;
                v.y += len;
            } else {
                float temp = len - 1 - v.x;
                v.x = len - 1 - v.y;
                v.y = temp;
                v.x += len;
            }
        }
        return v;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
