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
import main.java.controller.GraphController;

/**
 * This class represents the main view with all its components
 * 
 * @author pietrocalzini
 */

public class MainView extends Stage {
	
	//Inspector panel
	private InspectorPane inspector;
	//Main container
	private HBox container;
	//Initial message
	private Label beginningLabel;
	//Graph view
	private BorderPane root;
	//Controller
	private GraphController controller;
	//GraphContainer
	private GridPane graphContainer;
	//Search field
	private SearchField searchField;

	public MainView(){
		super();
	}

	/**
 	* Assign controller to the view
 	* 
 	* @param controller to be assigned to the view
 	*/
	public void assignController(GraphController controller){
		this.controller = controller;
		System.out.println(controller);
	}

	public void start() {
		setTitle("Graphy");

		// Set minimum size of the window
		setMinHeight(650);
		
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
		searchField = new SearchField();
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
		
		graphContainer = new GridPane();
		graphContainer.getStyleClass().add("graph-container");
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
		System.out.println(controller);
		addButton.setOnAction(controller);
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

		setScene(scene);
		setResizable(true);
		show();
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

	public String getTextField(){
		return searchField.getSearch();
	}

	public void invalidQuery(){
		searchField.setNotFound();
	}

	public void removeNotFound(){
		searchField.removeNotFound();
	}

	public void addGraph(String graphName, String graphType, Map<Integer, Double> graphMap){
		Graph centralGraph = new Graph("graphName");
		centralGraph.addSeries("My Serie", graphMap);
		centralGraph.switchGraph("PieChart");
		graphContainer.add(centralGraph.getGraph(),0,0);
		System.out.println("GRAPH ADDED");
	}
}