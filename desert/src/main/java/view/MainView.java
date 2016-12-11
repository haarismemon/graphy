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
import main.java.view.CachePan;

/**
 * This class represents the main view with all its components
 * 
 * @author pietrocalzini
 */

public class MainView extends Stage {
	
	//Main container 
	private StackPane superContainer;
	//Inspector panel
	private InspectorPane inspector;
	//Cache panel
	private CachePan cachePane;
	//Main graph + inspector container
	private HBox container;
	//Initial message
	private Label beginningLabel;
	//Controller
	private GraphController controller;
	//GraphContainer
	private GridPane graphContainer;
	//Graph view
	private BorderPane root;
	
	public MainView(){
		super();
		drawWidgets();
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

	public void drawWidgets() {
		setTitle("Graphy");

		// Set minimum size of the window
		setMinHeight(650);
		
		superContainer = new StackPane();
		
		container = new HBox();
		container.setMinWidth(900);
		superContainer.getChildren().add(container);
		
		//Cache history
		cachePane = new CachePan();
		cachePane.setPrefWidth(200);
		cachePane.setMaxWidth(250);
		superContainer.setAlignment(cachePane, Pos.CENTER_LEFT);
//		superContainer.getChildren().add(cachePane);
		
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
			toggleCachePane();
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
		container.getChildren().add(inspector);

		Scene scene = new Scene(superContainer);

		setScene(scene);
		setResizable(true);
	}
	
	public void start(){
		show();
	}

	/**
	 * Remove initial message from the graph space
	 */
	public void removeInitialMessage(){
		root.setCenter(null);
	}
	
	/**
	 * Method used to hide and show the cache pane
	 */
	public void toggleCachePane() {
		if(superContainer.getChildren().contains(cachePane)){
			superContainer.getChildren().remove(cachePane);
		} else {
			superContainer.getChildren().add(cachePane);
		}
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

	public InspectorPane getInspectorPane(){
		return inspector;
	}

	/**
	 * Add a new graph in the graph area
	 *@param graphName - the name of the graph
	 *@param graphType - the type of the graph
	 *@param graphMap - the map that contains the data to be plotted in the graph
	 */
	public void addGraph(String graphName, String graphType, Map<Integer, Double> graphMap){
		Graph centralGraph = new Graph(graphName);
		centralGraph.addSeries("My Serie", graphMap);
		centralGraph.switchGraph(graphType);
		root.setCenter(centralGraph.getGraph());
	}
}