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
		    	System.out.println(event.getTitle());
		        event.consume();
		    }
		  };

		 mainView.getInspectorPane().createButtonHandler(handler);
	}

}