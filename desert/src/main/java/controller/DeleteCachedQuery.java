package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import main.java.api.Query;

/**
 * This objects represents an event which stores the query object of the cache to delete.
 * @author pietrocalzini
 */
public class DeleteCachedQuery extends Event {

	/**
	 * Query object that represents the cache
	 */
	private Query query;

	/**
	 * Constructs the delete cache event when a graph is created
	 * @param query
	 */
	public DeleteCachedQuery(Query query) {
		super(MouseEvent.MOUSE_PRESSED);
		this.query = query;
	}

	/**
	 * Gets Query object that represents the cache
	 * @return Query object that represents the cache
	 */
	public Query getQuery(){
		return query;
	}
}
