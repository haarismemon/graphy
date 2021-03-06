package main.java.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import main.java.api.Indicator;
import main.java.api.Query;
import main.java.api.WorldBankAPI;
import main.java.controller.SelectEvent;
import main.java.nlp.InputAnalysis;

import java.util.List;

/**
 * This class represents the search field 
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class SearchField extends BorderPane {

	/**
	 * Represents the combobox to show suggestions to indicator name
	 */
	private ComboBox comboBox;
	/**
	 * Store the main view stage
	 */
	private MainView mainView;
	/**
	 * Represnts the textfield to search for query
	 */
	private final TextField textField;

	/**
	 * Create a custom search field
	 * @param mainView - the main view stage where the searchfield is stored
	 */
	public SearchField(MainView mainView){
		super();
		this.mainView = mainView;
		VBox cacheClosePane = new VBox();
		cacheClosePane.setAlignment(Pos.CENTER);
		cacheClosePane.getStyleClass().add("cache-button-pane");
		Pane cacheClose = new Pane();
		cacheClose.getStyleClass().add("cache-button");

		cacheClose.setOnMouseClicked((event) -> {
			mainView.getCachePane().listQueryItems();
			mainView.toggleCachePane();
			mainView.getCachePane().listQueryItems();
		});
		cacheClosePane.getChildren().add(cacheClose);
		this.setLeft(cacheClosePane);

		comboBox = new ComboBox();
		comboBox.setPrefSize(540, 55);
		comboBox.setPromptText("GDP in Italy between 2010 to 2015");
		comboBox.setFocusTraversable(false);
		comboBox.setEditable(true);

		for(String s : Indicator.getAllNames()) {
			comboBox.getItems().add(s);
		}

		textField = new TextField();
		textField.setPrefSize(540, 55);

		Pane pane = new Pane(comboBox);

		//Link CSS file search-field.css
		getStylesheets().add("/css/search-field.css");
		textField.getStyleClass().add("search-field");
		comboBox.getStyleClass().add("search-field");

		//combobox event handler when enter or button clicked (search)
		comboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String input = comboBox.getEditor().getText();
					if(Indicator.hasIndicator(input)) {
						textField.setText(input);

						pane.getChildren().remove(comboBox);
						pane.getChildren().add(textField);
						textField.requestFocus();
						textField.positionCaret(input.length());
					}
					else {
						comboBox.setValue("");
						comboBox.hide();
						List<String> queryInfo = InputAnalysis.isValidCommand(input);
						List<String> autoCompleteIndicators = Indicator.getAutocomplete("");
						updateComboBox(autoCompleteIndicators);

						//make search
						searchQuery(queryInfo);
					}
				} catch(Exception e) {

				}
			}
		});

		//combobox event handler when letters pressed (updates suggestions)
		comboBox.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				removeNotFound();
				String s = comboBox.getEditor().getText();
				if (s.equals("")) comboBox.hide();
				else comboBox.show();
				comboBox.getItems().removeAll(comboBox.getItems());

				List<String> autoCompleteIndicators = Indicator.getAutocomplete(s);
				if (!autoCompleteIndicators.isEmpty()) {
					updateComboBox(autoCompleteIndicators);
				} else {
					comboBox.hide();
				}
			}
		});

		//textfield event handler when it becomes empty (go back to combobox)
		textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				removeNotFound();
				if (textField.getText().equals("")) {
					comboBox.getEditor().clear();
					pane.getChildren().remove(textField);
					pane.getChildren().add(comboBox);
				}
			}

		});

		//textfield event handler when enter or button clicked (search)
		textField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String input = textField.getText();
				textField.setText("");
				comboBox.hide();
				List<String> queryInfo = InputAnalysis.isValidCommand(input);
				List<String> autocompleteIndicators = Indicator.getAutocomplete(textField.getText());
				updateComboBox(autocompleteIndicators);

				//make search
				searchQuery(queryInfo);
			}
		});

		this.setCenter(pane);
		
	}

	/**
	 * Update combobox with new list of Indicator name suggestions
	 * @param autocompleteIndicators - list of Indicator names that are suggestions of input
	 */
	private void updateComboBox(List<String> autocompleteIndicators) {
		comboBox.getItems().clear();
		for (String string : autocompleteIndicators) {
			comboBox.getItems().add(string);
		}
	}

	/**
	 * Creates query object and displays graph on the main view.
	 * @param queryInfo - list of information about the query
	 */
	private void searchQuery(List<String> queryInfo) {
		if(queryInfo != null) {
			String indicatorName = queryInfo.get(0);
			String countryName = queryInfo.get(1);
			if(countryName.equals("all")) countryName = "world";
			int startYear = Integer.parseInt(queryInfo.get(2));
			int endYear = Integer.parseInt(queryInfo.get(3));
			Query queryResult = WorldBankAPI.query(indicatorName, countryName, startYear, endYear);
			if(queryResult != null && !queryResult.getData().isEmpty()){
				mainView.addGraph(queryResult.getTitle(), "BarChart", queryResult);

				EventHandler<SelectEvent> selectGraphHandler = new EventHandler<SelectEvent>() {
					public void handle(SelectEvent event) {
						mainView.getInspectorPane().setTitle(event.getGraph().getTitle());
						mainView.getInspectorPane().setIndicator(event.getGraph().getQuery().getIndicatorName());
						mainView.getInspectorPane().setCountry(event.getGraph().getQuery().getCountryName());
						mainView.getInspectorPane().setGraphType(event.getGraph().getGraphType());
						mainView.getInspectorPane().setStartYear(event.getGraph().getQuery().getStartYear());
						mainView.getInspectorPane().setEndYear(event.getGraph().getQuery().getEndYear());
						mainView.getInspectorPane().setUpdate();
						mainView.getInspectorPane().setSelectedGraph(event.getGraph());
						event.consume();
					}
				};

				mainView.selectGraphHandlers(selectGraphHandler);

				List<Integer> invalidYears = queryResult.getInvalidYears();
				if (invalidYears != null) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Warning about missing years");
					alert.setHeaderText("For query " + queryResult.getIndicatorName() + " " + queryResult.getCountryName()
							+ " for range " + queryResult.getStartYear() + " - " + queryResult.getEndYear()
							+ " ,there is no data for the following years in this range: ");
					String invalidYearsString = "";
					for(int y : invalidYears) {
						invalidYearsString = invalidYearsString + " " + y;
					}
					alert.setContentText(invalidYearsString);
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Invalid Search ");
				alert.setContentText("The search you have made does not return a result.");

				alert.showAndWait();
			}
		}
	}

	/**
	 * Change search field style for not found queries
	 */
	public void setNotFound(){
		textField.getStyleClass().add("search-field-not-found");
		comboBox.getStyleClass().add("search-field-not-found");
	}
	
	/**
	 * Reset search field to normal mode
	 */
	public void removeNotFound(){
		textField.getStyleClass().remove("search-field-not-found");
		comboBox.getStyleClass().remove("search-field-not-found");
	}
}
