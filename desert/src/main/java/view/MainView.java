package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.java.graph.Graph;
import main.java.controller.GraphController;
import main.java.api.Query;
import java.util.List;
import javafx.event.EventHandler;
import java.util.ArrayList;
import main.java.controller.SelectEvent;


/**
 * This class represents the main view with all its components
 * 
 * @author pietrocalzini
 * @author Haaris Memon
 */

public class MainView extends Stage {
	
	//Main container 
	private StackPane superContainer;
	//Inspector panel
	private InspectorPane inspector;
	//Cache panel
	private CachePane cachePane;
	//Main graph + inspector container
	private BorderPane container;
	//Initial message
	private Label beginningLabel;
	//Controller
	private GraphController controller;
	//GraphContainer
	private GridPane graphContainer = new GridPane();
	//Graph view
	private BorderPane root;
	//List of al the graphs
	private List<Graph> graphs = new ArrayList<Graph>();
	private SearchField searchField;

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
//		System.out.println(controller);
	}

	public void drawWidgets() {
		setTitle("Graphy");

		// Set minimum size of the window
		setMinHeight(650);
		setMinWidth(1200);

		superContainer = new StackPane();
		
		container = new BorderPane();
		container.setMinWidth(900);
		superContainer.getChildren().add(container);
		
		//Cache history
		cachePane = new CachePane();
		cachePane.setPrefWidth(310);
		cachePane.setMaxWidth(310);
		superContainer.setAlignment(cachePane, Pos.CENTER_LEFT);
		
		//Root pane
		root = new BorderPane();
		//Get the main stylesheet main.css
		root.getStylesheets().add("/css/main.css");
		root.setStyle("-fx-background-color: white");

		// Top bar containing the search bar
		HBox topBar = new HBox();
		topBar.setStyle("-fx-padding: 30 0 0 0;");
		topBar.setAlignment(Pos.BOTTOM_CENTER);
		topBar.setPrefHeight(80);
		
		//Add search field to the view
		searchField = new SearchField(this);
		topBar.getChildren().add(searchField);
				
		root.setTop(topBar);

		// Graph space
		StackPane graphSpace = new StackPane();
		graphSpace.setStyle("-fx-background-color: white");
		graphSpace.setAlignment(Pos.CENTER);
		
		//Label displayed when the application is launched and no graphs are shown
		beginningLabel = new Label("Start searching for one indicator or add a new graph");
		beginningLabel.getStyleClass().add("beginning-label");
		graphSpace.getChildren().add(beginningLabel);
		root.setCenter(graphSpace);

		graphContainer.setAlignment(Pos.CENTER);
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
		container.setCenter(root);
		
		//Inspector
		newInspectorPane();

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

	public SearchField getSearchField() {
		return searchField;
	}

	public CachePane getCachePane(){
		return cachePane;
	}
	
	/**
	 * Method used to get the number of graphs in the central space
	 */
	private int getGraphNumber(){
		return graphs.size();
	}

	public void printGraphs(){
		System.out.println(graphs.toString());
		switch(getGraphNumber()){
			case 0: root.setCenter(null); break;
			case 1: root.setCenter(graphs.get(0).getGraph()); break;
			case 2: 
				HBox graphC2 = new HBox();
				for(Graph g : graphs) graphC2.getChildren().add(g.getGraph());
				root.setCenter(graphC2);
			break;
			case 3:
				GridPane graphC3 = new GridPane();
				graphC3.add(graphs.get(0).getGraph(),0,0);
				graphC3.add(graphs.get(1).getGraph(),0,1);
				graphC3.add(graphs.get(2).getGraph(),1,0);
				root.setCenter(graphC3);
			break;
			case 4:
				GridPane graphC4 = new GridPane();
				graphC4.add(graphs.get(0).getGraph(),0,0);
				graphC4.add(graphs.get(1).getGraph(),0,1);
				graphC4.add(graphs.get(2).getGraph(),1,0);
				graphC4.add(graphs.get(3).getGraph(),1,1);
				root.setCenter(graphC4);
			break;
			default: return;
		}
	}

	/**
	 * create handler for the graph selection
	 */
	public void selectGraphHandlers(EventHandler<SelectEvent> handler) {
		for(Graph g : graphs){
			g.selectGraphHandler(handler);
		}
	}

	public void deleteGraph(Graph g){
		graphs.remove(g);
		printGraphs();
	}

	/**
	 * Add a new graph in the graph area
	 *@param graphName - the name of the graph
	 *@param graphType - the type of the graph
	 *@param query - the query representing the data to be plotted in the graph
	 */
	public void addGraph(String graphName, String graphType, Query query){
		Graph centralGraph = new Graph(graphName);
		centralGraph.addSeries("My Series", query);
		centralGraph.switchGraph(graphType);
		graphs.add(centralGraph);
		printGraphs();
	}

	/**
	 * Update a graph in the graph area
	 * @param newGraphName - the name of the graph
	 * @param newGraphType - the type of the graph
	 * @param newQuery - the query representing the data to be plotted in the graph
	 */
	public void updateGraph(Graph oldGraph, String newGraphName, String newGraphType, Query newQuery, String color){
		Graph centralGraph = new Graph(newGraphName);
		centralGraph.addSeries("My Series", newQuery);
		centralGraph.switchGraph(newGraphType);
		centralGraph.changeColor(color);
		int indexOfOldGraph = graphs.indexOf(oldGraph);
		if(indexOfOldGraph != -1) {
			graphs.remove(graphs.indexOf(oldGraph));
			graphs.add(indexOfOldGraph, centralGraph);
			printGraphs();
		}
	}

	public void hideInspectorPane() {
		container.setRight(null);
	}

	public void newInspectorPane() {
		inspector = new InspectorPane(this);
		inspector.setPrefWidth(300);
		container.setRight(inspector);
	}
}