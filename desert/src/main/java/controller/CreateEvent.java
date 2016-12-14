package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

/**
 * This objects represents an event which stores information about a graph when it is created.
 * @author pietrocalzini
 */
public class CreateEvent extends Event {

	/**
	 * Title of the graph
	 */
	private String title;
	/**
	 * Indicator name of the query
	 */
	private String indicator;
	/**
	 * Country name of the query
	 */
	private String country;
	/**
	 * Color hex string of the graph
	 */
	private String color;
	/**
	 * String of type of graph
	 */
	private String graphType;
	/**
	 * String of the start year
	 */
	private String startYear;
	/**
	 * String of the end year
	 */
	private String endYear;

	/**
	 * Constructs the create event when a graph is created
	 * @param title Title of the graph
	 * @param indicator Indicator name of the query
	 * @param country Country name of the query
	 * @param graphType String of type of graph
	 * @param color Color hex string of the graph
	 * @param startYear String of the start year
	 * @param endYear String of the end year
	 */
	public CreateEvent(String title, String indicator, String country, String graphType, String color, String startYear, String endYear) {
		super(MouseEvent.MOUSE_PRESSED);
		this.title = title;
		this.indicator = indicator;
		this.graphType = graphType;
		this.country = country;
		this.color = color;
		this.startYear = startYear;
		this.endYear = endYear;
	}

	/**
	 * Gets the title of the graph
	 * @return title of graph
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * Gets the Indicator name of query
	 * @return Indicator Name of query
	 */
	public String getIndicator(){
		return indicator;
	}

	/**
	 * Gets the Country name of query
	 * @return Country name of the query
	 */
	public String getCountry(){
		return country;
	}

	/**
	 * Gets the Color hex string of the graph
	 * @return Color hex string of the graph
	 */
	public String getColor(){
		return color;
	}

	/**
	 * Gets String of type of graph
	 * @return String of type of graph
	 */
	public String getGraphType(){
		return graphType;
	}

	/**
	 * Gets String of the start year
	 * @return String of the start year
	 */
	public String getStartYear(){
		return startYear;
	}

	/**
	 * Gets String of the end year
	 * @return String of the end year
	 */
	public String getEndYear(){
		return endYear;
	}
}
