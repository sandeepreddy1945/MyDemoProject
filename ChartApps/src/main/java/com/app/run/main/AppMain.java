/**
 * 
 */
package com.app.run.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.app.chart.animation.DashboardProjectStatus;
import com.app.chart.animation.ScrollTextDataUI;
import com.app.chart.customer.ui.CustomerUI;
import com.app.chart.dashboard.ui.PerfomanceBoardDetails;
import com.app.chart.diagnose.DiagnoseIssues;
import com.app.chart.fx.AddressBook;
import com.app.chart.fx.FilesUtil;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.run.ui.AppSequencerUI;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep Reddy Battula
 *
 */
@Slf4j
public class AppMain extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;
	private Stage stage;
	private Scene mainScene;

	@Override
	public void start(Stage stage) throws Exception {
		log.info("Initializing the file contents required for the App to run!!!");
		FilesUtil.initializeFileSettings();

		this.stage = stage;
		// stage.setMaximized(true);
		stage.setTitle("MPS Org View Editing Options");

		mainScene = new Scene(contructUIPane(), 900, 800);
		stage.setScene(mainScene);
		stage.show();

	}

	public GridPane contructUIPane() {
		GridPane gridPane = new GridPane();
		ImageView addrBookImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/AppChartEditor.PNG")));
		addrBookImage.setFitWidth(230);
		addrBookImage.setFitHeight(230);
		addrBookImage.setPreserveRatio(false);
		Tile addressBookTile = buildTileFromData("Edit Organization Charts", "Org Charts Editor", addrBookImage);

		addressBookTile.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = null;
			try {
				scene = new Scene(new AddressBook().fetchMainDisplayBox(), WIDTH, HEIGHT);
			} catch (IOException e1) {
				log.error( "contructUIPane", e1);
			}
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
			stage.setTitle("MPS Org chart Editor");
		});

		ImageView perfomanceBoardEditorImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/PerfomanceBoard.png")));
		perfomanceBoardEditorImage.setFitWidth(230);
		perfomanceBoardEditorImage.setFitHeight(230);
		perfomanceBoardEditorImage.setPreserveRatio(false);
		Tile perfomanceBoardEditor = buildTileFromData("Edit Perfomance Board", "Perfomance Board Editor",
				perfomanceBoardEditorImage);
		perfomanceBoardEditor.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new PerfomanceBoardDetails(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		// add the main components.
		ImageView runEditorImg = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/RunUI.PNG")));
		runEditorImg.setFitWidth(230);
		runEditorImg.setFitHeight(230);
		runEditorImg.setPreserveRatio(false);
		Tile runUIEditort = buildTileFromData("Dsiplay Order Editor", "View Ordering Editor", runEditorImg);

		runUIEditort.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new AppSequencerUI(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		Tile disgnosticsTool = buildTextTileFromData("Diagnostic Tools", "Runs The App Diagnostics",
				"Run The App Diagnostics Tool.");

		disgnosticsTool.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new DiagnoseIssues(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		Tile projectStatus = buildTextTileFromData("Project Status UI", "To Build and Edit the Current Project Status",
				"UI To edit and Build Project Status");

		projectStatus.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new DashboardProjectStatus(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		Tile scrollTxt = buildTextTileFromData("Scroll Text Builder",
				"UI To Edit and Build Scroll Texts that run in Performance Board", "UI to Build and Edit Scroll Texts");

		scrollTxt.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new ScrollTextDataUI(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});
		
		Tile customerAddittion = buildTextTileFromData("Customer Slide Editor",
				"UI To Edit and Create Customer Visit Slides", "UI to Build and Customer Slide");

		customerAddittion.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new CustomerUI(), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		// add Zoom handlers to the UI
		// currently not working some disruptions.
		/*
		 * addZoomHandlers(scrollTxt, addressBookTile, perfomanceBoardEditor,
		 * projectStatus, disgnosticsTool, runUIEditort);
		 */

		gridPane.add(addressBookTile, 0, 0);
		gridPane.add(perfomanceBoardEditor, 1, 0);
		gridPane.add(projectStatus, 2, 0);
		gridPane.add(runUIEditort, 0, 1);
		gridPane.add(disgnosticsTool, 1, 1);
		gridPane.add(scrollTxt, 2, 1);
		gridPane.add(customerAddittion, 0, 2);

		gridPane.setVgap(20);
		gridPane.setHgap(20);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(10));
		gridPane.setBackground(DashboardUtil.BLACK_BACKGROUND);
		return gridPane;
	}

	/**
	 * To build the Tiles for Adding data.
	 * 
	 * @param tile
	 * @param text
	 * @param imageView
	 * @return
	 */
	private Tile buildTileFromData(String tile, String text, ImageView imageView) {

		Tile dataTile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(250, 250).title(tile).text(text)
				.graphic(imageView).roundedCorners(true).build();

		return dataTile;
	}

	/**
	 * To build a text Tile.
	 * 
	 * @param title
	 * @param text
	 * @param description
	 * @return
	 */
	private Tile buildTextTileFromData(String title, String text, String description) {
		Tile textTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(250, 250).title(title).text(text)
				.description(description).descriptionAlignment(Pos.CENTER).textVisible(true).build();

		return textTile;
	}

	/**
	 * Need to check on this currently some buggy code just flatters off the screen.
	 * 
	 * @param tiles
	 */
	private void addZoomHandlers(Tile... tiles) {
		addZoomHandlers(Arrays.asList(tiles));
	}

	private void addZoomHandlers(List<Tile> tiles) {
		ScaleTransition st = new ScaleTransition(Duration.millis(999));
		// initialize zoom settings.
		st.setCycleCount(1);
		st.setAutoReverse(false);
		tiles.stream().forEach(t -> {
			t.setOnMouseEntered(e -> {
				st.setNode(t);
				st.setFromX(.8);
				st.setFromY(.8);
				st.setToX(1.1);
				st.setToY(1.1);
				st.playFromStart();
			});

			t.setOnMouseExited(e -> {
				st.setNode(t);
				st.setFromX(1.1);
				st.setFromY(1.1);
				st.setToX(1.0);
				st.setToY(1.0);
				st.playFromStart();
			});
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
