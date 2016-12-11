package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.java.api.Query;

public class CachedQueryPane extends HBox {
	
	private Label indicatorInfo;
	private Label dateInfo;
	private Pane deleteButton;
	
	public CachedQueryPane(Query q){
		super(8);
		setAlignment(Pos.CENTER);
		getStylesheets().add("css/cache-query-pane.css");
		getStyleClass().add("query-container");
		
		deleteButton = new Pane();
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
}
