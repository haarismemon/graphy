package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import main.java.graph.Graph;

public class SelectEvent extends Event {

	private Graph g;

	public SelectEvent(Graph g) {
		super(MouseEvent.MOUSE_PRESSED);
		this.g = g;
		
	}

	public Graph getGraph(){
		return g;
	}
}
