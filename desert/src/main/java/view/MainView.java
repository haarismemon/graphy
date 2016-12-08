package main.java.view;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.graph.Graph;

/**
 * This class represents the main view with all its components
 * 
 * @author pietrocalzini
 */

public class MainView extends Application {
	
	//Inspector panel
	private InspectorPane inspector;
	//Main container
	private HBox container;
	//Initial message
	private Label beginningLabel;
	//Graph view
	private BorderPane root;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Graphy");

		// Set minimum size of the window
		primaryStage.setMinHeight(650);
		
		container = new HBox();
		container.setMinWidth(900);
		
		//Root pane
		root = new BorderPane();
		//Get the main stylesheet main.css
		root.getStylesheets().add("/css/main.css");
		root.setStyle("-fx-background-color: white");

		// Top bar containing the search bar
		HBox topBar = new HBox();
		topBar.setAlignment(Pos.BOTTOM_CENTER);
		topBar.setPrefHeight(80);
		
		//Add search field to the view
		SearchField searchField = new SearchField();
		topBar.getChildren().add(searchField);
				
		root.setTop(topBar);

		// Graph space
		StackPane graphSpace = new StackPane();
		graphSpace.setStyle("-fx-background-color: white");
		graphSpace.setAlignment(Pos.CENTER);
		
//		//Label displayed when the application is launched and no graphs are shown
//		beginningLabel = new Label("Start searching for one indicator or add a new graph");
//		beginningLabel.getStyleClass().add("beginning-label");
//		graphSpace.getChildren().add(beginningLabel);
//		root.setCenter(graphSpace);
		
		GridPane graphContainer = new GridPane();
		graphContainer.getStyleClass().add("graph-container");
		
		Graph centralGraph = new Graph("My Graph");
		Map<Integer, Double> graphMap = new HashMap<Integer, Double>();
		graphMap.put(1,2.0);
		graphMap.put(2,4.0);
		graphMap.put(3,5.0);
		graphMap.put(4,2.0);
		graphMap.put(5,4.0);
		graphMap.put(6,5.0);
		centralGraph.addSeries("My Serie", graphMap);
		centralGraph.switchGraph("BarChart");
		graphContainer.add(centralGraph.getGraph(),0,0);
		
		Graph centralGraph1 = new Graph("My Graph");
		Map<Integer, Double> graphMap1 = new HashMap<Integer, Double>();
		centralGraph1.addSeries("My Serie", graphMap1);
		centralGraph1.switchGraph("LineGraph");
		graphContainer.add(centralGraph1.getGraph(),1,0);
		root.setCenter(graphContainer);
		
		Graph centralGraph2 = new Graph("My Graph");
		Map<Integer, Double> graphMap2 = new HashMap<Integer, Double>();
		centralGraph2.addSeries("My Serie", graphMap2);
		centralGraph2.switchGraph("LineGraph");
		graphContainer.add(centralGraph2.getGraph(),0,1);
		
		Graph centralGraph3 = new Graph("My Graph");
		Map<Integer, Double> graphMap3 = new HashMap<Integer, Double>();
		centralGraph3.addSeries("My Serie", graphMap3);
		centralGraph3.switchGraph("LineGraph");
		graphContainer.add(centralGraph3.getGraph(),1,1);
		root.setCenter(graphContainer);
		
		// Bottom bar containing 'add' button
		HBox bottomBar = new HBox();
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setStyle("-fx-background-color: #F3F3F3;");
		bottomBar.setPrefHeight(50);
		
		Label addNewGraph = new Label("Start adding a new graph");
		addNewGraph.getStyleClass().add("label-add");
		bottomBar.getChildren().add(addNewGraph);

		Button addButton = new Button("");
		addButton.setOnAction((event) -> {
			toggleInspector();
		});		
		addButton.getStyleClass().add("button-add");
		addButton.setPrefSize(18, 18);
		bottomBar.getChildren().add(addButton);
		
		root.setBottom(bottomBar);
		root.setMinWidth(900);
		container.getChildren().add(root);
		
		//Inspector
		inspector = new InspectorPane();
		inspector.setPrefWidth(300);
		
		Scene scene = new Scene(container);

		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
	
	/**
	 * Remove initial message from the graph space
	 */
	public void removeInitialMessage(){
		root.setCenter(null);
	}
	
	/**
	 * Method used to hide and show the inspector
	 */
	public void toggleInspector() {
		if(container.getChildren().contains(inspector)){
			container.getChildren().remove(inspector);
		} else {
			container.getChildren().add(inspector);
		}
	}
}