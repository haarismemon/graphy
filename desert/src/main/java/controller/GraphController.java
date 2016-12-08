package main.java.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.event.Event;
import javafx.event.EventHandler;
import main.java.view.MainView;
import main.java.api.WorldBankAPI;

import java.util.HashMap;
import java.util.Map;
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
		Map<Integer, Double> graphMap = WorldBankAPI.query("GDP","GB",2010,2012);
		System.out.println(graphMap);
		mainView.addGraph(graphMap);
	}
}