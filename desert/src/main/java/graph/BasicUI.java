package graph;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.util.Map;
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
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);
        final ComboBox indicatorComboBox = new ComboBox();
        indicatorComboBox.getItems().addAll("GDP/National income", "Unemployment");
        final CheckComboBox countryComboBox = new CheckComboBox();
        countryComboBox.getItems().addAll("HK","US","AU");
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        final ComboBox graphType = new ComboBox();
        graphType.getItems().addAll("LineGraph","BarChart", "PieChart","AreaChart");
        hBox.getChildren().addAll(indicatorComboBox,countryComboBox,textField1,textField2,graphType);

        BorderPane root = new BorderPane(graph.getGraph());
        root.setTop(hBox);
        graph.addSeries("testing",testSeries  );
        Scene scene = new Scene(root,800,400);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:graph.reset();
                        Map corresbondingSeries = new TreeMap();
                        ObservableList<String> list = countryComboBox.getCheckModel().getCheckedItems();
                        for(String s:list) {
                            switch ((String) indicatorComboBox.getValue()) {
                                case "GDP/National income":
                                    corresbondingSeries = api.MyWorldBank.getGDPGrowth(s, Integer.parseInt(textField1.getText()), Integer.parseInt(textField2.getText()));
                                    break;
                                case "Unemployment":
                                    corresbondingSeries = api.MyWorldBank.getUnemploymentTotal(s, Integer.parseInt(textField1.getText()), Integer.parseInt(textField2.getText()));
                                    break;
                            }
                            graph.addSeries("China", corresbondingSeries);


                        }
                        graph.switchGraph((String) graphType.getValue());
                        break;
                    case SPACE:;
                        System.out.println((String)graphType.getValue());
                        break;

                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
