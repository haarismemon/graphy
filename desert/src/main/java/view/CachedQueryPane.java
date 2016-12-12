package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.java.api.Query;
import main.java.controller.DeleteCachedQuery;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.java.controller.CreateEvent;

public class CachedQueryPane extends HBox {

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
	
	public CachedQueryPane(Query q){

		super(8);
		this.query = q;
		setAlignment(Pos.CENTER);
		getStylesheets().add("css/cache-query-pane.css");
		getStyleClass().add("query-container");
		
		this.setOnMouseClicked((event) -> {
			createCachedQueryButton.get().handle(new CreateEvent(query.getTitle(), query.getIndicatorName(), query.getCountryName(), "BarChart", query.getColour(), "" +query.getStartYear(), "" +query.getEndYear()));
		});	

		deleteButton = new Pane();
		deleteButton.setOnMouseClicked((event) -> {
			deleteCachedQueryAction.get().handle(new DeleteCachedQuery(query));
		});	

		deleteButton.getStyleClass().add("delete-query-button");

		VBox queryDetails = new VBox(4);

		String cachedStringIndicator = (q.getIndicatorName() + " in " + q.getCountryName());

		String cachedStringYear = (q.getStartYear() +  " - " + q.getEndYear());

		indicatorInfo = new Label(cachedStringIndicator);
		dateInfo = new Label(cachedStringYear);

		queryDetails.getChildren().add(indicatorInfo);		
		queryDetails.getChildren().add(dateInfo);


		this.getChildren().add(queryDetails);
		this.getChildren().add(deleteButton);

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
