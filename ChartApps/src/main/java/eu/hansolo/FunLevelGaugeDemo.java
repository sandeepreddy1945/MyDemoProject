/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.app.chart.perfomance.dashboard.DashboardUtil;

/**
 * User: hansolo Date: 23.02.16 Time: 12:26
 */
public class FunLevelGaugeDemo extends Application {
	private static final Random RND = new Random();
	private long lastTimerCall;
	private AnimationTimer timer;
	private FunLevelGauge gauge;

	@Override
	public void init() {
		gauge = new FunLevelGauge();
		gauge.setPrefSize(400, 400);

		lastTimerCall = System.nanoTime();
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 10_000_000_000l) {
					gauge.setLevel(RND.nextDouble() * 0.5 + 0.25);
					lastTimerCall = now;
				}
			}
		};
	}

	@Override
	public void start(Stage stage) {
		Label label = new Label("Employee Prfomance");
		label.setFont(Font.font("Verdana", 20));
		label.setTextFill(Color.WHITESMOKE);
		label.setAlignment(Pos.CENTER);
		StackPane pane = new StackPane(gauge);
		
		VBox box = new VBox(5);
		box.getChildren().addAll(label, pane);
		box.setBackground(DashboardUtil.BLACK_BACKGROUND);
		pane.setPadding(new Insets(10));

		Scene scene = new Scene(box);
		scene.setFill(Color.BLACK);

		stage.setTitle("Medusa FunLevelGauge");
		stage.setScene(scene);
		stage.show();

		timer.start();
	}

	@Override
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
