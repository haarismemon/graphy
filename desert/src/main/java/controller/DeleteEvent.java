package main.java.controller;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

public class DeleteEvent extends Event {

	public DeleteEvent() {
		super(MouseEvent.MOUSE_PRESSED);
	}
}
