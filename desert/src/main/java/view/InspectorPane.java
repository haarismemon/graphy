
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * This class represents the inspector pane, containing all the options to change graph parameters
 * @author pietrocalzini
 */
public class InspectorPane extends BorderPane{
	//Option Pane
	private GridPane optionPane;
	//Delete button
	private Button deleteButton;
	//Update/create button
	private Button updateButton;
	final ObservableList<String> graphType = FXCollections.observableArrayList("Bar ","Pie chart","Line chart");
	final ObservableList<String> graphColors = FXCollections.observableArrayList("Red ","Blue","Yellow","Orange");
	final ObservableList<String> graphCountries = FXCollections.observableArrayList("Italy ","United Kingdom","Russia","France",
			"Romania","Greece","Sweden","Spain");
	final ObservableList<String> graphIndicators = FXCollections.observableArrayList("Deflation","Inflation","Income","GDP");

	public InspectorPane(){
		super();
		this.getStylesheets().add("/css/inspector-pane.css");
		this.getStyleClass().add("inspector-pane");
		this.setStyle("-fx-background-color: #E7E7E7");
		drawWidgets();
	}

	private void drawWidgets() {
		HBox buttonPane = new HBox();
		buttonPane.setPrefHeight(60);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setSpacing(60.0);
		
		deleteButton = new Button("Delete");
		deleteButton.getStyleClass().add("button");
		deleteButton.getStyleClass().add("delete");
		buttonPane.getChildren().add(deleteButton);
		
		updateButton = new Button("Update");
		updateButton.getStyleClass().add("button");
		updateButton.getStyleClass().add("update");
		buttonPane.getChildren().add(updateButton);
		
		HBox topPane = new HBox();
		topPane.setAlignment(Pos.CENTER_LEFT);
		topPane.setPrefHeight(5);
		Button backButton = new Button();
		backButton.getStyleClass().add("backButton");
		topPane.getStyleClass().add("topPanel");
		topPane.getChildren().add(backButton);
		setTop(topPane);
		
		optionPane = new GridPane();
		optionPane.setVgap(16);
		optionPane.setAlignment(Pos.TOP_CENTER);
		
		VBox titleBox = new VBox();
		Label titleLabel = new Label("Graph title");
		titleLabel.getStyleClass().add("title-label");
		titleBox.getChildren().add(titleLabel);
		TextField titleField = new TextField();
		titleField.setPromptText("Graph title");
		titleField.getStyleClass().add("options");
		titleBox.getChildren().add(titleField);
		optionPane.add(titleBox,0,1);
		
		VBox indicatorBox = new VBox();
		Label indicatorLabel = new Label("Indicator");
		indicatorLabel.getStyleClass().add("title-label");
		indicatorBox.getChildren().add(indicatorLabel);
		ComboBox indicatorComboBox = new ComboBox(graphIndicators);
		indicatorComboBox.getStyleClass().add("options");
		indicatorBox.getChildren().add(indicatorComboBox);
		optionPane.add(indicatorBox,0,2);
		
		VBox countryBox = new VBox();
		Label countryLabel = new Label("Country");
		countryLabel.getStyleClass().add("title-label");
		countryBox.getChildren().add(countryLabel);
		ComboBox countryComboBox = new ComboBox(graphCountries);
		countryComboBox.getStyleClass().add("options");
		countryBox.getChildren().add(countryComboBox);
		optionPane.add(countryBox,0,3);
		
		VBox graphTypeBox = new VBox();
		Label graphTypeLabel = new Label("Graph Type");
		graphTypeLabel.getStyleClass().add("title-label");
		graphTypeBox.getChildren().add(graphTypeLabel);
		ComboBox graphTypeComboBox = new ComboBox(graphType);
		graphTypeComboBox.setPromptText("Select graph type");
		graphTypeComboBox.getStyleClass().add("options");
		graphTypeBox.getChildren().add(graphTypeComboBox);
		optionPane.add(graphTypeBox,0,4);
		
		HBox yearBox = new HBox(10);
		
		VBox leftBox = new VBox();
		
		Label startYear = new Label("Start Year");
		startYear.getStyleClass().add("title-label");
		leftBox.getChildren().add(startYear);
		
		TextField startYearComboBox = new TextField();
		startYearComboBox.setPromptText("Start year");
		startYearComboBox.getStyleClass().add("optionsYear");		
		leftBox.getChildren().add(startYearComboBox);
		
		VBox rightBox = new VBox();

		Label endYear = new Label("End Year");
		endYear.getStyleClass().add("title-label");
		rightBox.getChildren().add(endYear);
		
		TextField endYearComboBox = new TextField();
		endYearComboBox.setPrefWidth(20);
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
		ComboBox<String> colorComboBox = new ComboBox<String>(graphColors);
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
		                        String iconPath = "/color" + iconNumber + ".png";
		                        icon = new Image(iconPath);
		                    } catch(NullPointerException ex) {
		                        // in case the above image doesn't exist, use a default one
		                        String iconPath = "/color1.png";
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
		
		setCenter(optionPane);
		
		setBottom(buttonPane);
	}
	
	/**
	 * Set the create button to UPDATE mode
	 */
	public void setUpdate(){
		updateButton.setText("Update");
	}
	
	/**
	 * Set the create button to CREATE mode
	 */
	public void setADD(){
		updateButton.setText("Create");
	}
}