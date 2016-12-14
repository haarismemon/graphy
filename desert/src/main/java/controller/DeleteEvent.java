package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import main.java.graph.Graph;

/**
 * This objects represents an event which stores information about when a graph is deleted from the main view.
 * @author pietrocalzini
 */
public class DeleteEvent extends Event {

	/**
	 * Store the graph object that is deleted from the main view.
	 */
	private Graph graph;

	/**
	 * Constructs the delete event when a graph is deleted
	 * @param graph - Graph object that is deleted
	 */
	public DeleteEvent(Graph graph) {
		super(MouseEvent.MOUSE_PRESSED);
		this.graph = graph;
	}

	/**
	 * Get graph object that is deleted from the main view.
	 * @return Graph object that is deleted from the main view.
	 */
	public Graph getGraph(){
		return graph;
	}
}
