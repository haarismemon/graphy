package main.java;

import main.java.api.WorldBankAPI;

import java.io.IOException;

import javafx.application.Application;

import javafx.stage.Stage;

import main.java.view.MainView;

import main.java.controller.GraphController;


/**
 * This class represents the entry point of the whole application
 *
 * @author pietrocalzini
 */
public class Main extends Application {

    public static void main(String[] args) throws IOException {

    	launch();
    }

    @Override
	public void start(Stage primaryStage) throws Exception {
		MainView m = new MainView();
		GraphController gc = new GraphController(m);
		m.assignController(gc);
		m.start();
	}

}
