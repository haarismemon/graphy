package main.java.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.api.Country;
import main.java.api.Indicator;
import main.java.api.Query;
import main.java.api.WorldBankAPI;
import main.java.nlp.InputAnalysis;

import java.util.Date;
import java.util.List;

/**
 * This class represents the search field 
 * @author pietrocalzini
 */
public class SearchField extends BorderPane {

	private ComboBox comboBox;
	private MainView mainView;

	/**
	 * Create a custom search field
	 */
	public SearchField(MainView mainView){
		super();
		this.mainView = mainView;
		comboBox = new ComboBox();
		comboBox.setPrefSize(540, 55);
		comboBox.setPromptText("GDP in Italy between 2010 to 2015");
		comboBox.setFocusTraversable(false);
		comboBox.setEditable(true);

		for(String s : Indicator.getAllNames()) {
			comboBox.getItems().add(s);
		}

		TextField textField = new TextField();
		textField.setPrefSize(540, 55);

		Pane pane = new Pane(comboBox);

		//Link CSS file search-field.css
		getStylesheets().add("/css/search-field.css");
		textField.getStyleClass().add("search-field");
		comboBox.getStyleClass().add("search-field");


//		comboBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				if(!Indicator.getAutocomplete(comboBox.getEditor().getText()).isEmpty()) {
//					comboBox.show();
//				}
//			}
//		});

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

		comboBox.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
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

		textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (textField.getText().equals("")) {
					comboBox.getEditor().clear();
					pane.getChildren().remove(textField);
					pane.getChildren().add(comboBox);
				}
			}

		});


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

	private void updateComboBox(List<String> autocompleteIndicators) {
		comboBox.getItems().clear();
		for (String string : autocompleteIndicators) {
			comboBox.getItems().add(string);
		}
	}

	private void searchQuery(List<String> queryInfo) {
		if(queryInfo != null) {
			String indicatorName = queryInfo.get(0);
			String countryName = queryInfo.get(1);
			int startYear = Integer.parseInt(queryInfo.get(2));
			int endYear = Integer.parseInt(queryInfo.get(3));
//			Query query = new Query(indicatorCode, countryCode, startYear, endYear, new Date());
//			return query;
			Query queryResult = WorldBankAPI.query(indicatorName, countryName, startYear, endYear);
			System.out.println(queryResult);
			if(queryResult != null){
				mainView.addGraph(queryResult.getTitle(), "BarChart", queryResult);
			}
		}
//		else return null;
	}

	/**
	 * Change search field style for not found queries
	 */
	public void setNotFound(){
		getStyleClass().add("search-field-not-found");
	}
	
	/**
	 * Reset search field to normal mode
	 */
	public void removeNotFound(){
		getStyleClass().remove("search-field-not-found");
	}
}
