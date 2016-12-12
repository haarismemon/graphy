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
import main.java.api.Indicator;
import main.java.api.Country;

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

		    	//UPDATE SELECTROS Select graph from the workspace
		 		EventHandler<SelectEvent> selectGraphHandler = new EventHandler<SelectEvent>() {
		    		public void handle(SelectEvent event) {

		    			mainView.getInspectorPane().setTitle(event.getGraph().getTitle());
		    			mainView.getInspectorPane().setIndicator(event.getGraph().getQuery().getIndicatorName());
		    			mainView.getInspectorPane().setCountry(event.getGraph().getQuery().getCountryName());
		    			mainView.getInspectorPane().setGraphType(event.getGraph().getGraphType());
		    			mainView.getInspectorPane().setStartYear(event.getGraph().getQuery().getStartYear());
		    			mainView.getInspectorPane().setEndYear(event.getGraph().getQuery().getEndYear());
		    			mainView.getInspectorPane().setUpdate();
		        		event.consume();

		    		}
		 		 };

		 		mainView.selectGraphHandlers(selectGraphHandler);
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().createButtonHandler(addGrapEndler);

		 //Delete graph from the main view
		 EventHandler<DeleteEvent> deleteGraphHandler = new EventHandler<DeleteEvent>() {
		    public void handle(DeleteEvent event) {
		    	System.out.println("DELETED");
		    	System.out.println(Indicator.getCode(event.getIndicator()));
		    	System.out.println(Country.getCode(event.getCountry()));

		    	mainView.deleteGraph(new Query(Indicator.getCode(event.getIndicator()),Country.getCode(event.getCountry()),0,0,null));
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

}