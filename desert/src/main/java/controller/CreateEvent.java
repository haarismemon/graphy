package main.java.controller;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

public class CreateEvent extends Event {

	private String title;

	public CreateEvent(String title) {
		super(MouseEvent.MOUSE_PRESSED);
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}
