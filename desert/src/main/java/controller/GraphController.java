package main.java.controller;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import main.java.api.Indicator;
import main.java.graph.Graph;
import main.java.view.InspectorPane;
import main.java.view.MainView;
import main.java.api.WorldBankAPI;
import main.java.api.Query;
import java.util.List;


/**
 * The controller of the graph view
 *
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class GraphController {

	/**
	 * Represents the Main View Stage
	 */
	private MainView mainView;

	/**
	 * Constructs the graph controller that stores the main view stage, and assigns handlers
	 * @param m
	 */
	public GraphController(MainView m){
		mainView = m;
		assignActionHandlers();
	}

	/**
	 * Assigns the action handlers for creating, updating, and deleting graphs. and also deleting cache query.
	 */
	public void assignActionHandlers(){
		//Add a new graph to the main view
		EventHandler<CreateEvent> addGraphHandler = new EventHandler<CreateEvent>() {
		    public void handle(CreateEvent event) {
		    	Query query = WorldBankAPI.query(event.getIndicator(), event.getCountry(), Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()));
				if(query != null && !query.getData().isEmpty()){
					query.setColour(event.getColor());
					String title = event.getTitle();
		    		System.out.println(title.isEmpty());
		    		if(title.isEmpty()) {
		    			title = event.getIndicator() + " in " + event.getCountry();
		    		}
		    		mainView.addGraph(title,event.getGraphType(),query);

		    		warningInvalidYears(query);

		    	} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Invalid Search ");
					alert.setContentText("The search you have made does not return a result.");

					alert.showAndWait();
				}

		    	//UPDATE SELECTROS Select graph from the workspace
				createEventHandler(mainView);

		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().createButtonHandler(addGraphHandler);

		EventHandler<CreateEvent> updateGraphHandler = new EventHandler<CreateEvent>() {
			public void handle(CreateEvent event) {
				Graph oldGraph = mainView.getInspectorPane().getSelectedGraph();
				System.out.println(oldGraph.getQuery().getIndicatorName().equals(event.getIndicator()));
				System.out.println(oldGraph.getQuery().getCountryName().equals(event.getCountry()));

				if(oldGraph.getQuery().getIndicatorName().equals(event.getIndicator()) &&
					oldGraph.getQuery().getCountryName().equals(event.getCountry()) &&
					oldGraph.getGraphType().equals(event.getGraphType())) {
					mainView.updateGraphRange(oldGraph, oldGraph.getQuery().getNewRange(Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()), event.getTitle(), event.getColor()),event.getTitle());

				} else {

					System.out.println("UPDATE NOT RANGE");
					Query query = WorldBankAPI.query(event.getIndicator(), event.getCountry(), Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()));
					if (query != null && !query.getData().isEmpty()) {
						String title = event.getTitle();
						System.out.println(title.isEmpty());
						if (title.isEmpty()) {
							title = event.getIndicator() + " in " + event.getCountry();
						}
				
						mainView.updateGraph(oldGraph, title, event.getGraphType(), query, event.getColor());
						createEventHandler(mainView);

						warningInvalidYears(query);
					} else {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText("Invalid Search ");
						alert.setContentText("The search you have made does not return a result.");
						alert.showAndWait();
					}
				}
			}
		};
		mainView.getInspectorPane().updateButtonHandler(updateGraphHandler);

		 //Delete graph from the main view
		 EventHandler<DeleteEvent> deleteGraphHandler = new EventHandler<DeleteEvent>() {
		    public void handle(DeleteEvent event) {
		    	System.out.println("DELETED");
		    	mainView.deleteGraph(event.getGraph());
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().deleteButtonHandler(deleteGraphHandler);

		 //Delete cached query from the history
		 EventHandler<DeleteCachedQuery> deleteCachedQueryHandler = new EventHandler<DeleteCachedQuery>() {
			public void handle(DeleteCachedQuery event) {
				try {
					event.getQuery().delete();
					mainView.getCachePane().removeCachePane(event.getQuery());
				} catch(Exception e){
					System.out.println("THE QUERY CAN NOT BE DELETED");
				}
				event.consume();
			}
		  };

		 mainView.getCachePane().setQueryHandlers(deleteCachedQueryHandler);
		 
		 mainView.getCachePane().setCreateQueryHandlers(addGraphHandler);
	}

	/**
	 * Creates the Alert dialog box if there are missing years in the query
	 * @param query
	 */
	private void warningInvalidYears(Query query) {
		List<Integer> invalidYears = query.getInvalidYears();
		if (invalidYears != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Warning about missing years");
			alert.setHeaderText("For query " + query.getIndicatorName() + " " + query.getCountryName() 
				+ " for range " + query.getStartYear() + " - " + query.getEndYear() 
				+ " ,there is no data for the following years in this range: ");
			String invalidYearsString = "";
			for(int y : invalidYears) {
				invalidYearsString = invalidYearsString + " " + y;
			}
			alert.setContentText(invalidYearsString);
			alert.showAndWait();
		}
	}

	/**
	 * Updates the inspector pane when a graph is selected in the main view.
	 * @param currentMainView
	 */
	private void createEventHandler(MainView currentMainView) {
		//UPDATE SELECTORS Select graph from the workspace
		EventHandler<SelectEvent> selectGraphHandler = new EventHandler<SelectEvent>() {
			public void handle(SelectEvent event) {
				InspectorPane inspectorPane = currentMainView.getInspectorPane();
				inspectorPane.setTitle(event.getGraph().getTitle());
				String indicatorName = event.getGraph().getQuery().getIndicatorName();
				inspectorPane.setIndicator(indicatorName);
				inspectorPane.setCountry(event.getGraph().getQuery().getCountryName());
				inspectorPane.setGraphType(event.getGraph().getGraphType());
				inspectorPane.setStartYear(event.getGraph().getQuery().getStartYear());
				inspectorPane.setEndYear(event.getGraph().getQuery().getEndYear());
				inspectorPane.setUpdate();
				inspectorPane.setSelectedGraph(event.getGraph());
				if(!inspectorPane.isSearch()) {
					inspectorPane.putInformation(indicatorName, Indicator.getInfo(indicatorName));
				}
				event.consume();
			}
		};

		mainView.selectGraphHandlers(selectGraphHandler);
	}

}
