package com.app.chart.model.test;

import com.jfoenix.controls.JFXSpinner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SpinnerDemo extends Application {

	@Override
	public void start(final Stage stage) throws Exception {

		StackPane pane = new StackPane();

		JFXSpinner root = new JFXSpinner();

		root.setMinSize(10, 10);

		pane.getChildren().add(root);

		final Scene scene = new Scene(pane, 300, 300);
		// scene.getStylesheets().add(MainDemo.class.getResource("/resources/css/jfoenix-components.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("JFX Spinner Demo");
		stage.show();
	}

	public static void main(final String[] arguments) {
		Application.launch(arguments);
	}
}