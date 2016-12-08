package main.java.view;

import javafx.scene.control.TextField;

/**
 * This class represents the search field 
 * @author pietrocalzini
 */
public class SearchField extends TextField {
	
	/**
	 * Create a custom search field
	 */
	public SearchField(){
		super();
		
		//Link CSS file search-field.css
		getStylesheets().add("./main/java/view/css/search-field.css");
		getStyleClass().add("search-field");
		
		//set placeholder text
		setPromptText("Income in Italy between 2010 to 2015");
		setPrefSize(540, 55);
		setNotFound();
		
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
