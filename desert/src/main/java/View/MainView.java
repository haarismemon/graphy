
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This class represents the main view with all its components
 * 
 * @author pietrocalzini
 */

public class MainView extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Graphy");

		// Set minimum size of the window
		primaryStage.setMinHeight(650);
		primaryStage.setMinWidth(1000);

		//Root pane
		BorderPane root = new BorderPane();
		//Get the main stylesheet main.css
		root.getStylesheets().add("/main.css");
		root.setStyle("-fx-background-color: white");

		// Top bar containing the search bar
		HBox topBar = new HBox();
		topBar.setAlignment(Pos.BOTTOM_CENTER);
		topBar.setPrefHeight(90);

		
		TextField searchField = new TextField("");
		searchField.setPromptText("Search for a topic");
		searchField.getStyleClass().add("search-field");
		searchField.setPrefSize(520, 55);
		topBar.getChildren().add(searchField);
				
		root.setTop(topBar);

		// Graph space
		StackPane graphSpace = new StackPane();
		graphSpace.setStyle("-fx-background-color: white");
		graphSpace.setAlignment(Pos.CENTER);
		
		//Label displayed when the application is launched and no graphs are shown
		Label beginningLabel = new Label("Start searching for one indicator or add a new graph");
		beginningLabel.getStyleClass().add("beginning-label");
		graphSpace.getChildren().add(beginningLabel);
		root.setCenter(graphSpace);

		// Bottom bar containing 'add' button
		HBox bottomBar = new HBox();
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setStyle("-fx-background-color: #F2F2F2;");
		bottomBar.setPrefHeight(50);
		
		Label addNewGraph = new Label("Start adding a new graph");
		addNewGraph.getStyleClass().add("label-add");
		bottomBar.getChildren().add(addNewGraph);

		Button addButton = new Button("");
		addButton.getStyleClass().add("button-add");
		addButton.setPrefSize(18, 18);
		bottomBar.getChildren().add(addButton);
		
		root.setBottom(bottomBar);

		
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}