package main.java.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.java.api.Indicator;
import main.java.controller.CreateEvent;
import main.java.controller.DeleteEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.api.Country;
import main.java.graph.Graph;


/**
 * This class represents the inspector pane, containing all the options to change graph parameters
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class InspectorPane extends BorderPane{

	private MainView mainView;

	//Option Pane
	private GridPane optionPane;
	//Delete button
	private Button deleteButton;
	//create button
	private Button createButton;
	//Update button
	private Button updateButton;
	//search button
	private Button searchButton;
	//information button
	private Button infoButton;

	//List of supported graph types
	final ObservableList<String> graphType = FXCollections.observableArrayList("Bar Chart","Pie Chart","Line Graph");
	//List of supported graph colors and tints
	final ObservableList<String> graphColors = FXCollections.observableArrayList("Red ","Blue","Yellow", "Orange", "Green", "Purple", "Black");
	//List of all supoorted countries
	final ObservableList<String> graphCountries = FXCollections.observableArrayList(Arrays.asList(Country.getAllNames()));
	//List of all supported indicators (full names)
	final ObservableList<String> graphIndicators = FXCollections.observableArrayList(Arrays.asList(Indicator.getAllNames()));

	//Graph title
	private TextField titleField;
	//Indicator Names
	private ComboBox<String> indicatorComboBox;
	//Countries
	private ComboBox<String> countryComboBox;
	//Graph types
	private ComboBox<String> graphTypeComboBox;
	//Start year
	private TextField startYearComboBox;
	//End year
	private TextField endYearComboBox;
	//Colors
	private ComboBox<String> colorComboBox;

	//Create a new graph action
	private ObjectProperty<EventHandler<CreateEvent>> createButtonAction;

	//Update graph action
	private ObjectProperty<EventHandler<CreateEvent>> updateButtonAction;

	//Delete graph action
	private ObjectProperty<EventHandler<DeleteEvent>> deleteButtonAction;

	//The selected graph 
	private Graph selectedGraph;
	//panel of buttons for delete and create/update
	private HBox buttonPane;
	//pane that holds the inspector pane
	private Pane inspectorContents;

	public InspectorPane(MainView mainView){
		super();
		this.mainView = mainView;
		this.getStylesheets().add("css/inspector-pane.css");
		this.getStyleClass().add("inspector-pane");
		this.setStyle("-fx-background-color: #E7E7E7");

		createButtonAction = new SimpleObjectProperty<EventHandler<CreateEvent>>();
		updateButtonAction = new SimpleObjectProperty<EventHandler<CreateEvent>>();
		deleteButtonAction = new SimpleObjectProperty<EventHandler<DeleteEvent>>();

		drawWidgets();
	}

	private void drawWidgets() {
		searchButton = new Button("Search");
		searchButton.setPrefSize(300, 40);
		searchButton.getStyleClass().add("toggle-button");
		
		infoButton = new Button("Information");
		infoButton.setPrefSize(300, 40);
		infoButton.getStyleClass().add("toggle-button");

		buttonPane = new HBox();
		buttonPane.setPrefHeight(60);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setSpacing(60.0);

		deleteButton = new Button("Delete");

		deleteButton.setOnAction((event) -> {
			deleteButtonAction.get().handle(new DeleteEvent(selectedGraph));
			mainView.hideCachePane();
			mainView.hideInspectorPane();
		});

		deleteButton.getStyleClass().add("button");
		deleteButton.getStyleClass().add("delete");
		buttonPane.getChildren().add(deleteButton);

		createButton = new Button("Create");
		//TODO need to change button action for create button and upate button
		createButton.setOnAction((event) -> {
//			System.out.println(getTitle() + ". " + getIndicator() + ". " + getCountry()  + ". " +getGraphType() + ". " + getColor() + ". " + getStartYear() + ". " + getEndYear());
			createButtonAction.get().handle(new CreateEvent(getTitle(), getIndicator(), getCountry() ,getGraphType(), getColor(), getStartYear(), getEndYear()));
			mainView.hideCachePane();
			mainView.hideInspectorPane();
		});

		createButton.getStyleClass().add("button");
		createButton.getStyleClass().add("update");
		buttonPane.getChildren().add(createButton);

		updateButton = new Button("Update");
		updateButton.setOnAction((event) -> {
			System.out.println(getTitle() + ". " + getIndicator() + ". " + getCountry()  + ". " +getGraphType() + ". " + getColor() + ". " + getStartYear() + ". " + getEndYear());
			updateButtonAction.get().handle(new CreateEvent(getTitle(), getIndicator(), getCountry() ,getGraphType(), getColor(), getStartYear(), getEndYear()));
			mainView.hideCachePane();
			mainView.hideInspectorPane();
		});
		updateButton.getStyleClass().add("button");
		updateButton.getStyleClass().add("update");
//		buttonPane.getChildren().add(updateButton);

		HBox toggleButtons = new HBox(searchButton, infoButton);

		BorderPane topPane = new BorderPane();

		BorderPane topContainer = new BorderPane();
		Pane backButton = new Pane();
		topContainer.setLeft(backButton);
		backButton.getStyleClass().add("backButton");
		topPane.setTop(topContainer);
		topPane.setCenter(toggleButtons);

		setTop(topPane);

		optionPane = new GridPane();
		optionPane.setVgap(16);
		optionPane.setAlignment(Pos.TOP_CENTER);

		VBox titleBox = new VBox();
		Label titleLabel = new Label("Graph title");
		titleLabel.getStyleClass().add("title-label");
		titleBox.getChildren().add(titleLabel);
		titleField = new TextField();
		titleField.setPromptText("Graph title");
		titleField.getStyleClass().add("options");
		titleBox.getChildren().add(titleField);
		optionPane.add(titleBox,0,1);

		VBox indicatorBox = new VBox();
		Label indicatorLabel = new Label("Indicator");
		indicatorLabel.getStyleClass().add("title-label");
		indicatorBox.getChildren().add(indicatorLabel);
		indicatorComboBox = new ComboBox<String>(graphIndicators);
		indicatorComboBox.getSelectionModel().selectFirst();
		indicatorComboBox.getStyleClass().add("options");
		indicatorBox.getChildren().add(indicatorComboBox);
		optionPane.add(indicatorBox,0,2);

		VBox countryBox = new VBox();
		Label countryLabel = new Label("Country");
		countryLabel.getStyleClass().add("title-label");
		countryBox.getChildren().add(countryLabel);
		countryComboBox = new ComboBox<String>(graphCountries);
		countryComboBox.getStyleClass().add("options");
		countryComboBox.getSelectionModel().selectFirst();
		countryBox.getChildren().add(countryComboBox);
		optionPane.add(countryBox,0,3);

		VBox graphTypeBox = new VBox();
		Label graphTypeLabel = new Label("Graph Type");
		graphTypeLabel.getStyleClass().add("title-label");
		graphTypeBox.getChildren().add(graphTypeLabel);
		graphTypeComboBox = new ComboBox<String>(graphType);
		graphTypeComboBox.setPromptText("Select graph type");
		graphTypeComboBox.getSelectionModel().selectFirst();
		graphTypeComboBox.getStyleClass().add("options");
		graphTypeBox.getChildren().add(graphTypeComboBox);
		optionPane.add(graphTypeBox,0,4);

		HBox yearBox = new HBox(10);

		VBox leftBox = new VBox();

		Label startYear = new Label("Start Year");
		startYear.getStyleClass().add("title-label");
		leftBox.getChildren().add(startYear);

		startYearComboBox = new TextField();
		startYearComboBox.setPromptText("Start year");
		startYearComboBox.getStyleClass().add("optionsYear");
		leftBox.getChildren().add(startYearComboBox);

		VBox rightBox = new VBox();

		Label endYear = new Label("End Year");
		endYear.getStyleClass().add("title-label");
		rightBox.getChildren().add(endYear);

		endYearComboBox = new TextField();
		endYearComboBox.setPromptText("End year");
		endYearComboBox.getStyleClass().add("optionsYear");
		rightBox.getChildren().add(endYearComboBox);

		yearBox.getChildren().add(leftBox);
		yearBox.getChildren().add(rightBox);

		optionPane.add(yearBox,0,5);

		VBox colorBox = new VBox();
		Label colorLabel = new Label("Color");
		colorLabel.getStyleClass().add("title-label");
		colorBox.getChildren().add(colorLabel);
		colorComboBox = new ComboBox<String>(graphColors);
		colorComboBox.getSelectionModel().selectFirst();
		colorComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> p) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(item);
						if (item == null || empty) {
							setGraphic(null);
						} else {
							Image icon;
							try {
								int iconNumber = this.getIndex() + 1;
								String iconPath = "images/color" + iconNumber + ".png";
								icon = new Image(iconPath);
							} catch(NullPointerException ex) {
								// in case the above image doesn't exist, use a default one
								String iconPath = "images/color1.png";
								icon = new Image(iconPath);
							}
							ImageView iconImageView = new ImageView(icon);
							iconImageView.setFitHeight(15);
							iconImageView.setPreserveRatio(true);
							setGraphic(iconImageView);
						}
					}
				};
			}
		});
		colorComboBox.getStyleClass().add("options");
		colorBox.getChildren().add(colorComboBox);
		optionPane.add(colorBox,0,6);

		inspectorContents = new Pane(optionPane);

		optionPane.setPadding(new Insets(0,0,0,25));

		
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mainView.hideInspectorPane();
			}
		});

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				inspectorContents.getChildren().clear();
				inspectorContents.getChildren().add(optionPane);
				searchButton.getStyleClass().add("toggle-button-selected");
				infoButton.getStyleClass().remove("toggle-button-selected");

			}
		});

		infoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				infoButton.getStyleClass().add("toggle-button-selected");
				searchButton.getStyleClass().remove("toggle-button-selected");

				inspectorContents.getChildren().clear();
				Label titleLabel = new Label(indicatorComboBox.getSelectionModel().getSelectedItem());
				titleLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: 800");
				titleLabel.setPadding(new Insets(15,15,0,30));

				Label informationLabel = new Label(Indicator.getInfo(indicatorComboBox.getSelectionModel().getSelectedItem()));
				informationLabel.setPrefWidth(280);
				informationLabel.setWrapText(true);
				informationLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");
				informationLabel.setPadding(new Insets(15,15,15,30));
				BorderPane pane = new BorderPane();
				pane.setTop(titleLabel);
				pane.setCenter(informationLabel);
				inspectorContents.getChildren().add(pane);
			}
		});

		setCenter(inspectorContents);


		setBottom(buttonPane);
	}

//	private Pane putInformation(String information) {
//		Pane pane = new Pane(new Label(information));
//		return pane;
//	}

	/**
	 * Set the create button to UPDATE mode
	 */
	public void setUpdate(){
//		System.out.println("CREATE");
//		updateButton.setText("Update");
		buttonPane.getChildren().clear();
		buttonPane.getChildren().addAll(deleteButton, updateButton);
	}

	/**
	 * Set the create button to CREATE mode
	 */
	public void setAdd(){
//		updateButton.setText("Create");
		buttonPane.getChildren().clear();
		buttonPane.getChildren().addAll(deleteButton, createButton);
	}

	/**
	 * @return the title of the text field
	 */
	public String getTitle(){
		return  titleField.getText();
	}

	/**
	 * @return indicator represented in the graph
	 */
	public String getIndicator(){
		return indicatorComboBox.getSelectionModel().getSelectedItem().toString();
	}

	/**
	 * @return the graph type of the currently shown graph
	 */
	public String getGraphType(){
		switch(graphTypeComboBox.getSelectionModel().getSelectedItem().toString()) {
			case "Bar Chart": return "BarChart";
			case "Pie Chart": return "PieChart";
			case "Line Graph": return "LineGraph";
			default: return "LineGraph";
		}
	}

	public List<ObjectProperty> getButtonActions() {
		return new ArrayList<>(Arrays.asList(createButtonAction, updateButtonAction, deleteButtonAction));
	}

	public void setButtonActions(List<ObjectProperty> buttonActions) {
		if(buttonActions != null && !buttonActions.isEmpty()) {
			createButtonAction = buttonActions.get(0);
			updateButtonAction = buttonActions.get(1);
			deleteButtonAction = buttonActions.get(2);
		}
	}

	/**
	 * @return the country represented in the graph
	 */
	public String getCountry(){
		return countryComboBox.getSelectionModel().getSelectedItem().toString();
	}

	/**
	 * @return get the exadecimal value for the color of the graph
	 */
	public String getColor(){
		switch(colorComboBox.getSelectionModel().getSelectedItem().toString()) {
			case "Red": return "#F05350";
			case "Yellow": return "#FFEF58";
			case "Blue": return "#29B7F7";
			case "Orange": return "#FFA826";
			case "Green": return "#009b0f";
			case "Purple": return "#7800c4";
			case "Black": return "#000000";
			default: return "#F05350";
		}
	}

	/**
	 * @return get the start year of the graph
	 */
	public String getStartYear(){
		String startDateString = startYearComboBox.getText();
		if(startDateString.equals("")) return "0";
		else return startDateString;
	}

	/**
	 * @return get the start end of the graph
	 */
	public String getEndYear(){
		String endDateString = endYearComboBox.getText();
		if(endDateString.equals("")) return "0";
		else return endDateString;
	}


	/**
	 * @return the title of the text field
	 */
	public void setTitle(String title){
		titleField.setText(title);
	}


	/**
	 * @param indicator - indicator to tbe displayed in the indicator comboBox
	 */
	public void setIndicator(String indicator){
		indicatorComboBox.setValue(indicator);
	}


	/**
	 * @param country - country to be displaye in the country comboBox
	 */
	public void setCountry(String country){
		countryComboBox.setValue(country);
	}


	/**
	 * @param color - color to be displayed on the color comboBox
	 */
	public void setColor(String color){
		colorComboBox.setValue(color);
	}

	/**
	 * @return the title of the text field
	 */
	public void setGraphType(String type){
		switch(type) {
			case "BarChart": graphTypeComboBox.setValue("Bar Chart"); break;
			case "PieChart": graphTypeComboBox.setValue("Pie Chart"); break;
			case "LineGraph": graphTypeComboBox.setValue("Line Graph"); break;
			default: graphTypeComboBox.setValue("Line Graph");
		}
	}

	/**
	 * @param year - year to be displayed on the start year text field
	 */
	public void setStartYear(int year){
		if(year == 0) startYearComboBox.clear();
		else startYearComboBox.setText("" + year);
	}

	/**
	 * clear the year to be displayed on the start year text field
	 */
	public void clearStartYear(){
		startYearComboBox.clear();
	}


	/**
	 * @param year - year to be displayed on the end year text field
	 */
	public void setEndYear(int year){
		if(year == 0) endYearComboBox.clear();
		else endYearComboBox.setText("" + year);
	}

	/**
	 * clear the year to be displayed on the start year text field
	 */
	public void clearEndYear(){
		endYearComboBox.clear();
	}

	/**
	 * create handler for the 'create graph' button
	 */
	public void createButtonHandler(EventHandler<CreateEvent> handler) {
		createButtonAction.set(handler);
	}

	/**
	 * create handler for the 'update graph' button
	 */
	public void updateButtonHandler(EventHandler<CreateEvent> handler) {
		updateButtonAction.set(handler);
	}

	/**
	 * create handler for the 'delete graph' button
	 */
	public void deleteButtonHandler(EventHandler<DeleteEvent> handler) {
		deleteButtonAction.set(handler);
	}

	/**
	 * Gets the graph that is selected and stored in the Inspector Pane
	 * @return The selected graph object
	 */
	public Graph getSelectedGraph() {
		return selectedGraph;
	}

	/**
	 * Set and store the graph object that is selected
	 * @param selectedGraph - Graph object that is selected in the main view
	 */
	public void setSelectedGraph(Graph selectedGraph) {
		this.selectedGraph = selectedGraph;
	}
}