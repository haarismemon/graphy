package main.java.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.api.Query;
import main.java.api.CacheAPI;
import main.java.controller.DeleteCachedQuery;
import javafx.event.EventHandler;
import javafx.scene.Node;
import main.java.controller.CreateEvent;
import javafx.scene.control.Label;

import java.util.List;

/**
 * This class represents the cached pane, containing all the cached queries
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class CachePane extends BorderPane {

	/**
	 * Container that stores each cache query pane
	 */
	private VBox container;
	/**
	 * Stores the Main View stage
	 */
	private MainView mainView;
	/**
	 * the Event Handler for the deleting cache
	 */
	private EventHandler<DeleteCachedQuery> deleteCacheHandler;
	/**
	 * Create a new graph based on cached query
	 */
	private EventHandler<CreateEvent> createCacheHandler;
	/**
	 * Label to represent if there are no cached queries
	 */
	private Label noCachedQuery;
	/**
	 * ScrollPane that holds the list of cached queries
	 */
	private ScrollPane scrollContainer;

	/**
	 * Constructs a pane for a cached query
	 * @param mainView - mainView where the cached pane is located
	 */
	public CachePane(MainView mainView){
		super();
		this.mainView = mainView;
		this.getStylesheets().add("css/cache-pane.css");
		this.getStyleClass().add("inspector-pane");
		this.setStyle("-fx-background-color: rgba(231,231,231,0.85)");
		drawWidgets();
	}

	/**
	 * Adds all the components to the cache pane
	 */
	private void drawWidgets() {
		HBox topPane = new HBox();
		topPane.getStyleClass().add("top-pane");
		topPane.setAlignment(Pos.CENTER_RIGHT);
		Pane backButton = new Pane();
		backButton.getStyleClass().add("back-button");
		topPane.getChildren().add(backButton);
		setTop(topPane);
		

		scrollContainer = new ScrollPane();

		container = new VBox(12);
		container.getStyleClass().add("query-container");
		container.setAlignment(Pos.CENTER_LEFT);

		//Display all the queries
		listQueryItems();

		scrollContainer.setContent(container);

		setCenter(scrollContainer);
		
		//Bottom pane
		HBox buttonPane = new HBox();
		buttonPane.setPrefHeight(60);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setSpacing(60.0);
		
		Button clearCache = new Button("Clear cache history");
		clearCache.setOnAction((event) -> {
			CacheAPI.clearCache();
			listQueryItems();
		});

		clearCache.getStyleClass().add("button");
		buttonPane.getChildren().add(clearCache);
		setBottom(buttonPane);

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mainView.toggleCachePane();
			}
		});
		
	}

	/**
	 * Toggles displaying the no cache label in the container
	 */
	private void toggleNoCacheLabel(){
		if(container.getChildren().size() == 0) {
			container.alignmentProperty().setValue(Pos.CENTER);
			noCachedQuery.setPadding(new Insets(220,0,0,55));
			container.getChildren().add(noCachedQuery);
		} else {
			container.getChildren().remove(noCachedQuery);
			container.setAlignment(Pos.CENTER);
		}			
	}

	/**
	 * Sets the query handlers to each cache query pane
	 * @param handler the handler to set to the cache query pane
	 */
	public void setQueryHandlers(EventHandler<DeleteCachedQuery> handler){
		deleteCacheHandler = handler;
		for(Node q : container.getChildren()){
			if(q instanceof CachedQueryPane){
				CachedQueryPane p = (CachedQueryPane)q;
				p.deleteCachedQueryHandler(handler);
			}
		}
	}

	/**
	 * create handler to create cached query
	 */
	public void setCreateQueryHandlers(EventHandler<CreateEvent> handler){
		createCacheHandler = handler;
		for(Node q : container.getChildren()){
			if(q instanceof CachedQueryPane){
				CachedQueryPane p = (CachedQueryPane)q;
				p.createCachedQueryButton(handler);
			}
		}
	}

	/**
	 * Removes the specific query cache from cache pane
	 * @param query - the cached query to remove
	 */
	public void removeCachePane(Query query){
		for(Node q : container.getChildren()){
			CachedQueryPane p = (CachedQueryPane)q;
			if(p.getQuery() == query) {
				container.getChildren().remove(q);
			}
		}
	}

	/**
	 * Updates the list of cached queries in the cache pane
	 */
	void listQueryItems(){
		container.getChildren().clear();
		List<Query> listOfQueries = CacheAPI.listCache();
		if(listOfQueries != null) {
			for(Query query : listOfQueries){
				CachedQueryPane q = new CachedQueryPane(mainView, query);
				q.deleteCachedQueryHandler(deleteCacheHandler);
				q.createCachedQueryButton(createCacheHandler);
				container.getChildren().add(q);
			}
		}

		noCachedQuery = new Label("There are no cached queries.");
		noCachedQuery.setAlignment(Pos.CENTER);
		//Add no query label is there are no cached queries
		toggleNoCacheLabel();
	}
}
