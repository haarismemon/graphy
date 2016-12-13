package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.api.Query;
import main.java.controller.DeleteCachedQuery;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.java.controller.CreateEvent;

public class CachedQueryPane extends BorderPane {

	//Indicator and country of the cached query
	private Label indicatorInfo;
	//Date of the cached query
	private Label dateInfo;
	//BUtton to delete the cached query
	private Pane deleteButton;

	//Query stored in the pane
	private Query query;

	//Delete a cached query action
	private ObjectProperty<EventHandler<DeleteCachedQuery>> deleteCachedQueryAction = new SimpleObjectProperty<EventHandler<DeleteCachedQuery>>();
	//Create a new graph based on cached query
	private ObjectProperty<EventHandler<CreateEvent>> createCachedQueryButton = new SimpleObjectProperty<EventHandler<CreateEvent>>();

	private MainView mainView;
	
	public CachedQueryPane(MainView mainView, Query q){
		super();
		this.mainView = mainView;
		this.query = q;
		this.setMinWidth(200);
		// setAlignment(Pos.CENTER);
		getStylesheets().add("css/cache-query-pane.css");
		getStyleClass().add("query-container");	

		StackPane p = new StackPane();
		p.setStyle("-fx-padding-left: 10");
		deleteButton = new Pane();
		deleteButton.setOnMouseClicked((event) -> {
			deleteCachedQueryAction.get().handle(new DeleteCachedQuery(query));
		});	

		deleteButton.getStyleClass().add("delete-query-button");

		p.getChildren().add(deleteButton);

		VBox queryDetails = new VBox(4);

		queryDetails.setOnMouseClicked((event) -> {
			createCachedQueryButton.get().handle(new CreateEvent(query.getTitle(), query.getIndicatorName(), query.getCountryName(), "BarChart", query.getColour(), "" +query.getStartYear(), "" +query.getEndYear()));
			mainView.hideCachePane();
			mainView.hideInspectorPane();
		});

		String cachedStringIndicator = (q.getIndicatorName() + " in " + q.getCountryName());

		String cachedStringYear = (q.getStartYear() +  " - " + q.getEndYear());

		indicatorInfo = new Label(cachedStringIndicator);
		dateInfo = new Label(cachedStringYear);

		queryDetails.getChildren().add(indicatorInfo);		
		queryDetails.getChildren().add(dateInfo);

		setCenter(queryDetails);
		setRight(p);
		// this.getChildren().add(queryDetails);
		// this.getChildren().add(deleteButton);

	} 

	public Query getQuery(){
		return query;
	}

	/**
	 * create handler to delete cached query
	 */
	public void deleteCachedQueryHandler(EventHandler<DeleteCachedQuery> handler) {
		deleteCachedQueryAction.set(handler);
	}

	/**
	 * create handler to create a new graph from a cached quety
	 */
	public void createCachedQueryButton(EventHandler<CreateEvent> handler) {
		createCachedQueryButton.set(handler);
	}
}
