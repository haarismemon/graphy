package main.java;

import main.java.api.WorldBankAPI;

import java.io.IOException;

import javafx.application.Application;

import javafx.stage.Stage;

import main.java.view.MainView;

import main.java.controller.GraphController;

import main.java.api.CacheAPI;

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
		CacheAPI.loadFromFile();
		MainView m = new MainView();
		GraphController gc = new GraphController(m);
		m.assignController(gc);
		m.start();
	}

	@Override
	public void stop(){
    System.out.println("Stage is closing");
    	CacheAPI.saveToFile();
	}
}
