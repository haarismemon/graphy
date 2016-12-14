package main.java.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.api.Query;
import main.java.controller.DeleteCachedQuery;
import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.java.controller.CreateEvent;

/**
 * This class represents the cached query pane, containing a query that is from cache
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class CachedQueryPane extends BorderPane {

	/**
	 * Indicator and country of the cached query
	 */
	private Label indicatorInfo;
	/**
	 * Date of the cached query
	 */
	private Label dateInfo;
	/**
	 * Button to delete the cached query
	 */
	private Pane deleteButton;

	/**
	 * Query stored in the pane
	 */
	private Query query;

	/**
	 * Delete a cached query action
	 */
	private ObjectProperty<EventHandler<DeleteCachedQuery>> deleteCachedQueryAction = new SimpleObjectProperty<EventHandler<DeleteCachedQuery>>();
	/**
	 * Create a new graph based on cached query
	 */
	private ObjectProperty<EventHandler<CreateEvent>> createCachedQueryButton = new SimpleObjectProperty<EventHandler<CreateEvent>>();

	/**
	 * the Event Handler for the deleting cache
	 */
	private EventHandler<DeleteCachedQuery> deleteCachehandler;

	/**
	 * Stores the Main View stage
	 */
	private MainView mainView;

	/**
	 * Constructs a pane for a cached query
	 * @param mainView - mainView where the cached pane is located
	 * @param query - the query object which the cached query pane represents
	 */
	public CachedQueryPane(MainView mainView, Query query){
		super();
		this.mainView = mainView;
		this.query = query;
		this.setMinWidth(275);
		getStylesheets().add("css/cache-query-pane.css");
		getStyleClass().add("query-container");	

		StackPane p = new StackPane();
		p.setStyle("-fx-padding-left: 20");
		deleteButton = new Pane();
		deleteButton.setOnMouseClicked((event) -> {
			deleteCachedQueryAction.get().handle(new DeleteCachedQuery(this.query));
			mainView.getCachePane().listQueryItems();
		});	

		deleteButton.getStyleClass().add("delete-query-button");

		p.getChildren().add(deleteButton);

		VBox queryDetails = new VBox(4);

		queryDetails.setOnMouseClicked((event) -> {
			createCachedQueryButton.get().handle(new CreateEvent(this.query.getTitle(), this.query.getIndicatorName(), this.query.getCountryName(), "BarChart", this.query.getColour(), "" + this.query.getStartYear(), "" + this.query.getEndYear()));
			mainView.hideCachePane();
			mainView.hideInspectorPane();
		});

		String cachedStringIndicator = (query.getIndicatorName() + " in " + query.getCountryName());
		if(cachedStringIndicator.length() > 37) cachedStringIndicator = cachedStringIndicator.substring(0,37); 
		String cachedStringYear = (query.getStartYear() +  " - " + query.getEndYear());

		indicatorInfo = new Label(cachedStringIndicator);
		dateInfo = new Label(cachedStringYear);

		queryDetails.getChildren().add(indicatorInfo);		
		queryDetails.getChildren().add(dateInfo);

		setCenter(queryDetails);
		setRight(p);
	}

	/**
	 * Gets the query object which this pane represents
	 * @return Query object which this pane represents
	 */
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
	 * create handler to create a new graph from a cached query
	 */
	public void createCachedQueryButton(EventHandler<CreateEvent> handler) {
		createCachedQueryButton.set(handler);
	}
}
