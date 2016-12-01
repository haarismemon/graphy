/**
 * Created by Evans on 11/30/2016.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.TreeMap;

public class BasicUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BasicUI");
        Graph graph = new Graph("graph 1");
        TreeMap<Integer,Double> testSeries = new TreeMap<Integer,Double>();
        testSeries.put(1996,14d);
        testSeries.put(1997,132d);
        testSeries.put(2000,299d);
        graph.addSeries("first",testSeries);
        primaryStage.setScene(graph.getGraph());
        primaryStage.show();
    }
}
