package main.java.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.event.Event;
import javafx.event.EventHandler;
import main.java.view.MainView;

/**
 * The controller of the graph view
 * @author pietrocalzini
 *
 */
public class GraphController implements EventHandler{

//View
private MainView mainView;

	public GraphController(MainView m){
		mainView = m;
	}


	@Override
	public void handle(Event event) {
		// TODO Auto-generated method stub
		System.out.println(mainView);
	}
}