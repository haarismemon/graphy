package main.java.controller;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import main.java.graph.Graph;
import main.java.view.MainView;
import main.java.api.WorldBankAPI;

import main.java.api.Query;

/**
 * The controller of the graph view
 *
 * @author pietrocalzini
 * @author Haaris Memon
 */
public class GraphController {

	//View
	private MainView mainView;

	public GraphController(MainView m){
		mainView = m;
		assignActionHandlers();
	}

	public void assignActionHandlers(){
		//Add a new graph to the main view
		EventHandler<CreateEvent> addGrapEndler = new EventHandler<CreateEvent>() {
		    public void handle(CreateEvent event) {
		    	Query query = WorldBankAPI.query(event.getIndicator(), event.getCountry(), Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()));
				System.out.println("Data: " + query.getData().isEmpty());
				if(query != null && !query.getData().isEmpty()){
					query.setColour(event.getColor());
					String title = event.getTitle();
		    		System.out.println(title.isEmpty());
		    		if(title.isEmpty()) {
		    			title = event.getIndicator() + " in " + event.getCountry();
		    		}
		    		mainView.addGraph(title,event.getGraphType(),query);
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

		 mainView.getInspectorPane().createButtonHandler(addGrapEndler);

		EventHandler<CreateEvent> updateGraphHandler = new EventHandler<CreateEvent>() {
			public void handle(CreateEvent event) {
				Query query = WorldBankAPI.query(event.getIndicator(), event.getCountry(), Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()));
				if(query != null && !query.getData().isEmpty()) {
					String title = event.getTitle();
					System.out.println(title.isEmpty());
					if (title.isEmpty()) {
						title = event.getIndicator() + " in " + event.getCountry();
					}
					Graph oldGraph = mainView.getInspectorPane().getSelectedGraph();
					mainView.updateGraph(oldGraph, title, event.getGraphType(), query, event.getColor());


					createEventHandler(mainView);
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Invalid Search ");
					alert.setContentText("The search you have made does not return a result.");

					alert.showAndWait();
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
		 
		 mainView.getCachePane().setCreateQueryHandlers(addGrapEndler);
	}

	private void createEventHandler(MainView currentMainView) {
		//UPDATE SELECTORS Select graph from the workspace
		EventHandler<SelectEvent> selectGraphHandler = new EventHandler<SelectEvent>() {
			public void handle(SelectEvent event) {
				currentMainView.getInspectorPane().setTitle(event.getGraph().getTitle());
				currentMainView.getInspectorPane().setIndicator(event.getGraph().getQuery().getIndicatorName());
				currentMainView.getInspectorPane().setCountry(event.getGraph().getQuery().getCountryName());
				currentMainView.getInspectorPane().setGraphType(event.getGraph().getGraphType());
				currentMainView.getInspectorPane().setStartYear(event.getGraph().getQuery().getStartYear());
				currentMainView.getInspectorPane().setEndYear(event.getGraph().getQuery().getEndYear());
				currentMainView.getInspectorPane().setUpdate();
				currentMainView.getInspectorPane().setSelectedGraph(event.getGraph());
				event.consume();
			}
		};

		mainView.selectGraphHandlers(selectGraphHandler);
	}

}