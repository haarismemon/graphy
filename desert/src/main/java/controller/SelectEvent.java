package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import main.java.graph.Graph;

/**
 * This objects represents an event which stores information about when a graph is selected in the main view.
 * @author pietrocalzini
 */
public class SelectEvent extends Event {

	/**
	 * Store the graph object that is selected in the main view.
	 */
	private Graph graph;

	/**
	 * Constructs the select event when a graph is selected
	 * @param graph - Graph object that is selected
	 */
	public SelectEvent(Graph graph) {
		super(MouseEvent.MOUSE_PRESSED);
		this.graph = graph;
	}

	/**
	 * Get graph object that is selected in the main view.
	 * @return Graph object that is selected in the main view.
	 */
	public Graph getGraph(){
		return graph;
	}
}
