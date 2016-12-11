package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CachedQueryPane extends HBox {
	
	private Label queryInfo;
	private Pane deleteButton;
	
	public CachedQueryPane(){
		super(8);
		setAlignment(Pos.CENTER);
		getStylesheets().add("css/cache-query-pane.css");
		getStyleClass().add("query-container");
		
		deleteButton = new Pane();
		deleteButton.getStyleClass().add("delete-query-button");

		queryInfo = new Label("Income in Italy 2010-2013");
		
		this.getChildren().add(queryInfo);
		this.getChildren().add(deleteButton);

	} 
}
