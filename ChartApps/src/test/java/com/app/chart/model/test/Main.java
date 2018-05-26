package com.app.chart.model.test;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private SequentialTransition sequentialTransition;

	@Override
	public void start(Stage primaryStage) {
		try {
			Label label = new Label("Sandeep Reddy \n Battula");
			label.setTextFill(Color.WHITE);
			label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			label.setRotate(270);
			label.setStyle("-fx-background-color: green; -fx-padding: 50px;");
			StackPane centeredLabel = new StackPane(label);

			// fade transistion
			/*
			 * FadeTransition ft = new FadeTransition(Duration.millis(999), centeredLabel);
			 * ft.setFromValue(1.0); ft.setToValue(0.0); ft.setCycleCount(2);
			 * ft.setAutoReverse(true); ft.play();
			 * 
			 * ft.setOnFinished(e -> { System.out.println("Animation Completed."); });
			 */

			// path transistion
			/*
			 * Path path = new Path(); path.getElements().add(new MoveTo(20,20));
			 * path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
			 * path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
			 * PathTransition pathTransition = new PathTransition();
			 * pathTransition.setDuration(Duration.millis(4000));
			 * pathTransition.setPath(path); pathTransition.setNode(centeredLabel);
			 * pathTransition.setOrientation(PathTransition.OrientationType.
			 * ORTHOGONAL_TO_TANGENT); pathTransition.setCycleCount(Timeline.INDEFINITE);
			 * pathTransition.setAutoReverse(true); pathTransition.play();
			 */

			// sequental transistion
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), centeredLabel);
			fadeTransition.setFromValue(1.0f);
			fadeTransition.setToValue(0.3f);
			fadeTransition.setCycleCount(1);
			fadeTransition.setAutoReverse(true);

			TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), centeredLabel);
			translateTransition.setFromX(0);
			translateTransition.setToX(375);
			translateTransition.setCycleCount(2);
			translateTransition.setAutoReverse(true);

			RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), centeredLabel);
			rotateTransition.setByAngle(180f);
			rotateTransition.setCycleCount(4);
			rotateTransition.setAutoReverse(true);

			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), centeredLabel);
			scaleTransition.setFromX(1);
			scaleTransition.setFromY(1);
			scaleTransition.setToX(2);
			scaleTransition.setToY(2);
			scaleTransition.setCycleCount(1);
			scaleTransition.setAutoReverse(true);

			sequentialTransition = new SequentialTransition();
			sequentialTransition.getChildren().addAll(/*fadeTransition, translateTransition, rotateTransition,
					scaleTransition*/ translateTransition);
			sequentialTransition.setCycleCount(Timeline.INDEFINITE);
			sequentialTransition.setAutoReverse(true);

			sequentialTransition.play();

			StackPane root = new StackPane(centeredLabel);
			root.setPadding(new Insets(10));
			root.setPrefSize(300, 100);

			Scene scene = new Scene(root, 800, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
