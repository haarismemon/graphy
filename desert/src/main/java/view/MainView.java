package main.java.view;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
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
		getIcons().add(new Image("/images/title-icon.png"));

		// Set minimum size of the window
		setMinHeight(670);
		setMinWidth(1200);
		superContainer = new StackPane();
		
		container = new BorderPane();
		container.setMinWidth(900);
		superContainer.getChildren().add(container);
		
		//Cache history
		cachePane = new CachePane(this);
		cachePane.setPrefWidth(300);
		cachePane.setMaxWidth(300);
		superContainer.setAlignment(cachePane, Pos.CENTER_LEFT);
		
		//Root pane
		root = new BorderPane();
		//Get the main stylesheet main.css
		root.getStylesheets().add("/css/main.css");
		root.setStyle("-fx-background-color: white");

		// Top bar containing the search bar
		HBox topBar = new HBox();
		topBar.setStyle("-fx-padding: 30 0 0 0;");
		topBar.setAlignment(Pos.BASELINE_CENTER);
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
//		root.setCenter(graphContainer);
		
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
			newInspectorPane(true);
			showInspectorPane();
		});
		addButton.getStyleClass().add("button-add");
		addButton.setPrefSize(18, 18);
		bottomBar.getChildren().add(addButton);
		
		root.setBottom(bottomBar);
		root.setMinWidth(900);
		container.setCenter(root);

		inspector = new InspectorPane(this);
		inspector.setPrefWidth(300);
		//Inspector
		//make new inspector pane and show it
//		newInspectorPane(true);
//		showInspectorPane();

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
	 * Method used to hide the cache pane
	 */
	public void hideCachePane() {
		if(superContainer.getChildren().contains(cachePane)){
			superContainer.getChildren().remove(cachePane);
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

		//if when adding a graph, the graph container is not in root, add it to the center of root
		if(!root.getCenter().equals(graphContainer)) {
			root.setCenter(graphContainer);
		}

		switch(getGraphNumber()){
			case 0: root.setCenter(beginningLabel); 
			break;
			case 1: 
				HBox graphC1 = new HBox();
				graphC1.getStyleClass().add("graph-container");
				graphC1.setAlignment(Pos.CENTER);
				graphC1.getChildren().add(graphs.get(0).getGraph());
				root.setCenter(graphC1); 
			break;
			case 2: 
				HBox graphC2 = new HBox();
				graphC2.setAlignment(Pos.CENTER);
				graphC2.getStyleClass().add("graph-container");
				for(Graph g : graphs) graphC2.getChildren().add(g.getGraph());
				root.setCenter(graphC2);
			break;
			case 3:
				GridPane graphC3 = new GridPane();
				graphC3.setAlignment(Pos.CENTER);
				graphC3.getStyleClass().add("graph-container");
				graphC3.add(graphs.get(0).getGraph(),0,0);
				graphC3.add(graphs.get(1).getGraph(),0,1);
				graphC3.add(graphs.get(2).getGraph(),1,0);
				root.setCenter(graphC3);
			break;
			case 4:
				GridPane graphC4 = new GridPane();
				graphC4.setAlignment(Pos.CENTER);
				graphC4.getStyleClass().add("graph-container");
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
		if(root.getCenter() == null) {
			root.setCenter(beginningLabel);
		}
	}

	/**
	 * Add a new graph in the graph area
	 *@param graphName - the name of the graph
	 *@param graphType - the type of the graph
	 *@param query - the query representing the data to be plotted in the graph
	 */
	public void addGraph(String graphName, String graphType, Query query){
		Graph centralGraph = new Graph(this, graphName);
		// centralGraph.setYaxis(yAxis);
		centralGraph.addSeries("My Series", query);
		centralGraph.switchGraph(graphType);
		if(graphs.size() < 4){
			graphs.add(centralGraph);
		} else {
			graphs.set(graphs.size() % 4,centralGraph);
		}
		printGraphs();
	}

	//Update a graph only in the range
	public void updateGraphRange(Graph g, Query q, String title){
		Graph graph = graphs.get(graphs.indexOf(g));
		graph.reset();
		graph.setGraphName(title);
		graph.addSeries("",q);
		printGraphs();
	}

	/**
	 * Update a graph in the graph area
	 * @param newGraphName - the name of the graph
	 * @param newGraphType - the type of the graph
	 * @param newQuery - the query representing the data to be plotted in the graph
	 */
	public void updateGraph(Graph oldGraph, String newGraphName, String newGraphType, Query newQuery, String color){
		Graph centralGraph = new Graph(this, newGraphName);
		// centralGraph.setYaxis(yAxis);
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

	public void newInspectorPane(Boolean isAdd) {
		List<ObjectProperty> listOfButtonActions = inspector.getButtonActions();
		inspector = new InspectorPane(this);

		inspector.setPrefWidth(300);
		if(isAdd == true) inspector.setAdd();
		else  inspector.setUpdate();

		inspector.setButtonActions(listOfButtonActions);

//		inspector.clearStartYear();
//		inspector.clearEndYear();
//		inspector.setSelectedGraph(null);

//		inspector.setAdd();

	}

	public void showInspectorPane() {
		container.setRight(inspector);
	}
}
