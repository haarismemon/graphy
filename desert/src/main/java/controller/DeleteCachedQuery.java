package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import main.java.api.Query;

public class DeleteCachedQuery extends Event {

	private Query query;

	public DeleteCachedQuery(Query query) {
		super(MouseEvent.MOUSE_PRESSED);
		this.query = query;
	}

	public Query getQuery(){
		return query;
	}
}
