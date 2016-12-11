package main.java.controller;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

public class CreateEvent extends Event {

	private String title;
	private String indicator;
	private String country;
	private String color;
	private String graphType;
	private String startYear;
	private String endYear;

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

	public String getTitle(){
		return title;
	}

	public String getIndicator(){
		return indicator;
	}

	public String getCountry(){
		return country;
	}

	public String getColor(){
		return color;
	}

	public String getGraphType(){
		return graphType;
	}

	public String getStartYear(){
		return startYear;
	}

	public String getEndYear(){
		return endYear;
	}
}
