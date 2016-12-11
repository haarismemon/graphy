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
		EventHandler<CreateEvent> handler = new EventHandler<CreateEvent>() {
		    public void handle(CreateEvent event) {
		    	System.out.println(event.getIndicator() + " in " + event.getCountry() + " YEARS: " + Integer.parseInt(event.getStartYear()) + " - " + Integer.parseInt(event.getEndYear()));

		    	Map<Integer, Double> dataMap = WorldBankAPI.query(event.getIndicator(), event.getCountry(), Integer.parseInt(event.getStartYear()), Integer.parseInt(event.getEndYear()));
		    	System.out.println(dataMap);
		    	if(dataMap != null){
		    		String title = event.getTitle();
		    		System.out.println(title.isEmpty());
		    		if(title.isEmpty()) {
		    			title = event.getIndicator() + " in " + event.getCountry();
		    		}
		    		mainView.addGraph(title,"Bar Chart",dataMap);
		    	} else {
		    		System.out.println("NO DATA");
		    	}
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().createButtonHandler(handler);
	}

}