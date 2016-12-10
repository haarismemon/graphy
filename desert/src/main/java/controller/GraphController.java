package main.java.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.event.Event;
import javafx.event.EventHandler;
import main.java.view.MainView;
import main.java.api.WorldBankAPI;
import main.java.nlp.InputAnalysis;
import main.java.nlp.QueryData;
import main.java.api.Country;
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
		QueryData data = InputAnalysis.isValidCommand(mainView.getTextField());
		if(data == null) { 
			mainView.invalidQuery();
		} else {
			mainView.removeNotFound();
			System.out.println(data.getIndicator());
			System.out.println(Country.getCountryCode(data.getCountry()));
			Map<Integer, Double> graphMap = WorldBankAPI.query(data.getIndicator(),data.getCountry(),data.getDates()[0],data.getDates()[1]);
			mainView.addGraph(data.getIndicator() + " in" + data.getCountry(), "LineGraph" ,graphMap);
		}
	}
}