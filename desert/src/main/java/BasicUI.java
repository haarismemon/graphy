
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);
        final ComboBox indicatorComboBox = new ComboBox();
        indicatorComboBox.getItems().addAll("GDP/National income", "Unemployment");
        final ComboBox countryComboBox = new ComboBox();
        countryComboBox.getItems().addAll("HK","US","AU");
        TextField textField1 = new TextField();
        TextField textField2 = new TextField ();
        hBox.getChildren().addAll(indicatorComboBox,countryComboBox,textField1,textField2);

        StackPane graphPane = new StackPane();
        graphPane.getChildren().add(graph.getGraph());

        BorderPane root = new BorderPane(graphPane);
        root.setTop(hBox);

        Scene scene = new Scene(root,800,400);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:    graph.reset();
                        TreeMap corresbondingSeries = new TreeMap();
                        switch((String)indicatorComboBox.getValue()){
                            case "GDP/National income": corresbondingSeries = MyWorldBank.getGDPGrowth((String)countryComboBox.getValue(),Integer.parseInt(textField1.getText()),Integer.parseInt(textField2.getText()));break;
                            case "Unemployment": corresbondingSeries = MyWorldBank.getUnemploymentTotal((String)countryComboBox.getValue(),Integer.parseInt(textField1.getText()),Integer.parseInt(textField2.getText()));break;
                        }
                                    graph.addSeries((String)countryComboBox.getValue(),corresbondingSeries);
                                    ; break;
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
