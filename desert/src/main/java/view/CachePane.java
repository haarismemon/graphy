package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.view.CachedQueryPane;
import main.java.api.Query;
import main.java.api.CacheAPI;
import main.java.controller.DeleteCachedQuery;
import javafx.event.EventHandler;
import javafx.scene.Node;
import main.java.api.CacheAPI;
import main.java.controller.CreateEvent;
import javafx.scene.control.Label;

import java.util.List;


public class CachePane extends BorderPane {

	//Queries container
	private VBox container;
	private MainView mainView;
	private EventHandler<DeleteCachedQuery> deleteCacheHandler;
	private EventHandler<CreateEvent> createCacheHandler;
	private Label noCachedQuery;
	private ScrollPane scrollContainer;

	public CachePane(MainView mainView){
		super();
		this.mainView = mainView;
		this.getStylesheets().add("css/cache-pane.css");
		this.getStyleClass().add("inspector-pane");
		this.setStyle("-fx-background-color: rgba(231,231,231,0.85)");
		drawWidgets();
	}

	public void drawWidgets() {
		
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

	public void toggleNoCacheLabel(){
		if(container.getChildren().size() == 0) {
			container.setAlignment(Pos.CENTER);
			container.getChildren().add(noCachedQuery);
		} else {
			container.getChildren().remove(noCachedQuery);
			container.setAlignment(Pos.CENTER);
		}			
	}

	public void setQueryHandlers(EventHandler<DeleteCachedQuery> handler){
		deleteCacheHandler = handler;
		for(Node q : container.getChildren()){
			if(q instanceof CachedQueryPane){
				CachedQueryPane p = (CachedQueryPane)q;
				p.deleteCachedQueryHandler(handler);
			}
		}
	}

	public void setCreateQueryHandlers(EventHandler<CreateEvent> handler){
		createCacheHandler = handler;
		for(Node q : container.getChildren()){
			if(q instanceof CachedQueryPane){
				CachedQueryPane p = (CachedQueryPane)q;
				p.createCachedQueryButton(handler);
			}
		}
	}

	public void removeCachePane(Query query){
		for(Node q : container.getChildren()){
			CachedQueryPane p = (CachedQueryPane)q;
			if(p.getQuery() == query) {
				container.getChildren().remove(q);
			}
		}
	}

	public void listQueryItems(){
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
