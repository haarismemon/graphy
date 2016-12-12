package main.java.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.event.Event;
import javafx.event.EventHandler;
import main.java.view.MainView;
import main.java.api.WorldBankAPI;

import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import main.java.api.Query;
import main.java.api.CacheAPI;
import java.io.IOException;

/**
 * The controller of the graph view
 * @author pietrocalzini
 *
 */
public class GraphController{

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
		    	if(query != null){
		    		String title = event.getTitle();
		    		System.out.println(title.isEmpty());
		    		if(title.isEmpty()) {
		    			title = event.getIndicator() + " in " + event.getCountry();
		    		}
		    		mainView.addGraph(title,event.getGraphType(),query);
		    	}
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().createButtonHandler(addGrapEndler);

		 //Delete graph from the main view
		 EventHandler<DeleteEvent> deleteGraphHandler = new EventHandler<DeleteEvent>() {
		    public void handle(DeleteEvent event) {
		    	System.out.println("DELETED");
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().deleteButtonHandler(deleteGraphHandler);

		 //Delete graph from the main view
		 EventHandler<DeleteCachedQuery> deleteCachedQueryHandler = new EventHandler<DeleteCachedQuery>() {
		    public void handle(DeleteCachedQuery event) {
		    	System.out.println("DELETED CACHED QUERY");
		    	try {
		    		System.out.println(CacheAPI.cacheSize());
		    		CacheAPI.deleteQuery(event.getQuery().getIndicatorName(), event.getQuery().getCountryName());
		    		System.out.println(CacheAPI.cacheSize());
		    	} catch(Exception e){
		    		System.out.println("THE QUERY CAN NOT BE DELETED");
		    	}
		        event.consume();
		    }
		  };

		 mainView.getCachePane().setQueryHandlers(deleteCachedQueryHandler);
	}

}