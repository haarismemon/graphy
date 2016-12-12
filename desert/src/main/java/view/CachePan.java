package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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

public class CachePan extends BorderPane {

	//Queries container
	private VBox container;

	public CachePan(){
		super();
		this.getStylesheets().add("css/cache-pane.css");
		this.getStyleClass().add("inspector-pane");
		this.setStyle("-fx-background-color: #E7E7E7");
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
		
		ScrollPane scrollContainer = new ScrollPane();

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
		clearCache.getStyleClass().add("button");
		buttonPane.getChildren().add(clearCache);
		setBottom(buttonPane);
		
	}

	public void setQueryHandlers(EventHandler<DeleteCachedQuery> handler){
		for(Node q : container.getChildren()){
			CachedQueryPane p = (CachedQueryPane)q;
			p.deleteCachedQueryHandler(handler);
		}
	}

	public void listQueryItems(){
		container.getChildren().clear();	
		for(Query query : CacheAPI.listCache()){
			CachedQueryPane q = new CachedQueryPane(query);
			container.getChildren().add(q);
		}
	}
}
